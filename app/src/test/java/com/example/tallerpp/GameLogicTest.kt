package com.example.tallerpp

import com.example.tallerpp.logic.GameLogic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.PI

class GameLogicTest {

    @Test
    fun testCalculateAimAngle() {
        // ax = -1, ay = 0 -> angle should be 0 (atan2(0, 1))
        assertEquals(0f, GameLogic.calculateAimAngle(-1f, 0f), 0.01f)

        // ax = 0, ay = 1 -> angle should be PI/2 (atan2(1, 0))
        assertEquals((PI / 2).toFloat(), GameLogic.calculateAimAngle(0f, 1f), 0.01f)

        // ax = 1, ay = 0 -> angle should be PI (atan2(0, -1))
        assertEquals(PI.toFloat(), GameLogic.calculateAimAngle(1f, 0f), 0.01f)

        // ax = 0, ay = -1 -> angle should be -PI/2 (atan2(-1, 0))
        assertEquals((-PI / 2).toFloat(), GameLogic.calculateAimAngle(0f, -1f), 0.01f)
    }

    @Test
    fun testCalculateForce() {
        // No motion (gravity only)
        assertEquals(0f, GameLogic.calculateForce(0f, 0f, 9.81f), 0.01f)

        // Strong hit
        val magnitude = 20f
        val expectedForce = magnitude - 9.81f
        assertEquals(expectedForce, GameLogic.calculateForce(0f, 0f, 20f), 0.01f)

        // Force shouldn't be negative
        assertEquals(0f, GameLogic.calculateForce(0f, 0f, 5f), 0.01f)
    }

    @Test
    fun testCalculateVelocity() {
        val angle = 0f
        val force = 10f
        val (velX, velY) = GameLogic.calculateVelocity(angle, force)

        // cos(0) = 1, sin(0) = 0
        assertEquals(force * GameLogic.FORCE_MULTIPLIER, velX, 0.01f)
        assertEquals(0f, velY, 0.01f)

        val angle90 = (PI / 2).toFloat()
        val (velX90, velY90) = GameLogic.calculateVelocity(angle90, force)
        // cos(90) = 0, sin(90) = 1
        assertEquals(0f, velX90, 0.01f)
        assertEquals(force * GameLogic.FORCE_MULTIPLIER, velY90, 0.01f)
    }

    @Test
    fun testIsBallInHole() {
        val holeX = 500f
        val holeY = 250f
        val holeRadius = 50f

        // Ball exactly in the middle, slow speed
        assertTrue(GameLogic.isBallInHole(holeX, holeY, holeX, holeY, holeRadius, 2f))

        // Ball within margin, slow speed
        // Margin is 10, so holeRadius - 10 = 40. Distance 30 should be in.
        assertTrue(GameLogic.isBallInHole(holeX + 30f, holeY, holeX, holeY, holeRadius, 2f))

        // Ball outside margin
        assertFalse(GameLogic.isBallInHole(holeX + 45f, holeY, holeX, holeY, holeRadius, 2f))

        // Ball too fast
        assertFalse(GameLogic.isBallInHole(holeX, holeY, holeX, holeY, holeRadius, 10f))
    }
}
