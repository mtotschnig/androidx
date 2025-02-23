/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.tracing

/**
 * Wrap the specified [block] in calls to [Trace.beginSection] (with the supplied [label])
 * and [Trace.endSection].
 *
 * @param label A name of the code section to appear in the trace.
 * @param block A block of code which is being traced.
 */
public inline fun <T> trace(label: String, crossinline block: () -> T): T {
    Trace.beginSection(label)
    try {
        return block()
    } finally {
        Trace.endSection()
    }
}

/**
 * Wrap the specified [block] in calls to [Trace.beginSection] (with a lazy-computed
 * [lazyLabel], only if tracing is enabled - [Trace.isEnabled]) and [Trace.endSection].
 *
 * This variant allows you to build a dynamic label, but only when tracing is enabled, avoiding
 * the cost of String construction otherwise.
 *
 * @param lazyLabel A name of the code section to appear in the trace, computed lazily if needed.
 * @param block A block of code which is being traced.
 */
public inline fun <T> trace(
    lazyLabel: () -> String,
    block: () -> T
): T {
    val isEnabled = Trace.isEnabled()
    if (isEnabled) {
        Trace.beginSection(lazyLabel())
    }
    try {
        return block()
    } finally {
        if (isEnabled) {
            Trace.endSection()
        }
    }
}

/**
 * Wrap the specified [block] in calls to [Trace.beginAsyncSection] (with the supplied [methodName]
 * and [cookie]) and [Trace.endAsyncSection].
 *
 * @param methodName The method name to appear in the trace.
 * @param cookie Unique identifier for distinguishing simultaneous events
 */
public suspend inline fun <T> traceAsync(
    methodName: String,
    cookie: Int,
    crossinline block: suspend () -> T
): T {
    Trace.beginAsyncSection(methodName, cookie)
    try {
        return block()
    } finally {
        Trace.endAsyncSection(methodName, cookie)
    }
}

/**
 * Wrap the specified [block] in calls to [Trace.beginAsyncSection] and [Trace.endAsyncSection],
 * with a lazy-computed [lazyMethodName] and [lazyCookie], only if tracing is
 * enabled - [Trace.isEnabled].
 *
 * @param lazyMethodName The method name to appear in the trace, computed lazily if needed.
 * @param lazyCookie Unique identifier for distinguishing simultaneous events,
 *         computed lazily if needed.
 */
public inline fun <T> traceAsync(
    lazyMethodName: () -> String,
    lazyCookie: () -> Int,
    block: () -> T
): T {
    var methodName: String? = null
    var cookie = 0
    if (Trace.isEnabled()) {
        methodName = lazyMethodName()
        cookie = lazyCookie()
        Trace.beginAsyncSection(methodName, cookie)
    }
    try {
        return block()
    } finally {
        if (methodName != null) {
            Trace.endAsyncSection(methodName, cookie)
        }
    }
}
