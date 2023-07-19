package ca.arnaud.hopsboilingtimer.domain.extension

inline fun <T> List<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
    return this.indexOfFirst(predicate).takeIf { index -> index >= 0 }
}