package id.ryan.suitgame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import id.ryan.suitgame.domain.GameMode
import id.ryan.suitgame.domain.GameState
import id.ryan.suitgame.domain.InGameAppData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        val OPTION = arrayOf("batu", "kertas", "gunting")
    }

    private val _uiState = MutableStateFlow(InGameAppData())
    val uiState: StateFlow<InGameAppData> = _uiState.asStateFlow()
    
    fun startGame(mode: GameMode, chosen: String, chosen2: String) {
        if (mode == GameMode.VERSUS_COM) {
            _uiState.update {
                if (it.state == GameState.FINISH) {
                    return
                }

                it.copy(
                    chosen = mapOf(
                        Pair("player", chosen),
                        Pair("computer", OPTION[Random.nextInt(from = 0, until = 3)])
                    ),
                    state = GameState.LOADING
                )
            }


            viewModelScope.launch {
                delay(4000L)
                calculateWinner()
            }
        }

        if (mode == GameMode.VERSUS_PLAYER) {
            _uiState.update {
                if (it.state == GameState.FINISH) {
                    return
                }

                val player = uiState.value.chosen["player"]
                val computer = uiState.value.chosen["computer"]

                val result = if (player!!.isNotBlank()) player else chosen
                val result2 = if (computer!!.isNotBlank()) computer else chosen2

                it.copy(
                    chosen = mapOf(
                        Pair("player", result),
                        Pair("computer", result2)
                    ),
                    state = GameState.LOADING
                )
            }


            viewModelScope.launch {
                val player = uiState.value.chosen["player"]
                val computer = uiState.value.chosen["computer"]

                val finished = (player?.isNotBlank() ?: false || chosen.isNotBlank())
                        && (computer?.isNotBlank() ?: false || chosen2.isNotBlank())
                if (finished) calculateWinner()
            }
        }
    }

    fun restartGame() {
        _uiState.update {
            it.copy(
                chosen = mapOf(Pair("player", ""), Pair("computer", "")),
                state = GameState.START,
                result = ""
            )
        }
    }

    private fun calculateWinner() {
        _uiState.getAndUpdate {
            val player = it.chosen["player"]
            val computer = it.chosen["computer"]
            val result = if (player == "gunting" && computer == "kertas") {
                "player"
            } else if (computer == "gunting" && player == "kertas") {
                "computer"
            } else if (player == "batu" && computer == "kertas") {
                "computer"
            } else if (computer == "batu" && player == "kertas") {
                "player"
            } else if (player == "batu" && computer == "gunting") {
                "player"
            } else if (computer == "batu" && player == "gunting") {
                "computer"
            } else {
                "draw"
            }

            it.copy(result = result, state = GameState.FINISH)
        }

    }


}