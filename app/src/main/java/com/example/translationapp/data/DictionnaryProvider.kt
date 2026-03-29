package com.example.translationapp.data

class DictionnaryProvider {

    fun getFoodWords(): List<DictionaryTranslation> {
        return listOf(
            DictionaryTranslation(1, "Apple", "Pomme", "Manzana", 1),
            DictionaryTranslation(2, "Bread", "Pain", "Pan", 1),
            DictionaryTranslation(3, "Cheese", "Fromage", "Queso", 1),
            DictionaryTranslation(4, "Water", "Eau", "Agua", 1),
            DictionaryTranslation(5, "Milk", "Lait", "Leche", 1),
            DictionaryTranslation(6, "Rice", "Riz", "Arroz", 1),
            DictionaryTranslation(7, "Chicken", "Poulet", "Pollo", 1),
            DictionaryTranslation(8, "Soup", "Soupe", "Sopa", 1),
            DictionaryTranslation(9, "Salt", "Sel", "Sal", 1),
            DictionaryTranslation(10, "Sugar", "Sucre", "Azucar", 1),
            DictionaryTranslation(11, "Butter", "Beurre", "Mantequilla", 1),
            DictionaryTranslation(12, "Orange", "Orange", "Naranja", 1),
            DictionaryTranslation(13, "Carrot", "Carotte", "Zanahoria", 1),
            DictionaryTranslation(14, "Potato", "Pomme de terre", "Patata", 1),
            DictionaryTranslation(15, "Tomato", "Tomate", "Tomate", 1),
            DictionaryTranslation(16, "Strawberry", "Fraise", "Fresa", 1),
            DictionaryTranslation(17, "Breakfast", "Petit dejeuner", "Desayuno", 1),
            DictionaryTranslation(18, "Pineapple", "Ananas", "Pina", 1),
            DictionaryTranslation(19, "Vegetable", "Legume", "Verdura", 1),
            DictionaryTranslation(20, "Chocolate", "Chocolat", "Chocolate", 1)
        )
    }

    fun getMiscWords(): List<DictionaryTranslation> {
        return listOf(
            DictionaryTranslation(21, "Red", "Rouge", "Rojo", 2),
            DictionaryTranslation(22, "Happy", "Heureux", "Feliz", 2),
            DictionaryTranslation(23, "Today", "Aujourd'hui", "Hoy", 2),
            DictionaryTranslation(24, "Blue", "Bleu", "Azul", 2),
            DictionaryTranslation(25, "Green", "Vert", "Verde", 2),
            DictionaryTranslation(26, "Sad", "Triste", "Triste", 2),
            DictionaryTranslation(27, "Fast", "Rapide", "Rapido", 2),
            DictionaryTranslation(28, "Slow", "Lent", "Lento", 2),
            DictionaryTranslation(29, "Tomorrow", "Demain", "Manana", 2),
            DictionaryTranslation(30, "Yesterday", "Hier", "Ayer", 2),
            DictionaryTranslation(31, "Always", "Toujours", "Siempre", 2),
            DictionaryTranslation(32, "Never", "Jamais", "Nunca", 2),
            DictionaryTranslation(33, "Beautiful", "Magnifique", "Hermoso", 2),
            DictionaryTranslation(34, "Important", "Important", "Importante", 2),
            DictionaryTranslation(35, "Friendly", "Amical", "Amistoso", 2),
            DictionaryTranslation(36, "Different", "Different", "Diferente", 2),
            DictionaryTranslation(37, "Powerful", "Puissant", "Poderoso", 2),
            DictionaryTranslation(38, "Dangerous", "Dangereux", "Peligroso", 2),
            DictionaryTranslation(39, "Saturday", "Samedi", "Sabado", 2),
            DictionaryTranslation(40, "Holiday", "Vacances", "Vacaciones", 2)
        )
    }

    fun getAnimalWords(): List<DictionaryTranslation> {
        return listOf(
            DictionaryTranslation(41, "Cat", "Chat", "Gato", 3),
            DictionaryTranslation(42, "Dog", "Chien", "Perro", 3),
            DictionaryTranslation(43, "Bird", "Oiseau", "Pajaro", 3),
            DictionaryTranslation(44, "Horse", "Cheval", "Caballo", 3),
            DictionaryTranslation(45, "Cow", "Vache", "Vaca", 3),
            DictionaryTranslation(46, "Fish", "Poisson", "Pez", 3),
            DictionaryTranslation(47, "Mouse", "Souris", "Raton", 3),
            DictionaryTranslation(48, "Rabbit", "Lapin", "Conejo", 3),
            DictionaryTranslation(49, "Duck", "Canard", "Pato", 3),
            DictionaryTranslation(50, "Lion", "Lion", "Leon", 3),
            DictionaryTranslation(51, "Tiger", "Tigre", "Tigre", 3),
            DictionaryTranslation(52, "Monkey", "Singe", "Mono", 3),
            DictionaryTranslation(53, "Elephant", "Elephant", "Elefante", 3),
            DictionaryTranslation(54, "Butterfly", "Papillon", "Mariposa", 3),
            DictionaryTranslation(55, "Crocodile", "Crocodile", "Cocodrilo", 3),
            DictionaryTranslation(56, "Kangaroo", "Kangourou", "Canguro", 3),
            DictionaryTranslation(57, "Dolphin", "Dauphin", "Delfin", 3),
            DictionaryTranslation(58, "Penguin", "Manchot", "Pinguino", 3),
            DictionaryTranslation(59, "Squirrel", "Ecureuil", "Ardilla", 3),
            DictionaryTranslation(60, "Octopus", "Pieuvre", "Pulpo", 3)
        )
    }

    fun getEverydayObjects(): List<DictionaryTranslation> {
        return listOf(
            DictionaryTranslation(61, "Chair", "Chaise", "Silla", 4),
            DictionaryTranslation(62, "Table", "Table", "Mesa", 4),
            DictionaryTranslation(63, "Key", "Cle", "Llave", 4),
            DictionaryTranslation(64, "Phone", "Telephone", "Telefono", 4),
            DictionaryTranslation(65, "Window", "Fenetre", "Ventana", 4),
            DictionaryTranslation(66, "Door", "Porte", "Puerta", 4),
            DictionaryTranslation(67, "Book", "Livre", "Libro", 4),
            DictionaryTranslation(68, "Pen", "Stylo", "Boligrafo", 4),
            DictionaryTranslation(69, "Bag", "Sac", "Bolsa", 4),
            DictionaryTranslation(70, "Clock", "Horloge", "Reloj", 4),
            DictionaryTranslation(71, "Bottle", "Bouteille", "Botella", 4),
            DictionaryTranslation(72, "Computer", "Ordinateur", "Computadora", 4),
            DictionaryTranslation(73, "Pencil", "Crayon", "Lapiz", 4),
            DictionaryTranslation(74, "Notebook", "Cahier", "Cuaderno", 4),
            DictionaryTranslation(75, "Backpack", "Sac a dos", "Mochila", 4),
            DictionaryTranslation(76, "Television", "Televiseur", "Television", 4),
            DictionaryTranslation(77, "Headphones", "Casque audio", "Auriculares", 4),
            DictionaryTranslation(78, "Umbrella", "Parapluie", "Paraguas", 4),
            DictionaryTranslation(79, "Keyboard", "Clavier", "Teclado", 4),
            DictionaryTranslation(80, "Refrigerator", "Refrigerateur", "Refrigerador", 4)
        )
    }

    fun getAllWords(): List<DictionaryTranslation> {
        return getFoodWords() + getMiscWords() + getAnimalWords() + getEverydayObjects()
    }
}
