package com.ikhokha.techcheck.domain.util

import kotlinx.coroutines.flow.Flow

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> takeNonNull(
    p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

inline fun <T> performFirebaseTransaction(job: () -> List<T>): List<T> {
    return job()
}