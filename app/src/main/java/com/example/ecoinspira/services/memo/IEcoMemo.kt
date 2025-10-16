package com.example.ecoinspira.services.memo

import android.content.Context
import com.example.ecoinspira.config.keys.EcoMemoChunks

interface IEcoMemo {
    fun save(context : Context, key: String, value : String, chunk : String = EcoMemoChunks().default)
    fun saveAsJson(context: Context, key: String, value: Any, chunk: String = EcoMemoChunks().default)
    fun edit(context : Context, key: String, chunk : String, newValue : String)
    fun exists(context: Context, chunk: String, refId : String) : Boolean
    fun remove(context: Context, key: String, chunk: String)
    fun find(context : Context, key: String, chunk : String = EcoMemoChunks().default) : String?
    fun <T> findAsJson(context: Context, key: String, clazz: Class<T>, chunk: String): T?
    fun <T> getAll(context: Context, chunk: String, tClass: Class<T>): List<T>
}