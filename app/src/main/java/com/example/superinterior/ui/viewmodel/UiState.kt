package com.example.superinterior.ui.viewmodel

/**
 * Generic UI state wrapper for API operations
 */
sealed class ApiState<out T> {
    object Idle : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : ApiState<Nothing>()
}

/**
 * Helper to determine if an operation is in progress
 */
fun <T> ApiState<T>.isLoading(): Boolean = this is ApiState.Loading

/**
 * Helper to get data or null
 */
fun <T> ApiState<T>.dataOrNull(): T? = (this as? ApiState.Success)?.data

/**
 * Helper to get error message or null
 */
fun <T> ApiState<T>.errorOrNull(): String? = (this as? ApiState.Error)?.message
