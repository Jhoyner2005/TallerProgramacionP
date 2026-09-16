

package com.example.tallerpp.logic

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object GameLogic {
    const val GRAVITY = 9.81f
    const val FORCE_MULTIPLIER = 3.2f
    const val HOLE_MARGIN = 10f
    const val MAX_WINNING_SPEED = 8f

    fun calculateAimAngle(ax: Float, ay: Float): Float {
        return atan2(ay.toDouble(), -ax.toDouble()).toFloat()
    }

    fun calculateForce(ax: Float, ay: Float, az: Float): Float {
        val magnitude = sqrt(ax * ax + ay * ay + az * az)
        return (magnitude - GRAVITY).coerceAtLeast(0f)
    }

    fun calculateVelocity(aimAngle: Float, force: Float): Pair<Float, Float> {
        val velX = cos(aimAngle.toDouble()).toFloat() * force * FORCE_MULTIPLIER
        val velY = sin(aimAngle.toDouble()).toFloat() * force * FORCE_MULTIPLIER
        return Pair(velX, velY)
    }

    fun isBallInHole(
        ballX: Float,
        ballY: Float,
        holeX: Float,
        holeY: Float,
        holeRadius: Float,
        currentSpeed: Float
    ): Boolean {
        val dx = ballX - holeX
        val dy = ballY - holeY
        val distance = sqrt(dx * dx + dy * dy)
        return distance < (holeRadius - HOLE_MARGIN) && currentSpeed < MAX_WINNING_SPEED
    }
}
