package com.example.ecoinspira.models.http

data class HttpResponseMessage<T>(
    val result: T?,
    val status: Int,
    val isOk: Boolean,
    val trace: ValidationProblemDetails?,
    val problem: String?,
)

data class ValidationProblemDetails(
    val title: String,
    val errors: Map<String, Array<String>>,
    val detail: String
)

data class EcoAPICallback<D>(
    val onSucess : (res : D) -> Unit,
    val onFailure : (problem : String) -> Unit,
)