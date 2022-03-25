package com.example.diktier_apptest

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class MainActivity : AppCompatActivity() {
    private var visualizerView: VisualizerView? = null
    private var myView: MyView? = null
    var recorder: MediaRecorder? = null
    var isRecording = false
    private val handler: Handler = Handler()
    val updater: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1)
            val maxAmplitude = recorder?.maxAmplitude ?: 0
            if (maxAmplitude != 0) {
                visualizerView?.addAmplitude(maxAmplitude)
                myView?.addAmplitude(maxAmplitude)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO), 100)
        }

        setContentView(R.layout.sample_visualizer_view)
        recorder = MediaRecorder()
        visualizerView = findViewById<View>(R.id.visualizer) as VisualizerView
        myView = findViewById(R.id.myViewVisu) as MyView
        setupRecorder(recorder)

        val button = findViewById<TextView>(R.id.txtRecord)
        button.setOnClickListener {
            if(isRecording) {
                isRecording = false
                recorder?.stop()
                recorder?.release()
                recorder = null
                recorder = MediaRecorder()
                button.text = "Start Recording"
            } else {
                isRecording = true
                setupRecorder(recorder)
                button.text = "Stop Recording"
            }
        }
    }

    private fun setupRecorder(recorder: MediaRecorder?) {
        val recordPath = this.getExternalFilesDir("/")?.absolutePath + "/test3.3gp"
        println(recordPath)
        recorder?.let {
            with(it) {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(recordPath)
                prepare()
                start()
                isRecording = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updater)
        recorder?.stop()
        recorder?.reset()
        recorder?.release()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        handler.post(updater)
    }
}