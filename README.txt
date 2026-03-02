src/main/java/com/polydeck/engine/
│
├── MagicObjDB_The_Poly_Deck_Engine.java  <-- Tu clase Main (Punto de entrada y orquestación)
│
├── model/                                <-- Entidades JPA (El Dominio)
│   ├── Carta.java                        (Clase abstracta base)
│   ├── Criatura.java                     (Especialización)
│   ├── Terra.java                        (Especialización)
│   ├── Encanteri.java                    (Especialización)
│   ├── CostMana.java                     (Componente Incrustado)
│   ├── Mazo.java                         (Agregador polimórfico)
│   ├── Jugador.java                      (Propietario)
│   └── enums/
│       ├── Raresa.java                   (COMUNA, INFREQÜENT, RARA, MITICA)
│       └── Color.java                    (Para las tierras: VERMELL, BLAU, etc.)
│
├── manager/                              <-- Lógica de negocio y persistencia
│   └── GestorCartes.java                 (Maneja el EntityManager y las consultas)
│
└── utils/                                <-- Utilidades auxiliares
    └── LectorFitxers.java                (Para parsear 'cartes.txt')

src/main/resources/
│
├── META-INF/
│   └── persistence.xml                   (Configuración de ObjectDB/JPA)
│
└── data/
    └── cartes.txt                        (Fichero de carga inicial de datos)