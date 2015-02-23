import java.util.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class IRHelper{

	public static double[] getScore(ArrayList<BingObject> bol, String keyWords){
		keyWords = keyWords.toLowerCase();
		keyWords = keyWords.trim();
		String[] query = keyWords.split(" ");
		double[] score = new double[bol.size()];
		int[][] tf = new int[query.length][bol.size()];
		int[] df = new int[query.length];
		for(int i = 0; i<query.length; i++){
			for(int j = 0; j < bol.size(); j++){
				String[] words = bol.get(j).description.split(" ");
				int tmp = df[i];
				for(int k=0;k<words.length;k++){
					words[k] = words[k].toLowerCase();
					if(words[k].equals(query[i])){
						tf[i][j]++;
						df[i]++;
					}
					//System.out.println(tf[i][j]);
					if(df[i] > tmp + 1)
						df[i] = tmp + 1;
				}
			}
		}

		double[] idf = new double[query.length];
		for(int i = 0; i<query.length; i++){
			idf[i] = Math.log((double)bol.size()/(double)df[i]);
		}

		for(int i = 0; i<score.length; i++){
			for(int j = 0; j<query.length; j++){
				score[i] += tf[j][i] * idf[j];
			}
		}
		return score;


	}

	public static String[] generateNewKey(ArrayList<BingObject> bol, String keyWords) throws IOException{
		keyWords = keyWords.toLowerCase();
		keyWords = keyWords.trim();
		String[] query = keyWords.split(" ");
		ArrayList<String> queryList = new ArrayList<String>();
		for(String q:query){
			queryList.add(q);
		}

		HashMap<String,Integer> map = new HashMap<String,Integer>();
		Set<String> stopwords = new HashSet<String>();

		FileInputStream fstream = new FileInputStream("stopwords.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;
		while ((strLine = br.readLine()) != null){
			stopwords.add(strLine);
		}
		br.close();

		for(BingObject bo : bol){
			if(bo.flag){
				bo.description = bo.description.toLowerCase();
				String[] words = bo.description.split(" ");
				for(int i=0;i<words.length;i++){
					if(queryList.indexOf(words[i]) >= 0 || words[i].charAt(0)<'a' && words[i].charAt(0)<'z')
						continue;
					if(!stopwords.contains(words[i])){
						if(!map.containsKey(words[i])){
							map.put(words[i],1);
						}else{
							map.put(words[i],map.get(words[i])+1);
						}
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
			//System.out.println("key="+key+" count="+map.get(key));
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