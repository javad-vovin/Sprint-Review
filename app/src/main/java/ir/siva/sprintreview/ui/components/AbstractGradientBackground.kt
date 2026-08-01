package ir.siva.sprintreview.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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

@Composable
fun AbstractGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    
    // Smooth infinite animation for abstract floating gradient shapes
    val infiniteTransition = rememberInfiniteTransition(label = "gradient_animation")
    val animOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1"
    )
    val animOffset2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2"
    )

    // Colors tailored for abstract atmospheric feel using current theme scheme
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val blob1Color = primaryColor.copy(alpha = if (isDark) 0.35f else 0.22f)
    val blob2Color = secondaryColor.copy(alpha = if (isDark) 0.30f else 0.18f)
    val blob3Color = Color(0xFF8B5CF6).copy(alpha = if (isDark) 0.25f else 0.14f) // Soft purple
    val blob4Color = Color(0xFF06B6D4).copy(alpha = if (isDark) 0.25f else 0.16f) // Soft cyan

    val bgGradient = if (isDark) {
        listOf(
            MaterialTheme.colorScheme.background,
            Color(0xFF0B1124),
            Color(0xFF060913)
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.background,
            Color(0xFFF1F5F9),
            Color(0xFFE2E8F0)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = bgGradient))
    ) {
        // Abstract floating radial gradient Orbs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Orb 1: Top Left -> Floating towards Top Right
            val center1 = Offset(
                x = width * (0.15f + 0.35f * animOffset1),
                y = height * (0.10f + 0.20f * animOffset2)
            )
            val radius1 = width.coerceAtLeast(height) * 0.55f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(blob1Color, Color.Transparent),
                    center = center1,
                    radius = radius1
                ),
                center = center1,
                radius = radius1
            )

            // Orb 2: Bottom Right -> Floating towards Center
            val center2 = Offset(
                x = width * (0.85f - 0.30f * animOffset2),
                y = height * (0.75f - 0.25f * animOffset1)
            )
            val radius2 = width.coerceAtLeast(height) * 0.60f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(blob2Color, Color.Transparent),
                    center = center2,
                    radius = radius2
                ),
                center = center2,
                radius = radius2
            )

            // Orb 3: Center Left Purple Accent
            val center3 = Offset(
                x = width * (0.10f + 0.25f * animOffset2),
                y = height * (0.60f + 0.25f * animOffset1)
            )
            val radius3 = width.coerceAtLeast(height) * 0.45f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(blob3Color, Color.Transparent),
                    center = center3,
                    radius = radius3
                ),
                center = center3,
                radius = radius3
            )

            // Orb 4: Top Right Cyan Glow
            val center4 = Offset(
                x = width * (0.80f - 0.20f * animOffset1),
                y = height * (0.20f + 0.15f * animOffset2)
            )
            val radius4 = width.coerceAtLeast(height) * 0.40f
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
