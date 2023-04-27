package com.example.besthack23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.besthack23.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fileRepository = FileRepository(this)
    private lateinit var fileAsString: String  // Строка, с которой можно работать

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUploadFileListener()
    }

    private fun setUploadFileListener() {
        binding.uploadBtn.setOnClickListener {
            try {
                fileAsString = fileRepository.uploadAndReadFile()
            } catch (e: Exception) {
                Log.e(TAG, "Cannot open file! ${e.stackTrace}")
            }
        }
    }

    companion object {
        const val TAG = "MainActLog"
    }
}