package com.ali1200.store.fakes

import android.content.SharedPreferences

/** In-memory SharedPreferences for JVM unit tests. */
class FakeSharedPreferences : SharedPreferences {

    private val data = mutableMapOf<String, Any?>()

    override fun getAll(): MutableMap<String, *> = data
    override fun getString(key: String?, defValue: String?): String? =
        if (data.containsKey(key)) data[key] as String? else defValue

    @Suppress("UNCHECKED_CAST")
    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? =
        if (data.containsKey(key)) data[key] as MutableSet<String>? else defValues

    override fun getInt(key: String?, defValue: Int): Int = data[key] as Int? ?: defValue
    override fun getLong(key: String?, defValue: Long): Long = data[key] as Long? ?: defValue
    override fun getFloat(key: String?, defValue: Float): Float = data[key] as Float? ?: defValue
    override fun getBoolean(key: String?, defValue: Boolean): Boolean = data[key] as Boolean? ?: defValue
    override fun contains(key: String?): Boolean = data.containsKey(key)
    override fun edit(): SharedPreferences.Editor = Editor()
    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}

    private inner class Editor : SharedPreferences.Editor {
        private val pending = mutableMapOf<String, Any?>()
        private val removed = mutableSetOf<String>()
        private var clearAll = false

        override fun putString(key: String, value: String?) = apply { pending[key] = value }
        override fun putStringSet(key: String, values: MutableSet<String>?) = apply { pending[key] = values }
        override fun putInt(key: String, value: Int) = apply { pending[key] = value }
        override fun putLong(key: String, value: Long) = apply { pending[key] = value }
        override fun putFloat(key: String, value: Float) = apply { pending[key] = value }
        override fun putBoolean(key: String, value: Boolean) = apply { pending[key] = value }
        override fun remove(key: String) = apply { removed.add(key) }
        override fun clear() = apply { clearAll = true }

        override fun commit(): Boolean {
            if (clearAll) data.clear()
            removed.forEach { data.remove(it) }
            data.putAll(pending)
            return true
        }

        override fun apply() {
            commit()
        }
    }
}
