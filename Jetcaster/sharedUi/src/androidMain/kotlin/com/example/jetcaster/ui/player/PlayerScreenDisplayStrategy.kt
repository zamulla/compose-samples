package com.example.jetcaster.ui.player

import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.FoldingFeature
import com.example.jetcaster.util.isBookPosture
import com.example.jetcaster.util.isSeparatingPosture
import com.example.jetcaster.util.isTableTopPosture

@Composable
actual fun getPlayerScreenDisplayStrategy(windowSizeClass: WindowSizeClass): PlayerScreenDisplayStrategy {
    val displayFeatures = com.example.jetcaster.ui.LocalDisplayFeatures.current
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    return if (
        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED ||
        isBookPosture(foldingFeature) ||
        isTableTopPosture(foldingFeature) ||
        isSeparatingPosture(foldingFeature)
    ) {
        // Determine if we are going to be using a vertical strategy (as if laying out
        // both sides in a column). We want to do so if we are in a tabletop posture,
        // or we have an impactful horizontal fold. Otherwise, we'll use a horizontal strategy.
        if (
            isTableTopPosture(foldingFeature) || (isSeparatingPosture(foldingFeature) && foldingFeature.orientation == FoldingFeature.Orientation.HORIZONTAL)
        ) {
            PlayerScreenDisplayStrategy.VERTICAL
        } else {
            PlayerScreenDisplayStrategy.HORIZONTAL
        }
    } else {
        PlayerScreenDisplayStrategy.REGULAR
    }
}