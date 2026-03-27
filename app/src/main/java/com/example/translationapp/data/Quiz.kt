package com.example.translationapp.data

import com.example.translationapp.controllers.GenerateWordSetController

class Quiz (val userAnswer:ArrayList<String>, val score:Int ,isRandomThemed: Boolean = true, theme: Int) {

    val generateWordSetController = GenerateWordSetController();
    // Recupere des mots au pif sans theme
    var words = if(isRandomThemed){
        generateWordSetController.getRandomSet(5)
    }else{
        generateWordSetController.getRandomSetByTheme(theme,5)
    };


    fun startQuiz(){
        // Envoie les words en boucle sur la liste de mots

        // Attend sur un listener que l'utilisateur réponde pour ensuite appeler la fonction writeAnswer()

        // passe chaque membre de la liste userAnswer à la fonction check et renvoie le résultat => Changement de page
    }

    fun writeAnswer(answer: String){
        userAnswer.add(answer)
    }


    fun checkAnswer(userAnswer: String, currentWordCorrectAnswer: DictionaryTranslation, langageSelected: String): Boolean{
        when(langageSelected){
            "FR" -> return userAnswer == currentWordCorrectAnswer.frenchWord;
            "EN" -> return userAnswer == currentWordCorrectAnswer.englishWord;
            else -> {}
        }
        return false;
    }

}