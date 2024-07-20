package com.hgh.na_o_man.presentation.ui.detail

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hgh.na_o_man.presentation.theme.NaOManTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("리컴포저블", "GroupDetailActivity")
        setContent {
            NaOManTheme {
                GroupDetailScreen()
            }
        }
    }

    private fun closeActivity() {
        finish()
    }

    companion object {
        const val GROUP_DETAIL = "GroupDetail"
    }
}