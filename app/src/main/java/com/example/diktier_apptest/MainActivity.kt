package com.example.diktier_apptest

import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class MainActivity : AppCompatActivity() {
    private var visualizerView: VisualizerView? = null
    lateinit var recorder: MediaRecorder

    private val handler: Handler = Handler()
    val updater: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1)
            val maxAmplitude = recorder.maxAmplitude
            if (maxAmplitude != 0) {
                visualizerView?.addAmplitude(maxAmplitude)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_visualizer_view)
        recorder = MediaRecorder()
        visualizerView = findViewById<View>(R.id.visualizer) as VisualizerView
        val state = Environment.getExternalStorageState()

        val audioDir: File =
            File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AudioMemos")
        audioDir.mkdirs()
        val audioDirPath = audioDir.absolutePath

        val recordingFile = File("$audioDirPath/test.m4a")
        with(recorder) {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFile.absolutePath)
            prepare()
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updater)
        recorder.stop()
        recorder.reset()
        recorder.release()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        handler.post(updater)
    }
}