package algorithm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class CalendarSchedule {

    public static void main(String[] args) {
    	List<String> playersList = new LinkedList<String>();
    	playersList.add("1");
    	playersList.add("2");
    	playersList.add("3");
    	playersList.add("4");
    	playersList.add("5");
        playersList.add("6");
        playersList.add("7");
        playersList.add("8");
        playersList.add("9");    	
        if (playersList.size() % 2 != 0)
        {
            playersList.add("Descansa"); 
        }
        Collections.shuffle(playersList);

        int numDays = (playersList.size() - 1);
        int halfSize = playersList.size() / 2;

        for (int day = 0; day < numDays*2; day++){
            System.out.println("\n\nDay " +  (day + 1));

            for (int i = 0; i < halfSize; i++)
            { 
            	if(numDays > day){
            		System.out.println(playersList.get(i) +" vs " + playersList.get(i+halfSize));
            	}else{
            		System.out.println(playersList.get(i+halfSize) +" vs " + playersList.get(i));
                }
                
            }
            playersList = rotateList(playersList, halfSize);
        }
    }

	private static List<String> rotateList(List<String> playersList, int halfSize) {
		List<String> finalList = new LinkedList<String>();
		finalList.add(playersList.get(0));
		finalList.add(playersList.get(halfSize));
		
		for(int i = 2;i<halfSize;i++){
			finalList.add(playersList.get(i-1));
		}
		for(int i = halfSize;i< playersList.size() -1;i++){
			finalList.add(playersList.get(i+1));
		}
		
		finalList.add(playersList.get(halfSize-1));
		
		return finalList;
	}
}
