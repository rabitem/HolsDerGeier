package de.rabitem.main.player.instances;

import java.util.ArrayList;
import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;

class Cheyenne extends Player {
    private ArrayList<Integer> myCards = new ArrayList<Integer>();
    private ArrayList<Integer> enemyCards = new ArrayList<Integer>();
    private ArrayList<Integer> gameCards = new ArrayList<Integer>();
    private ArrayList<ArrayList<Integer>> enemyStrategy = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> Spiele = new ArrayList<Integer>();
    private ArrayList<Integer> Spiele2 = new ArrayList<Integer>();
    private int lastEnemyMove = -99;

    private int countRounds;
    private int countGames = 0;
    private int valueNextCard = -99;
    boolean strategy = true;

    // Konstruktor definieren --> Default-Kontruktor steht nicht mehr zur Verfügung
    public Cheyenne() {
        super("Che"); /*
         * expliziter Aufruf des Konstruktors der Vaterklasse, da kein default
         * Konstruktor für einen impliziten Aufruf zur Verfügung steht
         */
        customResets();
        System.out.println("Ich werde gewinnen!!");
    }

    // überladener Konstruktur
    public Cheyenne(String playerinstance) {
        /*
         * Aufruf des Konstruktors der eigenen Klasse ohne Paramter
         */
        this();
        System.out.println("The Winner is: " + playerinstance);// stimmt natürlich nicht, nur eine Standrad-Aussage
    }

    @Override
    public void customResets() {
        // zurücksetzen aller Nicht-statischen Attribute
        countRounds = 0;
        myCards.clear();
        enemyCards.clear();
        gameCards.clear();

        // Befüllen der Karten Arrays
        for (int i = 1; i < 16; i++) {
            myCards.add(i);
            enemyCards.add(i);
        }
        for (int i = -5; i < 11; i++) {
            if (i == 0)
                continue;
            gameCards.add(i);
        }
    }

    // überschreiben der Methode für die nächste Karte
    @Override
    public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
        // Rundenanzahl hochzählen

        countRounds++;
        // Anzahl der Spiele hochzählen
        if (countRounds % 15 == 1) {
            countGames++;
        }
        /*
         * ab der zweiten Runde die letzte Karte des gegners zuweisen und aus seinen
         * Karten entfernen
         */
        if (countRounds > 1) {
            lastEnemyMove = getLastEnemyCard();
            enemyCards.remove(enemyCards.indexOf(lastEnemyMove));
        }
        // in den ersten 3 Spielen wird versucht die Strategie des Gegners
        // herauszufinden
        if (countGames < 4) {
            getStrategy(pointCardValue, lastEnemyMove);
        }
        /*
         * im ersten+zweiten Spiel wird eine Strategie,die unabhängig von der Stratgeie
         * des Gegners ist gespielt, danach wird entschieden, ob eine auf den gegner
         * angepasste Strategie gespielt werden kann
         */
        valueNextCard = (countGames < 3) ? strategyOne(pointCardValue)
                : ((strategy) ? strategyDynamic(pointCardValue) : strategyOne(pointCardValue));

