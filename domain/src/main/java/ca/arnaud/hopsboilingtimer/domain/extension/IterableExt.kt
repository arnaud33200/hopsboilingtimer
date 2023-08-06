package ca.arnaud.hopsboilingtimer.domain.extension

fun <Key, Value> Iterable<Key>.associateByNotNull(
    valueTransform: (Key) -> Value?,
): Map<Key, Value> = buildMap {
    return associateByNotNull<Key, Key, Value>(
        keyTransform = { it },
        valueTransform = { key, item ->
            valueTransform(key)
        },
    )
}

fun <T, Key, Value> Iterable<T>.associateByNotNull(
    keyTransform: (T) -> Key?,
    valueTransform: (Key, T) -> Value?,
): Map<Key, Value> = buildMap {
    for (item in this@associateByNotNull) {
        val key = keyTransform(item) ?: continue
        val value = valueTransform(key, item) ?: continue
        put(key, value)
    }
}