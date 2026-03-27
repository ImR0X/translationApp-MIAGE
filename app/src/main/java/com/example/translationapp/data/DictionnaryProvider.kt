package com.example.translationapp.data

/**
 * @author{NOTO BAPTISTE}
 *
 * La classe DictionnaryTranslationProvider permet de fournir différentes références
 * de mots en anglais et français avec chacun des thèmes et un id.
 *
 * Le choix a été fait de traiter ici la création de data par simplicité, mais la solution d'un appel API vers
 * un serveur distant restait envisageable.
 *
 * A NOTER QUE CETTE CLASSE NE TRAITE !! AUCUNE LOGIQUE !! (cf [com.example.translationapp.controllers.GenerateWordSetController] )
 *
 * Ne pas oublier que évidemment il faut l'instancier avant de l'injecter :)
 */
class DictionnaryProvider {

    fun getFoodWords(): List<DictionaryTranslation> {
        return listOf(
            DictionaryTranslation(1, "Apple", "Pomme", 1),
            DictionaryTranslation(2, "Bread", "Pain", 1),
            DictionaryTranslation(3, "Cheese", "Fromage", 1),
            DictionaryTranslation(4, "Water", "Eau", 1),
        )
    }

    fun getMiscWords(): List<DictionaryTranslation> {
        return listOf(
            DictionaryTranslation(5, "Red", "Rouge", 2),
            DictionaryTranslation(6, "Happy", "Heureux", 2),
            DictionaryTranslation(7, "Today", "Aujourd'hui", 2)
        )
    }

    fun getAnimalWords(): List<DictionaryTranslation> {
        return listOf(
            DictionaryTranslation(8, "Cat", "Chat", 3),
            DictionaryTranslation(9, "Dog", "Chien", 3),
            DictionaryTranslation(10, "Bird", "Oiseau", 3),
            DictionaryTranslation(11, "Horse", "Cheval", 3)
        )
    }

    fun getEverydayObjects(): List<DictionaryTranslation> {
        return listOf(
            DictionaryTranslation(12, "Chair", "Chaise", 4),
            DictionaryTranslation(13, "Table", "Table", 4),
            DictionaryTranslation(14, "Key", "Clé", 4),
            DictionaryTranslation(15, "Phone", "Téléphone", 4)
        )
    }

    fun getAllWords(): List<DictionaryTranslation> {
        return getFoodWords() + getMiscWords() + getAnimalWords() + getEverydayObjects()
    }

}