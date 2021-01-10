package de.rabitem.main.player.instances;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import java.util.ArrayList;

/**
 * Die Klasse Samirsbot verwendet folgende Strategie bei dem Kartenspiel Hols der Geier:
 * Bei niederigen Geierkarten vom Punktebereich -2,-1,1,2,3 wird die n�chst h�here Erdm�nnchenkarten vom Punktebereich 1 bis 5 gespielt.
 * Bei mittleren Geierkarten vom Punktebereich -3,4,5,6,7 wird die n�chst h�here Erdm�nnchenkarten vom Punktebereich 6 bis 10 gespielt.
 * Bei hohen Geierkarten vom Punktebereich -4,-5,8,9,10 wird die n�chst h�here Erdm�nnchenkarten vom Punktebereich 11 bis 15 gespielt.
 * 
 * 
 * @author Samir K�nigstein
 */



public class Samir extends Player {

	// expliziter Aufruf des Konstruktors der Vaterklasse Player dem ich meinen Namen �bermittle.
	public Samir(String name) {
		super("Samir");
	// 	
		customResets();
	}

	// Beinhaltet meine Karten im niedrigen Kartenbereich 1-5.
    private ArrayList<Integer> niedrigeKarten = new ArrayList<Integer>();
    // Beinhaltet meine Karten im mittleren Kartenbereich 6-10.
    private ArrayList<Integer> mittlereKarten = new ArrayList<Integer>();
    // Beinhaltet meine Karten im hohen Kartenbereich 11-15.
    private ArrayList<Integer> hoheKarten = new ArrayList<Integer>();
    
    public void customResets() {
    // Setzt alle n�tigen Variablen auf den Spielanfangszustand zur�ck.	
        niedrigeKarten.clear();	
    // F�gt Erdm�nnchenkarten von 1-5 hinzu.
   		for (int i = 1; i < 6; i++)		
   			niedrigeKarten.add(i);
   		
   	// Setzt alle n�tigen Variablen auf den Spielanfangszustand zur�ck.
   		mittlereKarten.clear();
   	// F�gt Erdm�nnchenkarten von 6-10 hinzu.
   		for (int i = 6; i < 11; i++)
   			mittlereKarten.add(i);
   		
   	// Setzt alle n�tigen Variablen auf den Spielanfangszustand zur�ck.
   		hoheKarten.clear();
   	// F�gt Erdm�nnchenkarten von 11-15 hinzu.
   		for (int i = 11; i < 16; i++)
   			hoheKarten.add(i);
   	
   		}
   
    // Hier wird die Methode definiert welche Erdm�nnchenkarte bei dem Geierstapel gelegt werden soll.
    public  PlayerCard getNextCardFromPlayer(final int pointCardValue) {
	 
    // Variable p muss mit einem Wert initialisert werden.
	   int p = -99;		
	   
	/** �berpr�fung in welchem Wertebereich die Geierkarte (pointCardValue) befindet.
	*   Wenn die Geierkarte sich im Wertebereich -2 bis 3 befindet, lege eine Erdm�nnchenkarte aus dem niedrigem Stapel.
	*   Falls dies der Falls sein sollte, wird mit remove die Karte aus dem Stapel genommen. 
	*   Falls die Geierkarte nicht in dem Wertebereich liegt, �berpr�fe die n�chste if-Abfrage. 
	*/
	   
	   if (-3 < pointCardValue && pointCardValue < 4 ) {   
		   p = niedrigeKarten.get(0);
	  niedrigeKarten.remove(niedrigeKarten.indexOf(p));
          
      }
	   
	 /** �berpr�fung in welchem Wertebereich die Geierkarte (pointCardValue) befindet.
	 *   Wenn die Geierkarte sich im Wertebereich 4 bis 7 und gleich -3 befindet, lege eine Erdm�nnchenkarte aus dem mittleren Stapel.
	 *   Falls dies der Falls sein sollte, wird mit remove die Karte aus dem Stapel genommen. 
	 *   Falls die Geierkarte nicht in dem Wertebereich liegt, �berpr�fe die n�chste if-Abfrage. 
		*/

      else if (3 < pointCardValue && pointCardValue < 8 || pointCardValue ==-3){
    	  p = mittlereKarten.get(0);
    	  mittlereKarten.remove(mittlereKarten.indexOf(p));
      }
	   
	   /** �berpr�fung in welchem Wertebereich die Geierkarte (pointCardValue) befindet.
		*   Wenn die Geierkarte sich im Wertebereich -4 bis -5 und im Wertebereich 8 bis 10 befindet, lege eine Erdm�nnchenkarte aus dem hohen Stapel.
		*   Falls dies der Falls sein sollte, wird mit remove die Karte aus dem Stapel genommen.  
		*/
	   
      else if (-6 < pointCardValue && pointCardValue < -3 || 7 < pointCardValue && pointCardValue < 11){
    	 p = hoheKarten.get(0);
    	  hoheKarten.remove(hoheKarten.indexOf(p));
      }
	   // Anhand der return Anweisung, wird die gespielte Karte zur�ckgegeben.
	  return new PlayerCard(p);
      
	  
     }
	
	
}
