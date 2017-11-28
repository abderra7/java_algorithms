package algorithm;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class CalendarSchedule {

    public static void main(String[] args) {
    	List<String> playersList = readPlayersFromJson("files/players.json");   
    	List<String> finalList = new LinkedList<String>();
        if (playersList.size() % 2 != 0)
        {
            playersList.add("Descansa"); 
        }
        Collections.shuffle(playersList);

        int numDays = (playersList.size() - 1);
        int halfSize = playersList.size() / 2;

        for (int day = 0; day < numDays*2; day++){
            for (int i = 0; i < halfSize; i++)
            { 
            	if(numDays > day){
            		finalList.add(playersList.get(i) +"vs" + playersList.get(i+halfSize));
            	}else{
            		finalList.add(playersList.get(i+halfSize) +"vs" + playersList.get(i));
                }
                
            }
            playersList = rotateList(playersList, halfSize);
        }
        
        writeCalendarInJson("files/calendar.json", finalList, playersList.size()/2);
    }

	private static void writeCalendarInJson(String outJson, List<String> finalList, int size) {
        int dia=1;
        int count=1;
        JSONArray list = new JSONArray();
        JSONArray allList = new JSONArray();

        for(String s : finalList){
            JSONObject part = new JSONObject();
            part.put("id", count);
            part.put("psnLocal", s.split("vs")[0].split("\\(")[0]);
            part.put("equipoLocal", s.split("vs")[0].split("\\(")[1].replaceAll("\\)", ""));
            part.put("psnVisitante", s.split("vs")[1].split("\\(")[0]);
            part.put("equipoVisitante", s.split("vs")[1].split("\\(")[1].replaceAll("\\)", ""));
            list.add(part);   	
        	if(count%size == 0){
        		JSONObject obj = new JSONObject();
                obj.put("Partidos", list);
                obj.put("Jornada", dia);    
                allList.add(obj);
                dia++;
                list = new JSONArray();
        	}
        	count++;
        }

        try (FileWriter file = new FileWriter(outJson)) {
        	JSONObject obj = new JSONObject();
    		obj.put("Calendar", allList);
            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
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
	
	private static List<String> readPlayersFromJson(String friendsFile) {
        JSONParser parser = new JSONParser();
		List<String> playerList = new LinkedList<String>();
		JSONArray a;
		try {
			a = (JSONArray) parser.parse(new FileReader(friendsFile));
			for (Object o : a) {
				JSONObject person = (JSONObject) o;
				String name = (String) person.get("psn");
				String team = (String) person.get("team");

				playerList.add(name + "(" + team + ")");
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}		
		return playerList;
	}
	
}
