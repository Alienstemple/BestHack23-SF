package com.example.besthack23

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class FileRepository(private val context: ComponentActivity) {
    private val openLauncher: ActivityResultLauncher<Array<String>> =
        context.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            try {
                uri?.let { openFile(it) }
            } catch (e: Exception) {
                Log.e(MainActivity.TAG, "Cannot open file! ${e.stackTrace}")
            }
        }

    fun uploadAndReadFile(): String {
        openLauncher.launch(arrayOf("text/plain"))
        return "Test"
    }

    private fun openFile(uri: Uri) {
        val data: String = context.contentResolver.openInputStream(uri)?.use {
            String(it.readBytes())
        } ?: throw IllegalStateException("Can't open input stream")

        Log.d(TAG, "Read: $data")
    }

    companion object {
        const val TAG = "FileRepoLog"
    }
}