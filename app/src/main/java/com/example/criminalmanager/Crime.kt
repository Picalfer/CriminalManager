package com.example.criminalmanager

import java.util.Calendar
import java.util.UUID

class Crime(private var title: String = "", private var isSolved: Boolean = false) {
    private val id: UUID = UUID.randomUUID()
    private var date: Calendar = Calendar.getInstance()

    fun getId(): UUID {
        return id
    }

    fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getDate(): Calendar {
        return date
    }

    fun setDate(date: Calendar) {
        this.date = date
    }

    fun isSolved(): Boolean {
        return isSolved
    }

    fun setSolved(isSolved: Boolean) {
        this.isSolved = isSolved
    }
}