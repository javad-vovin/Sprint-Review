package ir.siva.sprintreview.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AbstractGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    
    // Smooth infinite continuous animation for abstract floating gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "gradient_animation")
    
    // Continuous 360 degree rotation phase for gradient angle
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase2"
    )

    // Colors tailored for abstract atmospheric feel using current theme scheme
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val blob1Color = primaryColor.copy(alpha = if (isDark) 0.38f else 0.25f)
    val blob2Color = secondaryColor.copy(alpha = if (isDark) 0.32f else 0.20f)
    val blob3Color = Color(0xFF8B5CF6).copy(alpha = if (isDark) 0.28f else 0.16f) // Soft purple
    val blob4Color = Color(0xFF06B6D4).copy(alpha = if (isDark) 0.28f else 0.18f) // Soft cyan

    val bgGradient = if (isDark) {
        listOf(
            MaterialTheme.colorScheme.background,
            Color(0xFF0D1427),
            Color(0xFF070B16)
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.background,
            Color(0xFFEEF2FF),
            Color(0xFFE0E7FF)
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Moving base linear gradient with dynamic start & end points rotating gracefully
            val startX = width * (0.5f + 0.5f * cos(phase1))
            val startY = height * (0.5f + 0.5f * sin(phase1))
            val endX = width * (0.5f - 0.5f * cos(phase1))
            val endY = height * (0.5f - 0.5f * sin(phase1))

            drawRect(
                brush = Brush.linearGradient(
                    colors = bgGradient,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY)
                )
            )

            // 2. Orb 1: Floating top-left to top-right path
            val center1 = Offset(
                x = width * (0.35f + 0.25f * cos(phase1)),
                y = height * (0.25f + 0.15f * sin(phase2))
            )
            val radius1 = width.coerceAtLeast(height) * (0.50f + 0.08f * sin(phase1))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(blob1Color, Color.Transparent),
                    center = center1,
                    radius = radius1
                ),
                center = center1,
                radius = radius1
            )

            // 3. Orb 2: Floating bottom-right orbit
            val center2 = Offset(
                x = width * (0.70f + 0.20f * sin(phase1)),
                y = height * (0.70f + 0.18f * cos(phase2))
            )
            val radius2 = width.coerceAtLeast(height) * (0.55f + 0.06f * cos(phase1))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(blob2Color, Color.Transparent),
                    center = center2,
                    radius = radius2
                ),
                center = center2,
                radius = radius2
            )

            // 4. Orb 3: Floating middle-left purple accent
            val center3 = Offset(
                x = width * (0.20f + 0.22f * sin(phase2)),
                y = height * (0.55f + 0.20f * cos(phase1))
            )
            val radius3 = width.coerceAtLeast(height) * (0.42f + 0.05f * sin(phase2))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(blob3Color, Color.Transparent),
                    center = center3,
                    radius = radius3
                ),
                center = center3,
                radius = radius3
            )

            // 5. Orb 4: Floating top-right cyan accent
            val center4 = Offset(
                x = width * (0.75f - 0.18f * cos(phase2)),
                y = height * (0.25f + 0.16f * sin(phase1))
            )
            val radius4 = width.coerceAtLeast(height) * (0.38f + 0.07f * cos(phase2))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(blob4Color, Color.Transparent),
                    center = center4,
                    radius = radius4
                ),
                center = center4,
                radius = radius4
            )
        }

        content()
    }
}
