package com.example.criminalmanager

import java.util.UUID

class Crime(private var title: String = "") {
    private val id: UUID = UUID.randomUUID()

    fun getId(): UUID {
        return id
    }

    fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }
}