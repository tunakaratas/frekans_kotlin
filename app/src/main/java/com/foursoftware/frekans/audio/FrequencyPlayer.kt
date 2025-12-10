package com.foursoftware.frekans.audio
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import kotlinx.coroutines.*
import kotlin.math.sin
import kotlin.math.PI
class FrequencyPlayer(private val context: Context? = null) {
    @Volatile
    private var audioTrack: AudioTrack? = null
    @Volatile
    private var isPlaying = false
    private var playbackJob: Job? = null
    private var volume: Float = 1.0f
    private val playerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val lock = Any()
    fun setVolume(volume: Float) {
        val newVolume = volume.coerceIn(0f, 1f)
        synchronized(lock) {
            this.volume = newVolume
            try {
                audioTrack?.setVolume(newVolume)
            } catch (e: Exception) {
            }
        }
    }
    fun getVolume(): Float = synchronized(lock) { volume }
    fun playFrequency(frequency: Double, sampleRate: Int = 44100, volume: Float = 1.0f) {
        synchronized(lock) {
            if (isPlaying) {
                stopInternal()
            }
            this.volume = volume.coerceIn(0f, 1f)
            isPlaying = true
            try {
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                if (bufferSize <= 0) {
                    isPlaying = false
                    return
                }
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                val audioFormat = AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
                val audioContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && context != null) {
                    try {
                        context.createAttributionContext("audioPlayback")
                    } catch (e: Exception) {
                        context
                    }
                } else {
                    context
                }
                val trackBuilder = AudioTrack.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(bufferSize * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                val track = trackBuilder.build()
                if (track.state != AudioTrack.STATE_INITIALIZED) {
                    track.release()
                    isPlaying = false
                    return
                }
                audioTrack = track
                track.setVolume(this.volume)
                track.play()
                playbackJob = playerScope.launch {
                    generateTone(frequency, sampleRate, track)
                }
            } catch (e: Exception) {
                isPlaying = false
                try {
                    audioTrack?.release()
                } catch (ex: Exception) {
                }
                audioTrack = null
            }
        }
    }
    private suspend fun generateTone(
        frequency: Double,
        sampleRate: Int,
        track: AudioTrack
    ) {
        val samples = (sampleRate / 10).coerceAtLeast(1024).coerceAtMost(8192)
        val buffer = ShortArray(samples)
        try {
            while (isPlaying) {
                if (track.state != AudioTrack.STATE_INITIALIZED) {
                    break
                }
                val currentVolume = synchronized(lock) { volume }
                for (i in buffer.indices) {
                    val angle = 2.0 * PI * frequency * i / sampleRate
                    val sample = sin(angle) * currentVolume
                    buffer[i] = (sample * Short.MAX_VALUE).toInt().coerceIn(
                        Short.MIN_VALUE.toInt(),
                        Short.MAX_VALUE.toInt()
                    ).toShort()
                }
                val bytesWritten = track.write(buffer, 0, buffer.size, AudioTrack.WRITE_NON_BLOCKING)
                if (bytesWritten < 0) {
                    break
                }
                if (bytesWritten < buffer.size) {
                    delay(1)
                }
            }
        } catch (e: CancellationException) {
        } catch (e: Exception) {
        } finally {
            synchronized(lock) {
                isPlaying = false
            }
        }
    }
    private fun stopInternal() {
        isPlaying = false
        playbackJob?.cancel()
        playbackJob = null
        val track = audioTrack
        audioTrack = null
        try {
            track?.stop()
        } catch (e: Exception) {
        }
        try {
            track?.release()
        } catch (e: Exception) {
        }
    }
    fun stop() {
        synchronized(lock) {
            stopInternal()
        }
    }
    fun isCurrentlyPlaying(): Boolean = synchronized(lock) { isPlaying }
    fun release() {
        synchronized(lock) {
            stopInternal()
            playerScope.cancel()
        }
    }
}
