package com.example.besthack23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.besthack23.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Flow
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var fileAsString: String? = null  // Строка, с которой можно работать

    private lateinit var openLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerOpenLauncher()
        setUploadFileListener()
    }

    private fun registerOpenLauncher() {
        openLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                try {
                    uri?.let {
                        fileAsString = contentResolver.openInputStream(uri)?.use {
                            String(it.readBytes())
                        } ?: throw IllegalStateException("Can't open input stream")
//                        trySendBlocking(fAString)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Cannot open file! ${e.stackTrace}")
                }
            }

    }

    private fun setUploadFileListener() {
        binding.uploadBtn.setOnClickListener {
            runBlocking {
                launch { open() }.join()
                Log.d(TAG, "File is: $fileAsString")
            }
        }
    }

    private fun open() {  // callbackFlow<String>
        openLauncher.launch(arrayOf("text/plain", "text/fb2+xml", "application/epub+zip"))
    }

    companion object {
        const val TAG = "MainActLog"
    }
}