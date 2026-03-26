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

    fun getFoodWords(): List<DictionnaryTranslation> {
        return listOf(
            DictionnaryTranslation(1, "Apple", "Pomme", 1),
            DictionnaryTranslation(2, "Bread", "Pain", 1),
            DictionnaryTranslation(3, "Cheese", "Fromage", 1),
            DictionnaryTranslation(4, "Water", "Eau", 1),
        )
    }

    fun getMiscWords(): List<DictionnaryTranslation> {
        return listOf(
            DictionnaryTranslation(5, "Red", "Rouge", 2),
            DictionnaryTranslation(6, "Happy", "Heureux", 2),
            DictionnaryTranslation(7, "Today", "Aujourd'hui", 2)
        )
    }

    fun getAnimalWords(): List<DictionnaryTranslation> {
        return listOf(
            DictionnaryTranslation(8, "Cat", "Chat", 3),
            DictionnaryTranslation(9, "Dog", "Chien", 3),
            DictionnaryTranslation(10, "Bird", "Oiseau", 3),
            DictionnaryTranslation(11, "Horse", "Cheval", 3)
        )
    }

    fun getEverydayObjects(): List<DictionnaryTranslation> {
        return listOf(
            DictionnaryTranslation(12, "Chair", "Chaise", 4),
            DictionnaryTranslation(13, "Table", "Table", 4),
            DictionnaryTranslation(14, "Key", "Clé", 4),
            DictionnaryTranslation(15, "Phone", "Téléphone", 4)
        )
    }

    fun getAllWords(): List<DictionnaryTranslation> {
        return getFoodWords() + getMiscWords() + getAnimalWords() + getEverydayObjects()
    }

}