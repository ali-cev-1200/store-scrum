<?php

ini_set('display_errors','0');
ini_set('log_errors','0');
require_once((__DIR__).'/config.php');

$headers=apache_request_headers();

if($_GET) $_POST=$_GET;
$data=json_decode(file_get_contents('php://input'),true);
if($data) $_POST=$data;

$method=basename($_SERVER['REQUEST_URI']);
$result=[];

if(isset($_POST['email'])) $_POST['email']=strtolower(trim($_POST['email']));

$session=mysqli_fetch_array(mysqli_query($con,"select * from `sessions` where `token`='".mysqli_escape_string($con,isset($headers['Authorization'])?$headers['Authorization']:'')."'"),MYSQLI_ASSOC);
$isLoggedIn=($session and $session['expiresAt']>time());
if($isLoggedIn) $user=mysqli_fetch_array(mysqli_query($con,"select * from `users` where `userId`='{$session['userId']}'"),MYSQLI_ASSOC);

if($method=='getProducts'){
    $productsList=[];
    $products=mysqli_query($con,"select * from `products`");
    while($product=mysqli_fetch_array($products,MYSQLI_ASSOC)){
        $productsList[]=$product;
    }
    $result=[
        'success'=>true,
        'products'=>$productsList,
    ];
}elseif($method=='getSliderPics'){
    $adsList=[];
    $ads=mysqli_query($con,"select * from `ads` order by rand()");
    $adsList[]=mysqli_fetch_array($ads,MYSQLI_ASSOC);
    $adsList[]=mysqli_fetch_array($ads,MYSQLI_ASSOC);
    $result=[
        'success'=>true,
        'ads'=>$adsList,
    ];
}elseif($method=='getUserData'){
    if($isLoggedIn){
        unset($user['password']);
        $result=[
            'success'=>true,
            'user'=>$user,
        ];
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method=='getUserCart'){
    if($isLoggedIn){
        $productsList=[];
        $products=mysqli_query($con,"select * from `products`");
        while($product=mysqli_fetch_array($products,MYSQLI_ASSOC)){
            $productsList[$product['productId']]=$product;
        }
        $cartData=[];
        $totalPrice=0;
        $cart=mysqli_query($con,"select * from `cartProducts` where `userId`='{$user['userId']}'");
        while($prod=mysqli_fetch_array($cart,MYSQLI_ASSOC)){
            $product=$productsList[$prod['productId']];
            if($prod['quantity']) $cartData[]=[
                "productId"=>$product['productId'],
                "name"=>$product['name'],
                "imgUrl"=>$product['imgUrl'],
                "price"=>$product['price'],
                "category"=>$product['category'],
                "tags"=>$product['tags'],
                "quantity"=>$prod['quantity'],
            ];
            $totalPrice+=$product['price']*$prod['quantity'];
        }
        $result=[
            'success'=>true,
            'productList'=>$cartData,
            'totalPrice'=>$totalPrice,
        ];
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method=='addToCart'){
    $productId=(int) $_POST['productId'];
    if($isLoggedIn){
        $productsList=[];
        $products=mysqli_query($con,"select * from `products`");
        while($product=mysqli_fetch_array($products,MYSQLI_ASSOC)){
            $productsList[$product['productId']]=$product;
        }
        if(isset($productsList[$productId])){
            $stock=mysqli_fetch_array(mysqli_query($con,"select * from `cartProducts` where `userId`='{$user['userId']}' and `productId`='{$productId}'"));
            if(!$stock) mysqli_query($con,"insert into `cartProducts`(`userId`,`productId`,`quantity`) values('{$user['userId']}','{$productId}','1')");
            else mysqli_query($con,"update `cartProducts` set `quantity`=`quantity`+1 where `id`='{$stock['id']}'");
            $result=[
                'success'=>true,
                'message'=>"Product added to cart.",
                'stock'=>$stock?$stock['quantity']+1:1,
            ];
        }else{
            $result=[
                'success'=>false,
                'message'=>"Product was not found.",
            ];
        }
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method=='removeFromCart'){
    $productId=(int) $_POST['productId'];
    if($isLoggedIn){
        $productsList=[];
        $products=mysqli_query($con,"select * from `products`");
        while($product=mysqli_fetch_array($products,MYSQLI_ASSOC)){
            $productsList[$product['productId']]=$product;
        }
        if(isset($productsList[$productId])){
			$stock=mysqli_fetch_array(mysqli_query($con,"select * from `cartProducts` where `userId`='{$user['userId']}' and `productId`='{$productId}'"));
			if($stock and $stock['quantity']) mysqli_query($con,"update `cartProducts` set `quantity`=`quantity`-1 where `id`='{$stock['id']}'");
			else mysqli_query($con,"delete from `cartProducts` where `id`='{$stock['id']}'");
            $result=[
                'success'=>true,
                'message'=>"Product removed from cart.",
                'stock'=>$stock?$stock['quantity']-1:0,
            ];
        }else{
            $result=[
                'success'=>false,
                'message'=>"Product was not found.",
            ];
        }
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method=='getComments'){
    if($isLoggedIn){
        $productId=(int)$_POST['productId'];
        $product=mysqli_fetch_array(mysqli_query($con,"select * from `products` where `productId`='{$productId}'"),MYSQLI_ASSOC);
        if($product){
            $usersList=[];
            $users=mysqli_query($con,"select * from `users`");
            while($user=mysqli_fetch_array($users,MYSQLI_ASSOC)){
                $usersList[$user['userId']]=$user;
            }
            $commentsList=[];
            $comments=mysqli_query($con,"select * from `comments` where `productId`='{$productId}'");
            while($comment=mysqli_fetch_array($comments,MYSQLI_ASSOC)){
                $comment['userEmail']=$usersList[$comment['userId']]['email'];
                unset($comment['productId']);
                unset($comment['userId']);
                $commentsList[]=$comment;
            }
            $result=[
                'success'=>true,
                'comments'=>$commentsList,
            ];
        }else{
            $result=[
                'success'=>false,
                'message'=>"Product was not found.",
            ];
        }
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method=='addNewComment'){
    if($isLoggedIn){
        $productId=(int)$_POST['productId'];
        $product=mysqli_fetch_array(mysqli_query($con,"select * from `products` where `productId`='{$productId}'"),MYSQLI_ASSOC);
        if($product){
            mysqli_query($con,"insert into `comments` (`userId`, `productId`, `text`) VALUES ('{$user['userId']}', '{$productId}', '".mysqli_escape_string($con,$_POST['text'])."')");
            $result=[
                'success'=>true,
                'message'=>"comment was saved.",
            ];
        }else{
            $result=[
                'success'=>false,
                'message'=>"Product was not found.",
            ];
        }
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method=='signUp'){
    $_POST['email']=mysqli_escape_string($con,$_POST['email']);
    $similarUser=mysqli_fetch_array(mysqli_query($con,"select * from `users` where `email`='".mysqli_escape_string($con,$_POST['email'])."'"));
    if(!$similarUser){
        if($_POST['name']){
            if(preg_match("/^[^@]+@[^@]+\.[a-z]{2,6}$/i",$_POST['email'])){
                $expiresAt=(time()+86400*45);
                mysqli_query($con,"insert into `users` (`name`, `email`, `password`, `registeredTime`) VALUES ('".mysqli_escape_string($con,$_POST['name'])."','".mysqli_escape_string($con,$_POST['email'])."','".mysqli_escape_string($con,password_hash($_POST['password'], PASSWORD_DEFAULT))."','".time()."') ");
                $user['userId']=mysqli_insert_id($con);
                if($user['userId']){
                    $token=md5(random_bytes(50));
                    mysqli_query($con,"insert into `sessions`(`userId`,`token`,`expiresAt`) VALUES ('{$user['userId']}','{$token}','{$expiresAt}')");
                    $result=[
                        'success'=>true,
                        'message'=>"User was signed up successfully.",
                        'token'=>$token,
                        'expiresAt'=>$expiresAt,
                    ];
                }else{
                    $result=[
                        'success'=>false,
                        'message'=>"user can't signed up!",
                    ];
                }
            }else{
                $result=[
                    'success'=>false,
                    'message'=>"email is not correct!",
                ];
            }
        }else{
            $result=[
                'success'=>false,
                'message'=>"name is not set!",
            ];
        }
    }else{
        $result=[
            'success'=>false,
            'message'=>"User is already exist.",
        ];
    }
}elseif($method=='signIn'){
    $_POST['email']=mysqli_escape_string($con,$_POST['email']);
    $user=mysqli_fetch_array(mysqli_query($con,"select * from `users` where `email`='".mysqli_escape_string($con,$_POST['email'])."'"));
    if($user){
        if(password_verify($_POST['password'],$user['password'])){
            $expiresAt=(time()+86400*45);
            $token=md5(random_bytes(50));
            mysqli_query($con,"insert into `sessions`(`userId`,`token`,`expiresAt`) VALUES ('{$user['userId']}','{$token}','{$expiresAt}')");
            $result=[
                'success'=>true,
                'token'=>$token,
                'expiresAt'=>$expiresAt,
            ];
        }else{
            $result=[
                'success'=>false,
                'message'=>"Password is not correct.",
            ];
        }
    }else{
        $result=[
            'success'=>false,
            'message'=>"User was not found.",
        ];
    }
}elseif($method=='refreshToken'){
    if($session){
        $expiresAt=(time()+86400*45);
        mysqli_query($con,"update `sessions` set `expiresAt`='{$expiresAt}' where `sessionId`='{$session['sessionId']}'");
        $result=[
            'success'=>true,
            'message'=>"Token was refreshed.",
            'expiresAt'=>$expiresAt,
        ];
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method=="submitOrder"){
    if($isLoggedIn){
        $productsList=[];
        $products=mysqli_query($con,"select * from `products`");
        while($product=mysqli_fetch_array($products,MYSQLI_ASSOC)){
            $productsList[$product['productId']]=$product;
        }
        $cartData=[];
        $totalPrice=0;
        $cart=mysqli_query($con,"select * from `cartProducts` where `userId`='{$user['userId']}'");
        $choosenProducts=[];
        while($prod=mysqli_fetch_array($cart,MYSQLI_ASSOC)){
            $product=$productsList[$prod['productId']];
            if($prod['quantity']) $cartData[]=[
                "productId"=>$product['productId'],
                "name"=>$product['name'],
                "imgUrl"=>$product['imgUrl'],
                "price"=>$product['price'],
                "category"=>$product['category'],
                "tags"=>$product['tags'],
                "quantity"=>$prod['quantity'],
            ];
            $choosenProducts[$product['productId']]=$prod['quantity'];
            $totalPrice+=$product['price']*$prod['quantity'];
        }
        if($cartData){
            $amount=$totalPrice*10;
            $paymentHash=md5(random_bytes(50));
            // Local test payment instead of the real gateway (see pay.php)
            mysqli_query($con,"insert into `orders` (`hash`,`amount`,`userId`, `products`, `data`, `status`, `creationTime`, `paymentTime`) values('{$paymentHash}','{$amount}','{$user['userId']}', '".mysqli_escape_string($con,json_encode($choosenProducts))."', '".mysqli_escape_string($con,json_encode($_POST))."', '0', '".time()."', '0') ");
            $result=[
                'success'=>true,
                'orderId'=>mysqli_insert_id($con),
                'paymentLink'=>"http://{$_SERVER['HTTP_HOST']}".dirname($_SERVER['SCRIPT_NAME'])."/pay.php?hash=".$paymentHash,
            ];
        }else{
            $result=[
                'success'=>false,
                'message'=>"Cart is empty.",
            ];
        }
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method=="checkout"){
    if($isLoggedIn){
        $orderId=(int)$_POST['orderId'];
        $order=mysqli_fetch_array(mysqli_query($con,"select * from `orders` where `orderId`='{$orderId}'"),MYSQLI_ASSOC);
        if($order and $order['userId']==$user['userId']){
            $order['products']=json_decode($order['products']);
            $order['data']=json_decode($order['data']);
            $result=[
                'success'=>true,
                'order'=>$order,
            ];
        }else{
            $result=[
                'success'=>false,
                'message'=>"order was not found.",
            ];
        }
    }else{
        $result=[
            'success'=>false,
            'message'=>"You are not logged in.",
        ];
    }
}elseif($method==null){
    $result=[
        'success'=>false,
        'message'=>"Method isn't set.",
    ];
}else{
    $result=[
        'success'=>false,
        'message'=>"Method {$_POST['method']} was not found.",
    ];
}

header('content-type: application/json');
echo(json_encode($result));