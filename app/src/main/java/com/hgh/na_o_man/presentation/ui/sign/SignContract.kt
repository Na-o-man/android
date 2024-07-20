package com.hgh.na_o_man.presentation.ui.sign

import com.hgh.na_o_man.presentation.base.LoadState
import com.hgh.na_o_man.presentation.base.ViewEvent
import com.hgh.na_o_man.presentation.base.ViewSideEffect
import com.hgh.na_o_man.presentation.base.ViewState

class SignContract {

    data class SignViewState(
        val loadState: LoadState = LoadState.SUCCESS,
    ): ViewState

    sealed class SignSideEffect : ViewSideEffect {

    }

    sealed class SignEvent : ViewEvent {
        object InitSignScreen : SignEvent()
    }
}