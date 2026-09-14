package com.example.tallerpp.composables
import kotlinx.coroutines.delay
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
    onBack: () -> Unit,
    onGameWon: (Int) -> Unit
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
    var showHoleInfo by remember {
        mutableStateOf(true)
    }

    val ballRadius = 30f
    val holeRadius = 50f
    LaunchedEffect(Unit) {
        delay(1000)
        showHoleInfo = false
    }

    DisposableEffect(sensorManager) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val ax = event.values[0]
                    val ay = event.values[1]
                    val az = event.values[2]

                    if (!isMoving && !hasWon) {
                        val tiltMagnitude = sqrt(ax * ax + ay * ay)
                        if (tiltMagnitude > 1.0f) {
                            aimAngle = atan2(ay.toDouble(), -ax.toDouble()).toFloat()
                        }
                    }

                    val magnitude = sqrt(ax * ax + ay * ay + az * az)
                    if (magnitude > 15f && !isMoving && !hasWon) {
                        val force = magnitude - 9.81f

                        ballVelX = cos(aimAngle.toDouble()).toFloat() * force * 3.2f
                        ballVelY = sin(aimAngle.toDouble()).toFloat() * force * 3.2f

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

                    // bounce off walls
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

                    // Bounce off tree 1
                    val tree1X = 115f
                    val tree1Y = 400f
                    val treeRadius = 60f

                    val dx1 = ballX - tree1X
                    val dy1 = ballY - tree1Y
                    val distance1 = sqrt(dx1 * dx1 + dy1 * dy1)

                    if (distance1 < ballRadius + treeRadius) {
                        ballVelX = -ballVelX
                        ballVelY = -ballVelY
                    }

                    // Bounce off tree 2
                    val tree2X = canvasWidth - 115f
                    val tree2Y = 670f

                    val dx2 = ballX - tree2X
                    val dy2 = ballY - tree2Y
                    val distance2 = sqrt(dx2 * dx2 + dy2 * dy2)

                    if (distance2 < ballRadius + treeRadius) {
                        ballVelX = -ballVelX
                        ballVelY = -ballVelY
                    }

                    // Bounce off rock
                    val rockX = canvasWidth / 2f
                    val rockY = canvasHeight * 0.65f
                    val rockRadius = 80f

                    val dxRock = ballX - rockX
                    val dyRock = ballY - rockY
                    val distanceRock = sqrt(dxRock * dxRock + dyRock * dyRock)

                    if (distanceRock < ballRadius + rockRadius) {
                        ballVelX = -ballVelX
                        ballVelY = -ballVelY
                    }

                    // Slow down in Lake 1
                    val lake1X = 140f
                    val lake1Y = canvasHeight * 0.40f + 50f
                    val lake1Width = 100f
                    val lake1Height = 50f

                    val lake1Dx = (ballX - lake1X) / lake1Width
                    val lake1Dy = (ballY - lake1Y) / lake1Height

                    if (lake1Dx * lake1Dx + lake1Dy * lake1Dy < 1f) {
                        ballVelX *= 0.7f
                        ballVelY *= 0.7f
                    }

                    // Slow down in sand
                    val sandX = canvasWidth / 2f
                    val sandY = canvasHeight * 0.92f + 100f
                    val sandWidth = 400f
                    val sandHeight = 100f

                    val sandDx = (ballX - sandX) / sandWidth
                    val sandDy = (ballY - sandY) / sandHeight

                    if (sandDx * sandDx + sandDy * sandDy < 1f) {
                        ballVelX *= 0.7f
                        ballVelY *= 0.7f
                    }

                    // speed change
                    val currentSpeed = sqrt(ballVelX * ballVelX + ballVelY * ballVelY)
                    if (currentSpeed < 0.2f) {
                        ballVelX = 0f
                        ballVelY = 0f
                        isMoving = false
                    }

                    // Detect the entrance to the hole
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

                        onGameWon(strokeCount)
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



            // Golf course
            drawRect(color = Color(0xFF2E7D32), size = size)

            // Hole
            drawCircle(color = Color.Black, radius = holeRadius, center = Offset(holeX, holeY))


            // Flag pole
            drawLine(
                color = Color.White,
                start = Offset(holeX, holeY),
                end = Offset(holeX, holeY - 120f),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )

            // Flag
            val flagPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(holeX, holeY - 120f)
                lineTo(holeX + 70f, holeY - 95f)
                lineTo(holeX, holeY - 70f)
                close()
            }

            drawPath(
                path = flagPath,
                color = Color.Red
            )
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

                // p stone
                drawCircle(
                    color = Color(0xFF757575),
                    radius = 80f,
                    center = Offset(size.width / 2f, size.height * 0.65f
                    )
                )

                // Lake
                drawOval(
                    color = Color(0xFF2196F3),
                    topLeft = Offset(40f, size.height * 0.40f),
                    size = androidx.compose.ui.geometry.Size(220f, 100f)
                )


                drawOval(
                    color = Color(0xFFD7B98E),
                    topLeft = Offset((size.width - 800f) / 2f, size.height * 0.92f
                    ),
                    size = androidx.compose.ui.geometry.Size(800f, 200f)
                )


                // tree 1
                drawRect(
                    color = Color(0xFF795548),
                    topLeft = Offset(100f, 430f),
                    size = androidx.compose.ui.geometry.Size(30f, 100f)
                )

                drawCircle(
                    color = Color(0xFF1B5E20),
                    radius = 60f,
                    center = Offset(115f, 400f)
                )

                // tree 2
                drawRect(
                    color = Color(0xFF795548),
                    topLeft = Offset(size.width - 130f, 700f),
                    size = androidx.compose.ui.geometry.Size(30f, 100f)
                )

                drawCircle(
                    color = Color(0xFF1B5E20),
                    radius = 60f,
                    center = Offset(size.width - 115f, 670f)
                )

                // ball
                drawCircle(color = Color.White, radius = ballRadius, center = Offset(ballX, ballY))
            }
        }


    if (showHoleInfo) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HOLE 1",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Dangerous Forest",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
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
                text = "Shots: $strokeCount",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
            )
        }

        //Button reset
        Button(
            onClick = {
                strokeCount = 0
                hasWon = false
                isMoving = false

                ballVelX = 0f
                ballVelY = 0f

                isInitialized = false
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 75.dp, end = 16.dp)
                .height(40.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "RESTART",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
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
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(
                text = stringResource(R.string.home_btn_back),
                fontSize = 14.sp,
                color = Color.White
            )
        }
        if (hasWon) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.75f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 40.dp,
                            vertical = 35.dp
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = when (strokeCount) {
                                1 -> "HOLE IN ONE!"
                                2 -> "EAGLE!"
                                3 -> "BIRDIE!"
                                4 -> "PAR!"
                                5 -> "BOGEY!"
                                6 -> "DOUBLE BOGEY!"
                                else -> "TRIPLE BOGEY!"
                            },
                            color = Color(0xFFFFD54F),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "$strokeCount Shots",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = {

                                strokeCount = 0
                                hasWon = false
                                isMoving = false

                                ballVelX = 0f
                                ballVelY = 0f

                                isInitialized = false
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1565C0)
                            )
                        ) {
                            Text(
                                text = "PLAY AGAIN",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                onBack()
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {
                            Text(
                                text = "BACK TO HOME",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
        }

    }
