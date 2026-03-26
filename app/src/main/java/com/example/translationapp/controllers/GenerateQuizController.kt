package com.example.translationapp.controllers

import com.example.translationapp.data.DictionnaryTranslation

class QuizController {

    fun startQuiz(){

    }

    fun checkAnswer(userAnswer: String, currentWordCorrectAnswer: DictionnaryTranslation, langageSelected: String): Boolean{
        when(langageSelected){
            "FR" -> return userAnswer == currentWordCorrectAnswer.frenchWord;
            "EN" -> return userAnswer == currentWordCorrectAnswer.englishWord;
            else -> {}
        }
        return false;
    }
}