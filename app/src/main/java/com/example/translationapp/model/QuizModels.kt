package com.example.translationapp.model

import androidx.compose.runtime.saveable.Saver
import com.example.translationapp.data.DictionaryTranslation

// Modeles utilises par le flow du quiz. Ils restent independants de l'UI.
enum class AppScreen {
    Setup,
    Quiz,
    Result
}

enum class QuizLanguage(
    val label: String,
    val shortCode: String
) {
    FRENCH("French", "FR"),
    ENGLISH("English", "EN"),
    SPANISH("Spanish", "ES")
}

enum class QuizTheme(
    val label: String,
    val themeId: Int?
) {
    ALL("All Themes", null),
    FOOD("Food", 1),
    MISC("Misc", 2),
    ANIMALS("Animals", 3),
    OBJECTS("Objects", 4)
}

enum class QuizDifficulty(val label: String) {
    ALL("All Levels"),
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}

enum class QuizStage {
    Answering,
    Correct,
    Failed
}

data class QuizSetup(
    val sourceLanguage: QuizLanguage = QuizLanguage.FRENCH,
    val targetLanguage: QuizLanguage = QuizLanguage.ENGLISH,
    val theme: QuizTheme = QuizTheme.ALL,
    val difficulty: QuizDifficulty = QuizDifficulty.ALL
)

// Saver Compose pour conserver le setup lors d'une recreation d'activite.
val QuizSetupSaver = Saver<QuizSetup, List<String>>(
    save = {
        listOf(
            it.sourceLanguage.name,
            it.targetLanguage.name,
            it.theme.name,
            it.difficulty.name
        )
    },
    restore = {
        QuizSetup(
            sourceLanguage = QuizLanguage.valueOf(it[0]),
            targetLanguage = QuizLanguage.valueOf(it[1]),
            theme = QuizTheme.valueOf(it[2]),
            difficulty = QuizDifficulty.valueOf(it[3])
        )
    }
)

data class AnswerRecord(
    val word: DictionaryTranslation,
    val userAnswer: String,
    val isCorrect: Boolean
)

data class QuizResult(
    val answers: List<AnswerRecord>,
    val totalWords: Int
) {
    val score: Int
        get() = answers.count { it.isCorrect }
}

// Ces helpers centralisent les conversions metier sur les mots du dictionnaire.
fun DictionaryTranslation.textFor(language: QuizLanguage): String {
    return when (language) {
        QuizLanguage.FRENCH -> frenchWord
        QuizLanguage.ENGLISH -> englishWord
        QuizLanguage.SPANISH -> spanishWord
    }
}

fun DictionaryTranslation.answerLengthFor(language: QuizLanguage): Int {
    return textFor(language).replace(" ", "").length
}

fun DictionaryTranslation.themeLabel(): String {
    return when (theme) {
        1 -> "FOOD"
        2 -> "MISC"
        3 -> "ANIMALS"
        4 -> "OBJECTS"
        else -> "WORDS"
    }
}
