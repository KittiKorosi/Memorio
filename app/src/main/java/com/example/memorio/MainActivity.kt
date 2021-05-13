package com.example.memorio

import android.content.Intent
import android.location.LocationListener
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {

    public lateinit var easyBtn: Button
    public lateinit var mediumBtn: Button
    public lateinit var hardBtn: Button
    public lateinit var soundBtn: Switch

    private lateinit var myLocationListener: LocationListener

    public val mp: MediaPlayer? = null

    public var tts: TextToSpeech? = null
    public lateinit var mediaPlayer: MediaPlayer


    public fun speak(text: String) {

        var pitch = 1f
        var speed = 1f

        if (tts !== null) {
            tts!!.setPitch(pitch)
            tts!!.setSpeechRate(speed)
            tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.happy)
        mediaPlayer.start()

        if (tts == null) {
            tts = TextToSpeech(this, TextToSpeech.OnInitListener {
                Log.i("tts", "init")
            })

        }

        val easyBtn = findViewById<Button>(R.id.easyBtn)
        easyBtn.setOnClickListener {
            speak("easy game")
            val intent = Intent(this, EasyGameActivity::class.java)
            startActivity(intent)

        }

        val mediumBtn = findViewById<Button>(R.id.mediumBtn)
        mediumBtn.setOnClickListener {
            speak("medium game")
            val intent = Intent(this, MediumGameActivity::class.java)
            startActivity(intent)

        }

        val hardBtn = findViewById<Button>(R.id.hardBtn)
        hardBtn.setOnClickListener {
            speak("hard game")
            val intent = Intent(this, HardGameActivity::class.java)
            startActivity(intent)
        }

        soundBtn = findViewById(R.id.sound)
        soundBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mediaPlayer.start()
            } else {
                mediaPlayer.pause()
            }
        }
    }


    override fun onDestroy() {
        mediaPlayer.stop()
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

//    override fun onPause() {
//        mediaPlayer.pause()
//        super.onPause()
//    }
//
//    override fun onResume() {
//        mediaPlayer.start()
//        super.onResume()
//    }

}
