package com.example.besthack23

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.apache.commons
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.net.URLConnection

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSIONS = 101
    private val READ_WRITE_STORAGE_PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Проверяем наличие разрешений на чтение/запись хранилища устройства и запрашиваем их при необходимости
        if (!hasReadWriteStoragePermissions()) {
            ActivityCompat.requestPermissions(this, READ_WRITE_STORAGE_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Находим элементы интерфейса
        val selectFileButton = findViewById<Button>(R.id.select_file_button)
        val selectedFileTextView = findViewById<TextView>(R.id.selected_file_text_view)

        // Устанавливаем обработчик нажатия на кнопку выбора файла
        selectFileButton.setOnClickListener {
            // Запускаем системный диалог для выбора файла
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT)
        }
    }

    // Обработчик ответа на запрос разрешений
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!hasReadWriteStoragePermissions()) {
                // Если пользователь отказал в разрешении доступа к хранилищу устройства, закрываем приложение
                finish()
            }
        }
    }

    // Обработчик ответа на выбор файла
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK) {
            resultData?.data?.also { uri ->
                val fileType = getFileType(uri)
                if (fileType == FileType.TXT  fileType == FileType.EPUB) {
                    // Читаем содержимое файла и выводим его в текстовое поле
                    val inputStream = contentResolver.openInputStream(uri)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String? = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        stringBuilder.append(System.lineSeparator())
                        line = reader.readLine()
                    }
                    selectedFileTextView.text = stringBuilder.toString()
                } else {
                    selectedFileTextView.text = "Выбранный файл не поддерживается"
                }
            }
        }
    }
    // Определяем тип файла по его расширению или MIME-типу
    private fun getFileType(uri: Uri): FileType {
        val mimeType = contentResolver.getType(uri)
        return when {
            mimeType == "text/plain"
                    uri.toString().endsWith(".fb2") -> FileType.FB2
            mimeType == "application/epub+zip" || uri.toString().endsWith(".epub") -> FileType.EPUB
            else -> FileType.UNKNOWN
        }
    }

    // Проверяем наличие разрешений на чтение/запись хранилища устройства
    private fun hasReadWriteStoragePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    // Перечисление поддерживаемых типов файлов
    enum class FileType {
        TXT,
        FB2,
        EPUB,
        UNKNOWN
    }

}