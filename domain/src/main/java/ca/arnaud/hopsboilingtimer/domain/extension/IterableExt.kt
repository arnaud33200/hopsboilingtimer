package ca.arnaud.hopsboilingtimer.domain.extension

fun <Key, Value> Iterable<Key>.associateByNotNull(
    valueTransform: (Key) -> Value?,
): Map<Key, Value> = buildMap {
    for (key in this@associateByNotNull) {
        val value = valueTransform(key) ?: continue
        this[key] = value
    }
}