package com.example.criminalmanager.data

import android.content.Context
import android.util.Log
import com.example.criminalmanager.model.Crime
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Writer

class CriminalManagerJSONSerializer(private val context: Context, private val filename: String) {
    fun saveCrimes(crimes: List<Crime>) {
        val array = JSONArray()
        for (crime in crimes) {
            array.put(crime.toJSON())
        }
        Log.d("CriminalIntentJSON", array.toString())
        var writer: Writer? =
            null // Инициализируем переменную writer как null, чтобы использовать ее позже для записи в файл
        try {
            // Открываем файл для записи в режиме PRIVATE, что означает, что файл будет доступен только этому приложению
            val out = context.openFileOutput(filename, Context.MODE_PRIVATE)

            // Создаем OutputStreamWriter, который будет использоваться для записи в выходной поток
            writer = OutputStreamWriter(out)

            // Записываем строковое представление JSON-массива в файл
            writer.write(array.toString())
        } finally {
            // Закрываем writer, если он не равен null, чтобы освободить ресурсы
            writer?.close()
        }
    }

    fun loadCrimes(): List<Crime> {
        val crimes = ArrayList<Crime>()
        var reader: BufferedReader? = null
        try {
            // Открытие и чтение файла в StringBuilder
            val inputStream = context.openFileInput(filename)
            reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = StringBuilder()
            var line: String?

            // Чтение файла построчно
            while (reader.readLine().also { line = it } != null) {
                // Строки с переносами игнорируются
                jsonString.append(line)
            }

            // Разбор JSON с использованием JSONTokener
            val array = JSONTokener(jsonString.toString()).nextValue() as JSONArray

            // Построение массива объектов Crime по данным JSONObject
            for (i in 0 until array.length()) {
                crimes.add(Crime(array.getJSONObject(i)))
            }
        } catch (e: FileNotFoundException) {
            // Происходит при начале "с нуля"; не обращайте внимания
        } finally {
            // Закрываем reader, если он не равен null
            reader?.close()
        }
        return crimes
    }
}
