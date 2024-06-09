import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun oldapp() {
    var textValue by remember { mutableStateOf(TextFieldValue(text = "")) }
    var isProgressing by remember { mutableStateOf(false) }
    var isParsed by remember { mutableStateOf(false) }
    val asyncScope = rememberCoroutineScope()
    val timerScope = rememberCoroutineScope()
    var shownMessage by remember { mutableStateOf(DailyMessage.NONE) }
    val dailyRunner = DailyRunner()
    var timerCounter by remember { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(colorBg)) {
            if (isParsed) {
                if (dailyRunner.isAllRun() && shownMessage == DailyMessage.NONE) {
                    Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Happy ${getCurrentDayStr()}!",
                            fontSize = 40.sp,
                            modifier = Modifier.padding(top = 16.dp),
                            fontFamily = poppins(),
                            color = colorText
                        )

                        Text("Time Rating",
                            fontSize = 32.sp,
                            modifier = Modifier.padding(top = 16.dp),
                            fontFamily = lato(),
                            color = colorText)

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(3f, true)) {
                                Text(text = "Shortest speech:", fontSize = 28.sp, fontFamily = poppins(), color = colorTextGreen)
                                Spacer(modifier = Modifier.height(16.dp))
                                dailyRunner.getMinTimes().forEach {
                                    Text("${it.first}",
                                        fontFamily = lato(),
                                        color = colorTextGreen,
                                        fontSize = 40.sp)
                                    Text("${it.second.fromSecondsToTimeString()}",
                                        fontFamily = lato(),
                                        color = colorTextGreen,
                                        fontSize = 40.sp)
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                            Column(modifier = Modifier.weight(3f, true)) {
                                Text(text = "Longest speech:", fontSize = 28.sp, fontFamily = poppins(), color = colorTextRed)
                                Spacer(modifier = Modifier.height(16.dp))
                                dailyRunner.getMaxTimes().forEach {
                                    Text("${it.first}",
                                        fontFamily = lato(),
                                        color = colorTextRed,
                                        fontSize = 40.sp)
                                    Text("${it.second.fromSecondsToTimeString()}",
                                        fontFamily = lato(),
                                        color = colorTextRed,
                                        fontSize = 40.sp)
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                            Column(modifier = Modifier.weight(3f, true)) {
                                Text(text = "General rating:", fontSize = 24.sp, fontFamily = poppins(), color = colorTextLight)
                                dailyRunner.getSortedTimes().forEachIndexed { i, t ->
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text("${i+1}. ${t.first}",
                                            fontFamily = lato(),
                                            color = colorText,
                                            fontSize = 20.sp)
                                        Spacer(modifier = Modifier.weight(1f, true))
                                        Text("${t.second.fromSecondsToTimeString()}",
                                            fontFamily = lato(),
                                            color = colorText,
                                            fontSize = 20.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (shownMessage == DailyMessage.NONE) {
                            Text(
                                "Click start to randomize message",
                                modifier = Modifier.weight(1f, true).align(Alignment.CenterHorizontally),
                                textAlign = TextAlign.Center,
                                fontFamily = lato(),
                                color = colorText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            GradientButton(text = "Start", modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                                shownMessage = dailyRunner.getNext()
                                timerCounter = 0
                                if(shownMessage == DailyMessage.NONE) isTimerRunning = false
                            })
                        } else {
                            DailyMessageView(shownMessage, modifier = Modifier.fillMaxWidth().weight(1f, true))
                            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                                Image(painter = rememberVectorPainter(Icons.Default.Timer), contentDescription = "", colorFilter = ColorFilter.tint(colorTextRed), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = timerCounter.fromSecondsToTimeString(), fontFamily = poppins(), color = colorTextRed, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center) {
                                GradientButton(text = "Skip", onClick = {
                                    shownMessage = dailyRunner.getNext(true)
                                    timerCounter = 0
                                    if(shownMessage == DailyMessage.NONE) isTimerRunning = false
                                })
                                Spacer(modifier = Modifier.width(16.dp))
                                GradientButton(text = "Not attended", onClick = {
                                    shownMessage = dailyRunner.notAttend()
                                    timerCounter = 0
                                    if(shownMessage == DailyMessage.NONE) isTimerRunning = false
                                })
                                Spacer(modifier = Modifier.width(16.dp))
                                GradientButton(text = "Next", onClick = {
                                    dailyRunner.storeTimeForMessage(shownMessage, timerCounter)
                                    shownMessage = dailyRunner.getNext()
                                    timerCounter = 0
                                    if(shownMessage == DailyMessage.NONE) isTimerRunning = false
                                })
                            }
                        }
                    }
                }
            } else if (isProgressing) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = colorButtonBottom)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Progressing", fontFamily = lato(), color = colorText)
                }
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                    Text("Copy and paste daily message for today in the field below", fontFamily = lato(), color = colorText)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(textValue, onValueChange = {
                        textValue = it
                    }, modifier = Modifier.fillMaxWidth().weight(1f, fill = true), colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        backgroundColor = colorBgInput
                    ), textStyle = LocalTextStyle.current.copy(
                        fontFamily = lato()
                    ))
                    Spacer(modifier = Modifier.height(8.dp))
                    GradientButton(text = "Do Progressing", modifier = Modifier.wrapContentSize().align(Alignment.CenterHorizontally),
                        onClick = {
                        isProgressing = true
                        asyncScope.launch(Dispatchers.Default) {
                            dailyRunner.submitRawText(textValue.text)
                            delay(5000)
                            isTimerRunning = true
                            timerScope.launch(Dispatchers.Default) {
                                while (isTimerRunning) {
                                    delay(1000)
                                    timerCounter += 1
                                }
                            }
                            isProgressing = false
                            isParsed = true
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    gradient : Brush = Brush.linearGradient(listOf(colorButtonTop, colorButtonBottom)),
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    Button(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = { onClick() },
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation = 4.dp, RoundedCornerShape(6.dp))
                .background(gradient, RoundedCornerShape(6.dp))
                .then(modifier)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = text, fontFamily = lato(), color = colorText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

fun SnapshotStateList<DailyMessage>.peekRandom() : DailyMessage {
    if(isEmpty()) return DailyMessage.NONE
    val index = (0 until size).random()
    val message = removeAt(index)
    return message
}

@Composable
fun DailyMessageView(message: DailyMessage, modifier: Modifier = Modifier) {
    Column(modifier = modifier.background(colorBgInput, shape = RoundedCornerShape(6.dp)).padding(24.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(message.userName, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 40.sp, fontFamily = poppins(), color = Color.White)
        Spacer(modifier = Modifier.height(24.dp))
        Column(modifier = Modifier.fillMaxWidth().weight(1f, true)) {
            Text("Which task did you successfully deliver yesterday?", fontWeight = FontWeight.Bold, fontFamily = poppins(), color = colorTextLight)
            Spacer(modifier = Modifier.height(8.dp))
            Text(message.firstAnswer, fontSize = 24.sp, fontFamily = lato(), color = colorText)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Column(modifier = Modifier.fillMaxWidth().weight(1f, true)) {
            Text(
                "What are your top 3 tasks you will work on today, which one will you deliver?",
                fontWeight = FontWeight.Bold,
                fontFamily = poppins(),
                color = colorTextLight
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(message.secondAnswer, fontSize = 24.sp, fontFamily = lato(), color = colorText)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
