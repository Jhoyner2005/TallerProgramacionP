package com.example.tallerpp.composables
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tallerpp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun GameScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var aimAngle by remember { mutableFloatStateOf(0f) }
    var isMoving by remember { mutableStateOf(false) }
    var strokeCount by remember { mutableIntStateOf(0) }

    val sensorManager = remember {
        context.getSystemService(SensorManager::class.java)
    }

    var ballX by remember { mutableFloatStateOf(0f) }
    var ballY by remember { mutableFloatStateOf(0f) }
    var ballVelX by remember { mutableFloatStateOf(0f) }
    var ballVelY by remember { mutableFloatStateOf(0f) }

    var canvasWidth by remember { mutableFloatStateOf(0f) }
    var canvasHeight by remember { mutableFloatStateOf(0f) }
    var isInitialized by remember { mutableStateOf(false) }
    var hasWon by remember { mutableStateOf(false) }

    val ballRadius = 30f
    val holeRadius = 50f

    DisposableEffect(sensorManager) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val ax = event.values[0]
                    val ay = event.values[1]
                    val az = event.values[2]

                    if (!isMoving && !hasWon) {
                        val tiltMagnitude = sqrt(ax * ax + ay * ay)
                        if (tiltMagnitude > 1.5f) {
                            aimAngle = atan2(ay.toDouble(), -ax.toDouble()).toFloat()
                        }
                    }

                    val magnitude = sqrt(ax * ax + ay * ay + az * az)
                    if (magnitude > 16f && !isMoving && !hasWon) {
                        val force = magnitude - 9.81f

                        ballVelX = cos(aimAngle.toDouble()).toFloat() * force * 2.2f
                        ballVelY = sin(aimAngle.toDouble()).toFloat() * force * 2.2f

                        isMoving = true
                        strokeCount++
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(listener, accelSensor, SensorManager.SENSOR_DELAY_GAME)

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            if (canvasWidth > 0f && canvasHeight > 0f) {
                if (!isInitialized) {
                    ballX = canvasWidth / 2f
                    ballY = canvasHeight - 200f
                    isInitialized = true
                }

                if (isMoving && !hasWon) {
                    ballX += ballVelX
                    ballY += ballVelY
                    ballVelX *= 0.95f
                    ballVelY *= 0.95f

                    // Rebotes en paredes
                    if (ballX - ballRadius <= 0f) {
                        ballX = ballRadius
                        ballVelX = -ballVelX * 0.5f
                    }
                    if (ballX + ballRadius >= canvasWidth) {
                        ballX = canvasWidth - ballRadius
                        ballVelX = -ballVelX * 0.5f
                    }
                    if (ballY - ballRadius <= 0f) {
                        ballY = ballRadius
                        ballVelY = -ballVelY * 0.5f
                    }
                    if (ballY + ballRadius >= canvasHeight) {
                        ballY = canvasHeight - ballRadius
                        ballVelY = -ballVelY * 0.5f
                    }

                    // cambio de velocidad
                    val currentSpeed = sqrt(ballVelX * ballVelX + ballVelY * ballVelY)
                    if (currentSpeed < 0.2f) {
                        ballVelX = 0f
                        ballVelY = 0f
                        isMoving = false
                    }

                    // Detectar entrada al Hoyo
                    val holeX = canvasWidth / 2f
                    val holeY = 250f
                    val dx = ballX - holeX
                    val dy = ballY - holeY
                    val distance = sqrt(dx * dx + dy * dy)

                    if (distance < (holeRadius - 10f) && currentSpeed < 8f) {
                        hasWon = true
                        ballX = holeX
                        ballY = holeY
                        ballVelX = 0f
                        ballVelY = 0f
                        isMoving = false
                    }
                }
            }
            delay(16L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            canvasWidth = size.width
            canvasHeight = size.height

            val holeX = size.width / 2f
            val holeY = 250f

            // Campo de Golf
            drawRect(color = Color(0xFF2E7D32), size = size)

            // Hoyo
            drawCircle(color = Color.Black, radius = holeRadius, center = Offset(holeX, holeY))

            if (isInitialized) {
                if (!isMoving && !hasWon) {
                    val lineLength = 130f
                    val endX = ballX + cos(aimAngle.toDouble()).toFloat() * lineLength
                    val endY = ballY + sin(aimAngle.toDouble()).toFloat() * lineLength

                    drawLine(
                        color = Color.Yellow,
                        start = Offset(ballX, ballY),
                        end = Offset(endX, endY),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                }

                // Pelota
                drawCircle(color = Color.White, radius = ballRadius, center = Offset(ballX, ballY))
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "Tiros: $strokeCount",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
            )
        }

        Button(
            onClick = { onBack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .width(110.dp)
                .height(44.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AEFF))
        ) {
            Text(text = stringResource(R.string.home_btn_back), fontSize = 14.sp, color = Color.White)
        }

        if (hasWon) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡HOLE IN ONE! \nIn $strokeCount Shots",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        // restart game Button
                        strokeCount = 0
                        hasWon = false
                        isMoving = false

                        ballVelX = 0f
                        ballVelY = 0f

                        isInitialized = false
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text(
                        text = "Restart",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

    }
}