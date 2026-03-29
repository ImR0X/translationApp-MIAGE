package com.example.translationapp.controllers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.translationapp.data.DictionaryTranslation
import com.example.translationapp.data.DictionnaryProvider
import com.example.translationapp.model.AnswerRecord
import com.example.translationapp.model.AppScreen
import com.example.translationapp.model.QuizDifficulty
import com.example.translationapp.model.QuizLanguage
import com.example.translationapp.model.QuizResult
import com.example.translationapp.model.QuizSetup
import com.example.translationapp.model.QuizSetupSaver
import com.example.translationapp.model.QuizStage
import com.example.translationapp.model.QuizTheme
import com.example.translationapp.model.answerLengthFor
import com.example.translationapp.model.textFor
import com.example.translationapp.ui.views.QuizResultView
import com.example.translationapp.ui.views.QuizView
import com.example.translationapp.ui.views.SetupQuizView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val QUIZ_LENGTH = 10
private const val MAX_LIVES_PER_WORD = 3

// Controller principal: il choisit quelle vue afficher et fait le lien entre
// les donnees du modele et les ecrans Compose.
@Composable
fun TranslationAppController(
    dataProvider: DictionnaryProvider = DictionnaryProvider()
) {
    val allWords = remember { dataProvider.getAllWords() }
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.Setup.name) }
    var setup by rememberSaveable(stateSaver = QuizSetupSaver) { mutableStateOf(QuizSetup()) }
    var quizWords by remember { mutableStateOf(generateQuizWords(allWords, setup)) }
    var result by remember { mutableStateOf(QuizResult(emptyList(), 0)) }

    when (AppScreen.valueOf(currentScreen)) {
        AppScreen.Setup -> {
            val availableWords = remember(setup) { buildFilteredWords(allWords, setup) }
            SetupQuizView(
                setup = setup,
                availableWords = availableWords.size,
                onSetupChange = { setup = it },
                onStartQuiz = {
                    quizWords = generateQuizWords(allWords, setup)
                    result = QuizResult(emptyList(), quizWords.size)
                    currentScreen = AppScreen.Quiz.name
                }
            )
        }

        AppScreen.Quiz -> {
            QuizFlowController(
                words = quizWords,
                sourceLanguage = setup.sourceLanguage,
                targetLanguage = setup.targetLanguage,
                onQuizCompleted = { completedAnswers ->
                    result = QuizResult(completedAnswers, quizWords.size)
                    currentScreen = AppScreen.Result.name
                }
            )
        }

        AppScreen.Result -> {
            QuizResultView(
                result = result,
                onBackToSetup = { currentScreen = AppScreen.Setup.name }
            )
        }
    }
}

@Composable
private fun QuizFlowController(
    words: List<DictionaryTranslation>,
    sourceLanguage: QuizLanguage,
    targetLanguage: QuizLanguage,
    onQuizCompleted: (List<AnswerRecord>) -> Unit
) {
    // La cle permet de reinitialiser proprement l'etat sauvegarde quand un
    // nouveau jeu de mots est genere.
    val wordsKey = remember(words) { words.joinToString(separator = "-") { it.id.toString() } }
    var currentIndex by rememberSaveable(wordsKey) { mutableIntStateOf(0) }
    var remainingLives by rememberSaveable(wordsKey) { mutableIntStateOf(MAX_LIVES_PER_WORD) }
    var answer by rememberSaveable(wordsKey) { mutableStateOf("") }
    var stage by rememberSaveable(wordsKey) { mutableStateOf(QuizStage.Answering) }
    var showWrongFeedback by rememberSaveable(wordsKey) { mutableStateOf(false) }
    var isLocked by rememberSaveable(wordsKey) { mutableStateOf(false) }
    val answerHistory = remember(wordsKey) { mutableStateListOf<AnswerRecord>() }
    val coroutineScope = rememberCoroutineScope()

    val currentWord = words.getOrNull(currentIndex)
    val progress = ((currentIndex + if (stage == QuizStage.Answering) 0 else 1).coerceAtMost(words.size)).toFloat() /
        words.size.coerceAtLeast(1).toFloat()

    if (currentWord == null) {
        onQuizCompleted(answerHistory.toList())
        return
    }

    // A chaque nouveau mot on repart avec 3 vies et un champ vide.
    fun moveToNextWord() {
        currentIndex += 1
        remainingLives = MAX_LIVES_PER_WORD
        answer = ""
        stage = QuizStage.Answering
        showWrongFeedback = false
        isLocked = false
    }

    suspend fun validateAnswer() {
        if (isLocked) return
        val normalizedUserAnswer = answer.trim()
        if (normalizedUserAnswer.isEmpty()) return

        isLocked = true
        val isCorrect = normalizedUserAnswer.equals(currentWord.textFor(targetLanguage), ignoreCase = true)

        if (isCorrect) {
            answerHistory.add(AnswerRecord(currentWord, normalizedUserAnswer, true))
            stage = QuizStage.Correct
            isLocked = false
            return
        }

        // En cas d'erreur, le champ passe en rouge pendant 3 secondes. Si les
        // vies tombent a zero, on bascule vers l'ecran de correction du mot.
        showWrongFeedback = true
        remainingLives -= 1

        if (remainingLives <= 0) {
            delay(3_000)
            showWrongFeedback = false
            answerHistory.add(AnswerRecord(currentWord, normalizedUserAnswer, false))
            stage = QuizStage.Failed
            isLocked = false
        } else {
            delay(3_000)
            showWrongFeedback = false
            isLocked = false
        }
    }

    QuizView(
        word = currentWord,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        currentIndex = currentIndex,
        totalWords = words.size,
        progress = progress,
        remainingLives = remainingLives,
        answer = answer,
        stage = stage,
        showWrongFeedback = showWrongFeedback,
        onAnswerChange = {
            if (!isLocked && stage == QuizStage.Answering) {
                answer = it
            }
        },
        onCheckAnswer = {
            coroutineScope.launch { validateAnswer() }
        },
        onPrimaryAction = {
            if (currentIndex == words.lastIndex) {
                onQuizCompleted(answerHistory.toList())
            } else {
                moveToNextWord()
            }
        }
    )
}

private fun generateQuizWords(
    allWords: List<DictionaryTranslation>,
    setup: QuizSetup
): List<DictionaryTranslation> {
    // On filtre d'abord selon les choix de setup, puis on tire 10 mots au hasard.
    return buildFilteredWords(allWords, setup)
        .shuffled()
        .take(QUIZ_LENGTH)
}

private fun buildFilteredWords(
    allWords: List<DictionaryTranslation>,
    setup: QuizSetup
): List<DictionaryTranslation> {
    // La difficulte actuelle est derivee de la longueur du mot attendu dans la
    // langue cible.
    return allWords.filter { word ->
        val themeMatches = setup.theme.themeId == null || word.theme == setup.theme.themeId
        val difficultyMatches = when (setup.difficulty) {
            QuizDifficulty.ALL -> true
            QuizDifficulty.EASY -> word.answerLengthFor(setup.targetLanguage) <= 5
            QuizDifficulty.MEDIUM -> word.answerLengthFor(setup.targetLanguage) in 6..7
            QuizDifficulty.HARD -> word.answerLengthFor(setup.targetLanguage) >= 8
        }
        themeMatches && difficultyMatches
    }
}
