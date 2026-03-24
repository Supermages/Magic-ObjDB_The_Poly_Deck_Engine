Estructura del projecte:

src/main/java/com/polydeck/engine/
│
├── MagicObjDB_The_Poly_Deck_Engine.java  <-- Classe main (Punt d'entrada i orquestració)
│
├── model/                                <-- Entitats JPA (El Domini)
│   ├── Carta.java                        (Classe abstracta base)
│   ├── Criatura.java                     (Especialització)
│   ├── Terra.java                        (Especialització)
│   ├── Encanteri.java                    (Especialització)
│   ├── CostMana.java                     (Component Incrustat)
│   ├── Mazo.java                         (Agregador polimòrfic)
│   ├── Jugador.java                      (Propietari)
│   └── enums/
│       ├── Raresa.java                   (COMUNA, INFREQÜENT, RARA, MÍTICA)
│       └── Color.java                    (Per a les terres: VERMELL, BLAU, etc.)
│
├── manager/                              <-- Lògica de negoci i persistència
│   └── GestorCartes.java                 (Maneja l'EntityManager i les consultes)
│
└── utils/                                <-- Utilitats auxiliars
    └── LectorFitxers.java                (Per a parsejar 'cartes.txt')

src/main/resources/
│
├── META-INF/
│   └── persistence.xml                   (Configuració d'ObjectDB/JPA)
│
└── data/
    └── cartes.txt                        (Fitxer de càrrega inicial de dades)

> La seguent classe es el diagrama, des de github es pot visualitzar directament, si no hi ha una imatge en assets/Diagrama.png
```mermaid
classDiagram
    class Carta {
        <<abstract>>
        +Long id
        +String nom
        +String descripcio
        +Raresa raresa
        +String edicioDefecte
    }
    class Criatura {
        +CostMana costMana
        +int poder
        +int resistencia
        +String habilitatEspecial
        +boolean potRegenerar
    }
    class Terra {
        +CostMana costMana
        +Color colorMana
        +boolean entraGirada
    }
    class Encanteri {
        +CostMana costMana
        +String efecte
        +boolean esAura
    }
    class CostMana {
        +int blau
        +int negre
        +int vermell
        +int verd
        +int blanc
        +int incolor
    }
    class Mazo {
        +Long id
        +String nom
        +List~Carta~ cartes
    }
    class Jugador {
        +Long id
        +String nom
        +List~Mazo~ mazos
    }
    class Raresa {
        <<enumeration>>
        COMUNA
        INFREQÜENT
        RARA
        MITICA
    }
    class Color {
        <<enumeration>>
        VERMELL
        BLAU
        VERD
        NEGRE
        BLANC
        INCOLOR
    }

    Carta <|-- Criatura : Hereta
    Carta <|-- Terra : Hereta
    Carta <|-- Encanteri : Hereta
    
    Carta --> CostMana : Utilitza
    Carta --> Raresa : Té
    Terra --> Color : Té
    
    Jugador "1" *-- "1..*" Mazo : Té
    Mazo "1" o-- "*" Carta : Conté
```