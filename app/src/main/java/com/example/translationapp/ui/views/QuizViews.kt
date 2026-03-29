package com.example.translationapp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.translationapp.R
import com.example.translationapp.data.DictionaryTranslation
import com.example.translationapp.model.QuizDifficulty
import com.example.translationapp.model.QuizLanguage
import com.example.translationapp.model.QuizResult
import com.example.translationapp.model.QuizSetup
import com.example.translationapp.model.QuizStage
import com.example.translationapp.model.QuizTheme
import com.example.translationapp.model.themeLabel
import com.example.translationapp.model.textFor
import com.example.translationapp.ui.AccentBlue
import com.example.translationapp.ui.AccentGreen
import com.example.translationapp.ui.AccentGreenSoft
import com.example.translationapp.ui.AccentRed
import com.example.translationapp.ui.AccentRedSoft
import com.example.translationapp.ui.CardBackground
import com.example.translationapp.ui.CardBorder
import com.example.translationapp.ui.FieldBackground
import com.example.translationapp.ui.LabelColor
import com.example.translationapp.ui.ProgressTrack
import com.example.translationapp.ui.ScreenBackground
import com.example.translationapp.ui.TextColor
import com.example.translationapp.ui.theme.TranslationAppTheme

// Vues Compose pures: elles affichent un etat et emettent des callbacks, sans
// porter la logique metier du quiz.
@Composable
fun SetupQuizView(
    setup: QuizSetup,
    availableWords: Int,
    onSetupChange: (QuizSetup) -> Unit,
    onStartQuiz: () -> Unit
) {
    Scaffold(
        containerColor = ScreenBackground,
        bottomBar = {
            BottomActionArea(
                backgroundColor = CardBackground,
                buttonColor = AccentGreen,
                buttonLabel = "START QUIZ",
                enabled = availableWords > 0,
                onClick = onStartQuiz
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SetupHeader()
            SetupCard(title = "DIRECTION") {
                DirectionSelector(
                    source = setup.sourceLanguage,
                    target = setup.targetLanguage,
                    onSourceSelected = {
                        val nextTarget = if (it == setup.targetLanguage) setup.sourceLanguage else setup.targetLanguage
                        onSetupChange(setup.copy(sourceLanguage = it, targetLanguage = nextTarget))
                    },
                    onTargetSelected = {
                        val nextSource = if (it == setup.sourceLanguage) setup.targetLanguage else setup.sourceLanguage
                        onSetupChange(setup.copy(sourceLanguage = nextSource, targetLanguage = it))
                    }
                )
            }
            SetupCard(title = "FILTERS") {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    DropdownSelector(
                        label = "Theme",
                        options = QuizTheme.entries,
                        selectedOption = setup.theme,
                        optionLabel = { it.label },
                        onSelected = { onSetupChange(setup.copy(theme = it)) }
                    )
                    DropdownSelector(
                        label = "Difficulty",
                        options = QuizDifficulty.entries,
                        selectedOption = setup.difficulty,
                        optionLabel = { it.label },
                        onSelected = { onSetupChange(setup.copy(difficulty = it)) }
                    )
                }
            }
            Text(
                text = "$availableWords WORDS AVAILABLE",
                color = LabelColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            if (availableWords == 0) {
                Text(
                    text = "Aucun mot ne correspond a cette combinaison. Choisis un autre filtre.",
                    color = AccentRed,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun QuizView(
    word: DictionaryTranslation,
    sourceLanguage: QuizLanguage,
    targetLanguage: QuizLanguage,
    currentIndex: Int,
    totalWords: Int,
    progress: Float,
    remainingLives: Int,
    answer: String,
    stage: QuizStage,
    showWrongFeedback: Boolean,
    onAnswerChange: (String) -> Unit,
    onCheckAnswer: () -> Unit,
    onPrimaryAction: () -> Unit
) {
    // La vue ne calcule pas le resultat: elle ne fait que refleter l'etat
    // courant fourni par le controller.
    val bottomPanelColor = when (stage) {
        QuizStage.Answering -> CardBackground
        QuizStage.Correct -> AccentGreenSoft
        QuizStage.Failed -> AccentRedSoft
    }
    val buttonColor = when (stage) {
        QuizStage.Answering -> if (answer.isNotBlank()) AccentGreen else LabelColor
        QuizStage.Correct -> AccentGreen
        QuizStage.Failed -> AccentRed
    }

    Scaffold(
        containerColor = ScreenBackground,
        bottomBar = {
            BottomActionArea(
                backgroundColor = bottomPanelColor,
                buttonColor = buttonColor,
                buttonLabel = if (stage == QuizStage.Answering) "CHECK ANSWER" else "CONTINUE",
                enabled = if (stage == QuizStage.Answering) answer.isNotBlank() else true,
                onClick = if (stage == QuizStage.Answering) onCheckAnswer else onPrimaryAction
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            QuizHeader(progress = progress, remainingLives = remainingLives)
            Spacer(modifier = Modifier.height(36.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Translate this word",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    modifier = Modifier.weight(1f)
                )
                ThemeBadge(label = word.themeLabel())
            }
            WordCard(word = word.textFor(sourceLanguage))
            AnswerField(
                value = answer,
                placeholder = "Type in ${targetLanguage.shortCode}...",
                isError = showWrongFeedback,
                enabled = stage == QuizStage.Answering,
                onValueChange = onAnswerChange
            )
            when (stage) {
                QuizStage.Answering -> Text(
                    text = "Word ${currentIndex + 1} of $totalWords",
                    color = LabelColor,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                QuizStage.Correct -> Text(
                    text = "Awesome!",
                    color = AccentGreen,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                QuizStage.Failed -> Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Correct answer was:",
                        color = AccentRed,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = word.textFor(targetLanguage),
                        color = TextColor,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun QuizResultView(
    result: QuizResult,
    onBackToSetup: () -> Unit
) {
    // Le message reste un simple retour visuel construit a partir du score.
    val progressMessage = when {
        result.score >= result.totalWords * 0.8f -> "You're making great progress."
        result.score >= result.totalWords * 0.5f -> "Nice work. Keep going."
        else -> "Good start. Practice makes progress."
    }

    Scaffold(
        containerColor = ScreenBackground,
        bottomBar = {
            BottomActionArea(
                backgroundColor = CardBackground,
                buttonColor = ProgressTrack,
                buttonLabel = "BACK TO SETUP",
                enabled = true,
                onClick = onBackToSetup
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "TODAY'S SESSION",
                color = LabelColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "${result.score}/${result.totalWords}",
                color = TextColor,
                fontSize = 88.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = progressMessage,
                color = LabelColor,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            SessionChart(score = result.score, total = result.totalWords)
            Spacer(modifier = Modifier.height(140.dp))
        }
    }
}

@Composable
private fun SetupHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = null,
                tint = AccentBlue,
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = "Setup Quiz",
                color = TextColor,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Surface(color = ProgressTrack, shape = CircleShape) {
            Box(modifier = Modifier.size(54.dp), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_account_box),
                    contentDescription = null,
                    tint = LabelColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun SetupCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = title,
                color = LabelColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
private fun DirectionSelector(
    source: QuizLanguage,
    target: QuizLanguage,
    onSourceSelected: (QuizLanguage) -> Unit,
    onTargetSelected: (QuizLanguage) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DropdownChip(
            modifier = Modifier.weight(1f),
            options = QuizLanguage.entries,
            selectedOption = source,
            optionLabel = { "${it.label} ${it.shortCode}" },
            onSelected = onSourceSelected
        )
        Text(
            text = "->",
            color = LabelColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        DropdownChip(
            modifier = Modifier.weight(1f),
            options = QuizLanguage.entries,
            selectedOption = target,
            optionLabel = { "${it.label} ${it.shortCode}" },
            onSelected = onTargetSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selectedOption: T,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Champ readonly + menu: la selection remonte ensuite au controller.
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, color = TextColor, style = MaterialTheme.typography.titleMedium)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = optionLabel(selectedOption),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth(),
                readOnly = true,
                shape = RoundedCornerShape(20.dp),
                trailingIcon = { TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FieldBackground,
                    unfocusedContainerColor = FieldBackground,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(optionLabel(option)) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownChip(
    modifier: Modifier = Modifier,
    options: List<T>,
    selectedOption: T,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = optionLabel(selectedOption),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = { TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FieldBackground,
                unfocusedContainerColor = FieldBackground,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun QuizHeader(
    progress: Float,
    remainingLives: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(14.dp),
            color = AccentGreen,
            trackColor = ProgressTrack
        )
        Spacer(modifier = Modifier.width(18.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = "Lives",
                tint = AccentRed,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = remainingLives.toString(),
                color = AccentRed,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ThemeBadge(label: String) {
    Surface(shape = RoundedCornerShape(999.dp), color = ProgressTrack) {
        Text(
            text = label,
            color = LabelColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun WordCard(word: String) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = word,
                color = TextColor,
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun AnswerField(
    value: String,
    placeholder: String,
    isError: Boolean,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    // Le controleur pilote l'etat d'erreur, la vue se contente d'appliquer le style.
    val containerColor = if (isError) AccentRedSoft else FieldBackground
    val borderColor = if (isError) AccentRed else Color.Transparent

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = LabelColor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        textStyle = MaterialTheme.typography.headlineSmall.copy(
            color = TextColor,
            fontWeight = FontWeight.SemiBold
        ),
        shape = RoundedCornerShape(28.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            disabledBorderColor = borderColor,
            cursorColor = TextColor
        )
    )
}

@Composable
private fun BottomActionArea(
    backgroundColor: Color,
    buttonColor: Color,
    buttonLabel: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(color = backgroundColor, shadowElevation = 8.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .imePadding()
        ) {
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    disabledContainerColor = LabelColor.copy(alpha = 0.45f)
                )
            ) {
                Text(
                    text = buttonLabel,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun SessionChart(
    score: Int,
    total: Int
) {
    val labels = listOf("Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val baseline = (score.toFloat() / total.coerceAtLeast(1)).coerceIn(0.1f, 1f)
    val points = listOf(
        baseline * 0.55f,
        baseline * 0.62f,
        baseline * 0.57f,
        baseline * 0.72f,
        baseline * 0.74f,
        baseline * 0.98f
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val stepX = size.width / (points.size - 1).coerceAtLeast(1)
                val path = Path()

                points.forEachIndexed { index, value ->
                    val x = stepX * index
                    val y = size.height - (size.height * value)
                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = Color(0xFF9BF1D0),
                    style = Stroke(width = 8f, cap = StrokeCap.Round)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                labels.forEach { label ->
                    Text(
                        text = label,
                        color = LabelColor,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SetupPreview() {
    TranslationAppTheme(dynamicColor = false) {
        SetupQuizView(
            setup = QuizSetup(),
            availableWords = 10,
            onSetupChange = {},
            onStartQuiz = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun QuizPreview() {
    TranslationAppTheme(dynamicColor = false) {
        QuizView(
            word = DictionaryTranslation(8, "bird", "oiseau", "pajaro", 3),
            sourceLanguage = QuizLanguage.FRENCH,
            targetLanguage = QuizLanguage.ENGLISH,
            currentIndex = 2,
            totalWords = 10,
            progress = 0.2f,
            remainingLives = 3,
            answer = "",
            stage = QuizStage.Answering,
            showWrongFeedback = false,
            onAnswerChange = {},
            onCheckAnswer = {},
            onPrimaryAction = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ResultPreview() {
    TranslationAppTheme(dynamicColor = false) {
        QuizResultView(
            result = QuizResult(
                answers = emptyList(),
                totalWords = 10
            ),
            onBackToSetup = {}
        )
    }
}
