package com.example.translationapp.controllers

import com.example.translationapp.data.DictionnaryProvider
import com.example.translationapp.data.DictionnaryTranslation

/**
 * @author{NOTO BAPTISTE}
 *
 * Cette classe permet de traiter la *LOGIQUE* et de générer des listes aléatoires de mots.
 *
 * @constructor : Prends en injection le DictionnaryProvider qui fournit les données.
 *  */
class GenerateWordSetController(private val dataProvider: DictionnaryProvider = DictionnaryProvider()) {

    /**
     * @return List<DictionnaryTranslation>
     *     Retourne une liste aléatoire de mots
     * @param size: Int qui défini la prise de 'n' éléments
     */
    fun getRandomSet(size: Int): List<DictionnaryTranslation> {
        return dataProvider.getAllWords()
            .shuffled()
            .take(size)
    }

    /**
     * @return List<DictionnaryTranslation>
     *     Retourne une liste aléatoire de mots filtrés par thèmes
     * @param size: Int qui défini la prise de 'n' éléments
     */
    fun getRandomSetByTheme(themeId: Int, size: Int): List<DictionnaryTranslation> {
        return dataProvider.getAllWords()
            .filter { mot -> mot.theme == themeId } // Filtre par l'ID du thème
            .shuffled()
            .take(size)
    }
}