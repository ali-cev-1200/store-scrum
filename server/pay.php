<?php
// Test payment page: replaces the real payment gateway on the local server
require_once(__DIR__ . '/config.php');
$hash = mysqli_escape_string($con, $_GET['hash'] ?? '');
$order = mysqli_fetch_array(mysqli_query($con, "select * from `orders` where `hash`='$hash'"), MYSQLI_ASSOC);
if (!$order) die('order was not found');
$link = "dunibazaar://dunijet.ir/Projects/DuniBazaar/appCheckoutResult?orderId={$order['orderId']}";
if (isset($_GET['pay']) && $order['status'] == 0) {
    foreach (json_decode($order['products'], true) as $productId => $quantity) {
        mysqli_query($con, "update `products` set `soldItem`=`soldItem`+" . (int)$quantity . " where `productId`=" . (int)$productId);
    }
    mysqli_query($con, "delete from `cartProducts` where `userId`='{$order['userId']}'");
    mysqli_query($con, "update `orders` set `status`=1, `paymentTime`='" . time() . "' where `orderId`='{$order['orderId']}'");
    header("Location: $link");
    exit;
}
?>
<meta name="viewport" content="width=device-width, initial-scale=1">
<div style="font-family:sans-serif;text-align:center;margin-top:30%">
  <h2>Test payment</h2>
  <p>Amount: <?= number_format($order['amount'] / 10) ?> Tomans</p>
  <p><a href="?hash=<?= $order['hash'] ?>&pay=1" style="font-size:20px">Pay</a></p>
  <p><a href="<?= $link ?>">Cancel and return to the app</a></p>
</div>
