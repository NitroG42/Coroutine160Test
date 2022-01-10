package com.nitro.coroutine160test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by nitrog42 on 10/01/2022.
 */
class BleLockManager {
    //The status of the lock
    val lockStatus = MutableStateFlow(LockStatus.NotConnected)

    enum class LockStatus {
        NotConnected,
        Locked
    }

    suspend fun connect() {
        // We simulate that the lock is locked on connection, for this test project
        lockStatus.emit(LockStatus.Locked)
    }
}

class MainActivityViewModel : ViewModel() {
    private val lockManager = BleLockManager()

    //Used to register if we have an operation in progress, like a connection
    private val operationInProgress = MutableStateFlow(false)

    // Flow on the state of the screen
    val stateFlow: StateFlow<LockUIState> by lazy {
        combine(lockManager.lockStatus, operationInProgress) { bleLockStatus, operationInProgress ->
            LockUIState(bleLockStatus = bleLockStatus, operationInProgress)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), LockUIState.Empty)
    }

    // We start the connection
    fun connectToLock() = viewModelScope.launch {
        // An operation is running so we start by putting this as true
        operationInProgress.emit(true)
        // We launch the connection
        lockManager.connect()
        // An operation is over
        operationInProgress.emit(false)
    }

    data class LockUIState(
        // The state of lock, either not connected, or locked for testing purpose
        val bleLockStatus: BleLockManager.LockStatus,
        // InProgress is used to display a loader in the screen
        val inProgress: Boolean = false
    ) {
        companion object {
            val Empty = LockUIState(BleLockManager.LockStatus.NotConnected, false)
        }
    }
}