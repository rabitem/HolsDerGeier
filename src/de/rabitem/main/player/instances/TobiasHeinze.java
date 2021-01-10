package de.rabitem.main.player.instances;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;


/**
* @author Tobias Heinze
*/
public class TobiasHeinze extends Player {

	/**
	 * Constructor of TobiasBot
	*/
	public TobiasHeinze(final String name) {
		super(name);
	}
		   


	@Override
	public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
	
		// Bei Karten zwischen 1 und 3 wird immer das gelegt, was die Geierkarte ist
		    	   			    
		if (1 <= pointCardValue && pointCardValue <= 3) {
			return new PlayerCard(pointCardValue);
			
			//Bei Geierkarten zwischen 4 und 6 wird immer eine Karte gelegt, die 2 h�her ist
			} else if (4 <= pointCardValue && pointCardValue <=6) {
				return new PlayerCard(pointCardValue + 2);
				
			//Bei Geierkarten zwischen 7 und 10 wird immer eine Karte gelegt, die 5 h�her ist	
			}else if (7 <= pointCardValue && pointCardValue <= 10) {    		
				return new PlayerCard(pointCardValue + 5);
				
			//Bei der -1 wird die 4 gelegt	
			} else if (pointCardValue == -1) {    			
				return new PlayerCard(4);
					
			//Bei der -2 wird die 5 gelegt
			}else if (pointCardValue == -2) {    		
				return new PlayerCard(5);
				
			//Bei der -3 wird die 9 gelegt	
			}else if (pointCardValue == -3) {    		
				return new PlayerCard(9); 
				
			//Bei der -4 wird die 10 gelegt
			}else if (pointCardValue == -4) {   		
				return new PlayerCard(10);
					
			//Bei der -5 wird die 11 gelegt
			}else {    		
				return new PlayerCard(11);    	
			}  	
		    	
	}
	
}

