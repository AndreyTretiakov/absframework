package com.tretiakov.absframework.routers

class AbsClosure {

    private val data: HashMap<String, Any> = HashMap()

    fun addObject(key: String, value: Any) {
        data[key] = value
    }

    fun getObject(key: String): Any? {
        return data[key]
    }
}