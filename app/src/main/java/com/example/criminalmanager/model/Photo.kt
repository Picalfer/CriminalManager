package com.example.criminalmanager.model

import org.json.JSONObject

class Photo(val filename: String) {
    constructor(json: JSONObject) : this(json.getString(JSON_FILENAME))

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put(JSON_FILENAME, filename)
        return json
    }

    companion object {
        const val JSON_FILENAME = "filename"
    }
}