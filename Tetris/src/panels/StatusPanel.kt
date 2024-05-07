package panels

import TetViewModel
import javazoom.jl.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import models.SoundMode
import models.Tracks
import models.State
import java.awt.Color
import java.io.File
import javax.swing.JLabel
import javax.swing.JPanel

class StatusPanel(private val viewModel: TetViewModel) : JPanel() {

    private val statusLabel = JLabel("PLAY")
    private var player: List<Player>? = null
    private var job: Job? = null

    private var statusText = ""
    private var soundState = ""

    init {
        statusLabel.foreground = Color.WHITE
        background = Color.GRAY
        add(statusLabel)

        CoroutineScope(Dispatchers.Default).launch {

            launch {
                viewModel.state.collect {
                    statusText = when (it) {
                        State.PLAY -> "PLAY ('P' - pause, 'Z','X' - rotate, 'M' - sound mode)"
                        State.PAUSE -> "PAUSE ('P' - play, 'Z','X' - rotate,'M' - sound mode)"
                        State.GAME_OVER -> "GAME OVER ('Enter' - restart, 'M' - sound mode)"
                    }
                    statusLabel.text = "$statusText - $soundState"
                }

            }

            launch {
                viewModel.soundMode.collect {
                    soundState = when (it) {
                        SoundMode.SoundAndMusic -> "sound and music"
                        SoundMode.Sound -> "sound"
                        SoundMode.Mute -> "mute"
                    }
                    statusLabel.text = "$statusText - $soundState"
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.soundMode.collect { volume ->
                if (volume == SoundMode.SoundAndMusic) {
                    job = CoroutineScope(Dispatchers.IO).launch {

                        val musicFiles = listOf(Tracks.Korobeiniki, Tracks.Kalinka, Tracks.Katusha).shuffled()

                        while (viewModel.soundMode.value == SoundMode.SoundAndMusic) {
                            player = musicFiles.map { Player(File(it.path).inputStream()) }
                            player?.forEach { it.play() }
                        }

                    }
                } else {
                    player?.forEach { it.close() }
                    job?.cancel()
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.soundTrack.collect {
                launch {
                    if (viewModel.soundMode.value != SoundMode.Mute) {
                        Player(File(it.path).inputStream()).play()
                    }
                }
            }
        }
    }
}