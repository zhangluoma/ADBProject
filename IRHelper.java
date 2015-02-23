import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Scanner;
public class IRHelper{
	public static String[] generateNewKey(ArrayList<BingObject> bol){
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(BingObject bo : bol){
			if(bo.flag){
				String[] words = bo.description.split(" ");
				for(int i=0;i<words.length;i++){
					if(!map.containsKey(words[i])){
						map.put(words[i],1);
					}else{
						map.put(words[i],map.get(words[i])+1);
					}
				}
			}
		}
		String[] keys = new String[2];
		int[] count = new int[2];
		for(int i=0;i<count.length;i++) count[i]=Integer.MIN_VALUE;
		for(String key:map.keySet()){
			int currCount = map.get(key);
			if(currCount>count[1]){
				count[1]=currCount;
				keys[1]=key;
			}else if(currCount>count[0]){
				count[0]=currCount;
				keys[0]=key;
			}
			System.out.println("key="+key+" count="+map.get(key));
		}
		return keys;
	}
	private String htmlFetch(String url){
		URL currUrl=null;
		String content=null;
		try {
		  currUrl = new URL(url);
		  String currContent = null;
		  URLConnection currConnection =  currUrl.openConnection();
		  Scanner scanner = new Scanner(currConnection.getInputStream());
		  scanner.useDelimiter("\\Z");
		  content = scanner.next();
		}catch ( Exception ex ) {
		    ex.printStackTrace();
		}
		return content;
	}
}