        // gespielte karte aus meinen Karten entfernen
        myCards.remove(myCards.indexOf(valueNextCard));
        // Punkte Karten aus den Stapel Karten entfernen
        gameCards.remove(gameCards.indexOf(pointCardValue));
        return new PlayerCard(valueNextCard);
    }

    /*
     * Strategie, die in den ersten beiden Spielen aufjedenfall gespielt wird und
     * danach nur noch, wenn die Strategie des Gegners nicht erkannt werden konnte
     */
    private int strategyOne(int pointCard) {
        int card = 0;
        switch (countRounds) {
            /* spezielle Kartenlegetechnik für die erste Runde */
            case 1:
                card = roundOne(pointCard);
                break;
            /*
             * ab der zweiten Runde wird immer gleich entschieden, welche Karte zu spielen
             * ist
             */
            default:
                card=this.roundElse(pointCard);

        }
        /*
         * zum Schluss wird noch überprüft ob meine gewählte Karte noch in meinen
         * Handkarten vorhanden ist wenn ja wird sie gespielt enn nein wird eine Karte
         * aus der Mitte gespielt
         */
        if (myCards.contains(card)) {
            return card;
        } else {
            return playMiddleCard();
        }

    }

    private int roundOne(int pointCard) {
        // ist die PunkteKarte 9 oder 10 lege ich 14
        if (pointCard == 10 || pointCard == 9) {
            return 1;
        }
        // ist die PunkteKarte 6,7,8 lege ich 13
        else if (pointCard>5)
            return 13;
            // ist die Punktekarte -4,-5,4oder 5 lege ich 12
        else if (pointCard == -5 || pointCard == -4 || pointCard == 4 || pointCard == 5) {
            return 12;
        }
        // ist die PunkteKarte -1,-2,-3 lege ich 11
        else if (pointCard < 0) {
            return 10;

        }
        // ansonsten lege ich eine Karte mit dem gleichen Wert wie die PunkteKarte
        else {
            return pointCard;
        }
    }

    private int roundElse(int pointCard) {
        int card = 0;

        switch (pointCard) {
            /*
             * ist die PunkteKarte 9 oder 10 und hat der Gegner die gleiche höchste Karte
             * noch wie ich dann lege ich meine niedrigste Karte wenn nicht, lege ich meine
             * höchste Karte
             */
            case 10:
            case 9:
                if (enemyCards.get(enemyCards.size() - 1) != myCards.get(myCards.size() - 1)) {
                    card = myCards.get(myCards.size() - 1);

                } else {
                    card = myCards.get(0);
                }
                break;
            case 8:
            case 7:
            case 6:
                if (gameCards.contains(10)|| gameCards.contains(9)) {
                    card=pointCard+5;
                }
                else {
                    card = myCards.get(myCards.size()-1);
                }
                break;
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                card=pointCard;
                break;
            case -1:
            case -2:
            case -3:
                card= this.playMiddleCard();
                break;
            /*
             * ist die PunktKarte -4 oder -5 und der Gegner hat in der Runde zuvor noch
             * nicht die 12 gelegt oder ich habe keine 12 mehr dann lege ich meine höchste
             * Karte hat er die 12 schon gespielt und ich kann die 12 noch spielen dann
             * spiele ich die 12
             */

            case -4:
            case -5:
                if (enemyCards.contains(12)) {
                    card= myCards.get(myCards.size() - 1);
                } else {
                    if (myCards.contains(12)) {
                        return 12;
                    } else {
                        card= myCards.get(myCards.size() - 1);
                    }

                }
            default:
                card = pointCard;
        }
        return card;
    }

    /*
     * passt meine Karte auf die GegnerKarte an kann frühestens ab der dritten Runde
     * gespielt werden falls die Strategie des Gegners erkennbar war
     */
    private int strategyDynamic(int pointCard) {
        int card;
        int index = 0;
        int enemyValue;
        for (int i = 0; i < enemyStrategy.get(0).size(); i++) {
            if (enemyStrategy.get(0).get(i) == pointCard) {
                index = i;
                break;
            }
        }
        enemyValue = enemyStrategy.get(0).get(index + 1);
        /*
         * meine Karte ist immer um 1 höher als die Karte, die der Gegner wahrscheinlich
         * legt
         */
        if (enemyValue == 15) {
            card = 1;
        } else {
            card = ++enemyValue;
        }
        if (myCards.contains(card)) {
            return card;
        } else {
            return strategyOne(pointCard);
        }

    }

    /*
     * durchläuft die aktiven Player und gibt die letzte Karte des Players zurück,
     * wenn er nicht meine ID hat wenn er also mein Gegner ist
     */
    private int getLastEnemyCard() {
        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
            if (p.getId() != this.getId()) {
                return p.getLastMove().getValue();
            }
        }
        return -99;

    }

    /* spielt eine Karte aus der Mitte */
    private int playMiddleCard() {
        return myCards.get(myCards.size() / 2);
    }

    /*
     * falls der Gegner immer die gleiche Karte zu einer bestimmten Punkte Karte
     * legt, wird das erkannt PunkteKarte und entsprechende Karte, die der Gegner
     * legt, werden hintereinander in Strategie-Array-List gespeichert und so
     * einander zugeordnet
     */
    private void getStrategy(int pointCardValue, int enemyCard) {
        /*
         * in der ersten Runde wird die erste Dimension der Array-list mit den Karten
         * des Gegners zu den jeweiligen PunkteKarten hintereinander gefüllt
         */
        switch (countGames) {
            case 1:
                this.caseOne(pointCardValue, enemyCard);
                break;
            case 2:
                this.caseTwo(pointCardValue, enemyCard);
                break;
            case 3:
                this.caseThree(enemyCard);
                break;
            default:
                System.out.println(strategy);
        }

    }

    private void caseOne(int pointCardValue, int enemyCard) {
        if (countRounds == 1) {
            enemyStrategy.add(Spiele);
            enemyStrategy.get(0).add(pointCardValue);
        } /*
         * else if (countRounds==15){ enemyStrategy.get(0).add(pointCardValue); }
         */ else {
            enemyStrategy.get(0).add(enemyCard);
            enemyStrategy.get(0).add(pointCardValue);
        }
    }

    private void caseTwo(int pointCardValue, int enemyCard) {
        /* zunächst noch hinzufügen der letzten GegnerKarte aus dem ersten Spiel */
        if (countRounds == 1) {
            enemyStrategy.get(0).add(enemyCard);
            enemyStrategy.add(Spiele2);
            enemyStrategy.get(1).add(pointCardValue);
            /*
             * dann befüllen der zweiten Dimesnion der ArrayList mit den Karten des Gegners
             * zu den jeweiligen PunkteKarten hintereinander
             */
        } else {

            enemyStrategy.get(1).add(enemyCard);
            enemyStrategy.get(1).add(pointCardValue);
        }
    }

    private void caseThree(int enemyCard) {
        if (countRounds == 2) {
            enemyStrategy.get(1).add(enemyCard);
        } else if (countRounds == 3) {
            /*
             * überprüfen ob die Zuordnung der erten Dimension der List zur Zuordnung der
             * zweiten Dimension der List passt wenn nein wird strategy auf false gesetzt
             * --> der Gegner legt nicht jede Runde, die gleiche Karte auf eine PunkteKarte
             * --> ich kann die Strategie nicht lernen
             */

            int points;
            int enemy;
            for (int i = 0; i < enemyStrategy.get(0).size(); i += 2) {
                points = enemyStrategy.get(0).get(i);
                enemy = enemyStrategy.get(0).get(i + 1);

                for (int j = 0; j < enemyStrategy.get(1).size(); j += 2) {
                    if (enemyStrategy.get(1).get(j) == points && enemyStrategy.get(1).get(j + 1) != enemy) {
                        strategy = false;
                        break;
                    }
                }
            }
        }
    }
}