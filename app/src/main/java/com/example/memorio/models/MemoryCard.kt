package com.example.memorio.models

import android.service.carrier.CarrierIdentifier
import android.speech.tts.TextToSpeech
import android.util.Log

data class MemoryCard (
    val identifier: Int,
    var isFacedUp: Boolean = false,
    var isMatched: Boolean = false
        )