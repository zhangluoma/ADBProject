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
		double[][] tf = new double[query.length][bol.size()];
		int[] df = new int[query.length];
		for(int i = 0; i<query.length; i++){
			for(int j = 0; j < bol.size(); j++){
				String[] words = bol.get(j).description.split(" ");
				int tmp = df[i];
				tf[i][j] = 0.2;  // smoothing
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
			idf[i] = Math.log(((double)bol.size()+1)/(double)df[i]);  // smoothing
		}

		for(int i = 0; i<score.length; i++){
			for(int j = 0; j<query.length; j++){
				score[i] += tf[j][i] * idf[j];
			}
		}
		/*
		for(int i = 0; i<score.length; i++){
			System.out.println(score[i]);
		}*/
		return score;


	}

	public static String[] generateNewKey(ArrayList<BingObject> bol, String keyWords) throws IOException{
		keyWords = keyWords.toLowerCase();
		keyWords = keyWords.trim();
		String[] query = keyWords.split(" ");

		double[] score = getScore(bol, keyWords);
		
		ArrayList<String> queryList = new ArrayList<String>();
		for(String q:query){
			queryList.add(q);
		}

		HashMap<String,Double> map = new HashMap<String,Double>();
		Set<String> stopwords = new HashSet<String>();

		FileInputStream fstream = new FileInputStream("stopwords.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;
		while ((strLine = br.readLine()) != null){
			stopwords.add(strLine);
		}
		br.close();

		for(int t = 0; t< bol.size();t++){
			bol.get(t).description = bol.get(t).description.toLowerCase();
			String[] words = bol.get(t).description.split(" ");
			for(int i=0;i<words.length;i++){

				char[] arr = words[i].toCharArray();
				String str = "";
				for(int j = 0; j<arr.length; j++){
					if(arr[j] >='a' && arr[j] <='z' || arr[j] >='0' && arr[j]<='9')
						str += arr[j];
				}
				words[i] = str;
			}
			if(bol.get(t).flag){
				for(int i=0;i<words.length;i++){
					if(words[i].length()<1)
						continue;
					if(queryList.indexOf(words[i]) >= 0)
						continue;
					if(!stopwords.contains(words[i])){
						if(!map.containsKey(words[i])){
							map.put(words[i],score[t]);
						}else{
							map.put(words[i],map.get(words[i])+score[t]);
						}
					}					
				}
			}else{
				for(int i=0;i<words.length;i++){
					if(words[i].length()<1)
						continue;
					if(queryList.indexOf(words[i]) >= 0)
						continue;
					if(!stopwords.contains(words[i])){
						if(!map.containsKey(words[i])){
							map.put(words[i], -1*score[t]);
						}else{
							map.put(words[i],map.get(words[i])-score[t]);
						}
					}					
				}
			}
		}
		String[] keys = new String[2];
		double[] count = new double[2];
		for(int i=0;i<count.length;i++) count[i]=Double.MIN_VALUE;
		for(String key:map.keySet()){
			double currCount = map.get(key);
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