package com.nitro.coroutine160test

import app.cash.turbine.test
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by t.coulange on 10/01/2022.
 */
class TestCustomViewModel {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Test
    fun testNormalBehavior() = coroutineTestRule.testDispatcher.runBlockingTest {
        val viewModel = MainActivityViewModel()
        //We use Turbine to https://github.com/cashapp/turbine to easy flow tests
        viewModel.stateFlow.test {
            // Before we connect, we should have no operation in progress,
            Assert.assertEquals(MainActivityViewModel.LockUIState(BleLockManager.LockStatus.NotConnected, false), awaitItem())
            // We start the connection to the lock
            viewModel.connectToLock()
            // We expect the inProgress to switch to true
            Assert.assertEquals(MainActivityViewModel.LockUIState(BleLockManager.LockStatus.NotConnected, true), awaitItem())
            // We are connected so we display the lock status
            Assert.assertEquals(MainActivityViewModel.LockUIState(BleLockManager.LockStatus.Locked, true), awaitItem())
            // The operation is now stopped
            Assert.assertEquals(MainActivityViewModel.LockUIState(BleLockManager.LockStatus.Locked, false), awaitItem())
        }
    }
}