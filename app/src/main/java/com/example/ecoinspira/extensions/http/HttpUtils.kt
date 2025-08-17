package com.example.ecoinspira.extensions.http

import com.example.ecoinspira.models.http.EcoAPICallback
import com.example.ecoinspira.models.http.HttpResponseMessage

fun <D> HttpResponseMessage<D>.serializeAndResolve(options: EcoAPICallback<D>) {
    if (!this.isOk) options.onFailure(
        this.problem ?: "Não foi possivel processar sua requisição"
    )
    else this.result?.let { options.onSucess(it) }
}

fun HttpResponseMessage<Any>.serializeAndResponse(options: EcoAPICallback<Any>) {
    if (!this.isOk) options.onFailure(
        this.problem ?: "Não foi possivel processar sua requisição"
    )
    else {
        options.onSucess("")
    }
}