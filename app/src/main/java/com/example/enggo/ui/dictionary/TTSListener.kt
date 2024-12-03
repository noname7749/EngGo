package com.example.enggo.ui.dictionary
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class TTSListener(context: Context, private val onSpeechCompleted: () -> Unit) :
    DefaultLifecycleObserver {
    private var onInit = false
    private var textToSpeechEngine: TextToSpeech = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            onInit = true
        } else {
            Log.e("TTS", "Initialization failed!")
        }
    }

    fun speak(text: String) {
        if (onInit) {
            val params = Bundle()
            params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        } else {
            Log.e("TTS", "TTS Engine is not initialized yet!")
        }
    }

    fun stop() {
        textToSpeechEngine.stop()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        textToSpeechEngine.setOnUtteranceProgressListener(object :
            UtteranceProgressListener() {

            @Deprecated("Deprecated in Java")
            override fun onError(p0: String?) {
            }

            override fun onStart(p0: String?) {

            }

            override fun onDone(p0: String?) {
                onSpeechCompleted()
            }
        })
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        textToSpeechEngine.stop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        textToSpeechEngine.shutdown()
    }
}