package com.foursoftware.frekans.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foursoftware.frekans.audio.MultiFrequencyPlayer
import com.foursoftware.frekans.data.Plant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class PlantDetailViewModel(private val context: Context? = null) : ViewModel() {
    private val multiFrequencyPlayer = MultiFrequencyPlayer(context)
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    private val _activeFrequencies = MutableStateFlow<Set<Double>>(emptySet())
    val activeFrequencies: StateFlow<Set<Double>> = _activeFrequencies.asStateFlow()
    private val _elapsedTime = MutableStateFlow(0)
    val elapsedTime: StateFlow<Int> = _elapsedTime.asStateFlow()
    private val _volume = MutableStateFlow(0.5f)
    val volume: StateFlow<Float> = _volume.asStateFlow()
    private val _isShuffleEnabled = MutableStateFlow(false)
    val isShuffleEnabled: StateFlow<Boolean> = _isShuffleEnabled.asStateFlow()
    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()
    private var currentPlant: Plant? = null
    private var currentFrequencyIndex = 0
    private var currentFrequency: Double? = null
    init {
        viewModelScope.launch {
            while (true) {
                delay(500)
                _activeFrequencies.value = multiFrequencyPlayer.getActiveFrequencies()
                _isPlaying.value = _activeFrequencies.value.isNotEmpty()
            }
        }
        viewModelScope.launch {
            while (true) {
                if (_isPlaying.value) {
                    delay(1000)
                    _elapsedTime.value = _elapsedTime.value + 1
                } else {
                    delay(100)
                }
            }
        }
    }
    fun setPlant(plant: Plant) {
        currentPlant = plant
        stopAllFrequencies()
    }
    fun toggleFrequency(frequency: Double) {
        if (multiFrequencyPlayer.isFrequencyPlaying(frequency)) {
            multiFrequencyPlayer.stopFrequency(frequency)
        } else {
            currentPlant?.let {
                multiFrequencyPlayer.playFrequency(frequency, volume = _volume.value)
            }
        }
    }
    fun stopFrequency(frequency: Double) {
        multiFrequencyPlayer.stopFrequency(frequency)
    }
    fun stopAllFrequencies() {
        multiFrequencyPlayer.stopAll()
        _elapsedTime.value = 0
    }
    fun togglePlayback() {
        if (_isPlaying.value) {
            stopAllFrequencies()
        } else {
            val activeFreqs = multiFrequencyPlayer.getActiveFrequencies()
            if (activeFreqs.isNotEmpty()) {
            } else {
                currentPlant?.let {
                    if (it.frequencies.isNotEmpty()) {
                        playFrequency(it.frequencies.first())
                    } else {
                        playFrequency(it.frequency)
                    }
                }
            }
        }
    }
    fun setVolume(volume: Float) {
        _volume.value = volume.coerceIn(0f, 1f)
        multiFrequencyPlayer.setVolume(_volume.value)
    }
    fun resetTimer() {
        _elapsedTime.value = 0
    }
    fun toggleShuffle() {
        _isShuffleEnabled.value = !_isShuffleEnabled.value
    }
    fun toggleRepeat() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }
    fun playNextFrequency() {
        currentPlant?.let { plant ->
            val frequencies = if (plant.frequencies.isNotEmpty()) {
                plant.frequencies
            } else {
                listOf(plant.frequency)
            }
            if (frequencies.isNotEmpty()) {
                if (_isShuffleEnabled.value) {
                    val randomIndex = (0 until frequencies.size).random()
                    currentFrequencyIndex = randomIndex
                    currentFrequency = frequencies[randomIndex]
                    playFrequency(frequencies[randomIndex])
                } else {
                    currentFrequencyIndex = (currentFrequencyIndex + 1) % frequencies.size
                    currentFrequency = frequencies[currentFrequencyIndex]
                    playFrequency(frequencies[currentFrequencyIndex])
                }
            }
        }
    }
    fun playPreviousFrequency() {
        currentPlant?.let { plant ->
            val frequencies = if (plant.frequencies.isNotEmpty()) {
                plant.frequencies
            } else {
                listOf(plant.frequency)
            }
            if (frequencies.isNotEmpty()) {
                if (_isShuffleEnabled.value) {
                    val randomIndex = (0 until frequencies.size).random()
                    currentFrequencyIndex = randomIndex
                    currentFrequency = frequencies[randomIndex]
                    playFrequency(frequencies[randomIndex])
                } else {
                    currentFrequencyIndex = if (currentFrequencyIndex == 0) {
                        frequencies.size - 1
                    } else {
                        currentFrequencyIndex - 1
                    }
                    currentFrequency = frequencies[currentFrequencyIndex]
                    playFrequency(frequencies[currentFrequencyIndex])
                }
            }
        }
    }
    fun playFrequency(frequency: Double) {
        stopAllFrequencies()
        currentFrequency = frequency
        currentPlant?.let {
            multiFrequencyPlayer.playFrequency(frequency, volume = _volume.value)
            val frequencies = if (it.frequencies.isNotEmpty()) {
                it.frequencies
            } else {
                listOf(it.frequency)
            }
            currentFrequencyIndex = frequencies.indexOf(frequency).takeIf { it >= 0 } ?: 0
        }
    }
    override fun onCleared() {
        super.onCleared()
        multiFrequencyPlayer.release()
    }
}
enum class RepeatMode {
    OFF,
    ALL,
    ONE
}
