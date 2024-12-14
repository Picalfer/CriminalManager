package com.example.criminalmanager.model

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class Crime(private var title: String = "", private var isSolved: Boolean = false) {
    private var id: UUID = UUID.randomUUID()
    private var date: Calendar = Calendar.getInstance()
    private var photo: Photo? = null

    constructor(json: JSONObject) : this() {
        id = UUID.fromString(json.getString(ID))
        title = json.getString(TITLE)
        isSolved = json.getBoolean(SOLVED)
        date = Calendar.getInstance().apply {
            // Парсим строку даты обратно в Calendar
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            time = dateFormat.parse(json.getString(DATE)) ?: Date()
        }

        if (json.has(PHOTO))
            photo = Photo(json.getJSONObject(PHOTO))
    }

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

    fun getPhoto(): Photo? = photo

    fun setPhoto(p: Photo) {
        photo = p
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put(ID, id)
        json.put(TITLE, title)
        json.put(SOLVED, isSolved)

        // Форматируем дату в строку
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        json.put(DATE, dateFormat.format(date.time)) // Сохраняем отформатированную дату

        if (photo != null)
            json.put(PHOTO, photo!!.toJSON())

        return json
    }

    companion object {
        const val ID = "id"
        const val TITLE = "title"
        const val SOLVED = "solved"
        const val DATE = "date"
        const val PHOTO = "photo"
    }
}