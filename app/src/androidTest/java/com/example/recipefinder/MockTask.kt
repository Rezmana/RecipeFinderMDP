package com.example.recipefinder

import com.google.android.gms.tasks.Task
import org.mockito.Mockito.*

fun <T> mockTask(result: T?): Task<T> {
    // Create a mock Task
    val mockTask = mock(Task::class.java) as Task<T>

    // Simulate success and set the result
    `when`(mockTask.isSuccessful).thenReturn(true)
    `when`(mockTask.result).thenReturn(result)

    return mockTask
}
