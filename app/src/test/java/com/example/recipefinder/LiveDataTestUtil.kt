package com.example.recipefinder

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Helper function to get the value of a LiveData for testing purposes.
 *
 * @param time The maximum time to wait for the LiveData value
 * @param timeUnit The time unit for the waiting period
 * @param afterObserve Optional lambda to be executed after setting up the observer
 * @return The value of the LiveData
 * @throws TimeoutException If the LiveData value is not set within the specified time
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    // Variable to store the result
    var data: T? = null

    // Latch to synchronize the test
    val latch = CountDownLatch(1)

    // Observer to capture the value
    val observer = Observer<T> { value ->
        data = value
        latch.countDown()
    }

    // Use ArchTaskExecutor to ensure main thread observation
    ArchTaskExecutor.getInstance().executeOnMainThread {
        this.observeForever(observer)
    }

    try {
        // Execute any additional setup
        afterObserve()

        // Wait for the value or throw a timeout exception
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        // Throw an exception if no data was received
        return data ?: throw IllegalStateException("LiveData value was null")
    } finally {
        // Ensure observer is removed on the main thread
        ArchTaskExecutor.getInstance().executeOnMainThread {
            this.removeObserver(observer)
        }
    }
}