package com.example.jetcaster.ui.player

import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

enum class PlayerScreenDisplayStrategy { VERTICAL, HORIZONTAL, REGULAR }

@Composable
expect fun getPlayerScreenDisplayStrategy(windowSizeClass: WindowSizeClass): PlayerScreenDisplayStrategy