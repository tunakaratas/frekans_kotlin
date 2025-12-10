package com.foursoftware.frekans.audio
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import kotlinx.coroutines.*
import kotlin.math.sin
import kotlin.math.PI

class MultiFrequencyPlayer(private val context: Context? = null) {
    private val activeTracks = mutableMapOf<Double, AudioTrack>()
    private val activeJobs = mutableMapOf<Double, Job>()
    private var volume: Float = 1.0f
    private val playerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val lock = Any()
    fun setVolume(volume: Float) {
        val newVolume = volume.coerceIn(0f, 1f)
        synchronized(lock) {
            this.volume = newVolume
            activeTracks.values.forEach { track ->
                try {
                    track.setVolume(newVolume)
                } catch (e: Exception) {
                }
            }
        }
    }
    fun getVolume(): Float = synchronized(lock) { volume }
    fun playFrequency(frequency: Double, sampleRate: Int = 44100, volume: Float = 1.0f) {
        synchronized(lock) {
            if (activeTracks.containsKey(frequency)) {
                return
            }
            this.volume = volume.coerceIn(0f, 1f)
            try {
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                if (bufferSize <= 0) {
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
                val trackBuilder = AudioTrack.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(bufferSize * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                val track = trackBuilder.build()
                if (track.state != AudioTrack.STATE_INITIALIZED) {
                    track.release()
                    return
                }
                activeTracks[frequency] = track
                track.setVolume(this.volume)
                track.play()
                val job = playerScope.launch {
                    generateTone(frequency, sampleRate, track)
                }
                activeJobs[frequency] = job
            } catch (e: Exception) {
                try {
                    activeTracks[frequency]?.release()
                } catch (ex: Exception) {
                }
                activeTracks.remove(frequency)
                activeJobs.remove(frequency)
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
            while (activeTracks.containsKey(frequency)) {
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
                activeTracks.remove(frequency)
                activeJobs.remove(frequency)
            }
        }
    }
    fun stopFrequency(frequency: Double) {
        synchronized(lock) {
            activeJobs[frequency]?.cancel()
            activeJobs.remove(frequency)
            val track = activeTracks.remove(frequency)
            try {
                track?.stop()
            } catch (e: Exception) {
            }
            try {
                track?.release()
            } catch (e: Exception) {
            }
        }
    }
    fun stopAll() {
        synchronized(lock) {
            val frequencies = activeTracks.keys.toList()
            frequencies.forEach { frequency ->
                stopFrequency(frequency)
            }
        }
    }
    fun isFrequencyPlaying(frequency: Double): Boolean = synchronized(lock) {
        activeTracks.containsKey(frequency)
    }
    fun getActiveFrequencies(): Set<Double> = synchronized(lock) {
        activeTracks.keys.toSet()
    }
    fun release() {
        synchronized(lock) {
            stopAll()
            playerScope.cancel()
        }
    }
}
