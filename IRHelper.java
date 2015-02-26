import java.util.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
/**
* IRHelper contains several static methods for information retrieval
* @author  Luoma Zhang, Huaiyuan Cao
* @version 1.0
* @since   2015.2.25
*/
public class IRHelper{
	/**
	* calculate the score of documents based on the given keyWords
	* @param bol the given documents
	* @param keyWords user input key words to search
	*/
	public static double[] getScore(ArrayList<BingObject> bol, String keyWords){
		keyWords = keyWords.toLowerCase();
		keyWords = keyWords.trim();
		String[] query = keyWords.split(" ");
		double[] score = new double[bol.size()];
		double[][] tf = new double[query.length][bol.size()];
		int[] df = new int[query.length];
		for(int i = 0; i<query.length; i++){
			for(int j = 0; j < bol.size(); j++){
				String[] wordsDes = bol.get(j).description.split(" ");
				String[] wordsTitle = bol.get(j).title.split(" ");
				String[] words = new String[wordsDes.length+wordsTitle.length];
				for(int k = 0; k<wordsDes.length; k++)
					words[k] = wordsDes[k];
				for(int k = 0; k<wordsTitle.length; k++)
					words[k+wordsDes.length] = wordsTitle[k];

				int tmp = df[i];
				tf[i][j] = 0.5;  // smoothing
				//count the term frequency and document frequency.
				for(int k=0;k<words.length;k++){
					words[k] = words[k].toLowerCase();
					if(words[k].equals(query[i])){
						tf[i][j]++;
						df[i]++;
					}
					//System.out.println(tf[i][j]);
					/*term frequency only count once every loop for a document*/
					if(df[i] > tmp + 1)
						df[i] = tmp + 1;
				}
			}
		}
		/* calculate idf to balance the score of very frequently appeared words.*/
		double[] idf = new double[query.length];
		for(int i = 0; i<query.length; i++){
			idf[i] = Math.log(((double)bol.size()+1)/(double)df[i]);  // smoothing
		}
		/* calculate score based on both tern frequency and idf */
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
	/**
	* this method is used to generate the new key words to improve the accuracy of the search
	* @param bol the input BingObjects
	* @param keyWords user input key words to search
	* @param countnum what is this shit?
	*/
	public static String[] generateNewKey(ArrayList<BingObject> bol, String keyWords, int countnum) throws IOException{
		OrderCounter oc = new OrderCounter();
		keyWords = keyWords.toLowerCase();
		keyWords = keyWords.trim();
		String[] query = keyWords.split(" ");

		double[] score = getScore(bol, keyWords);
		
		ArrayList<String> queryList = new ArrayList<String>();
		for(String q:query){
			queryList.add(q);
		}
		// map used to calculate the score of every term
		HashMap<String,Double> map = new HashMap<String,Double>();
		Set<String> stopwords = new HashSet<String>();

		FileInputStream fstream = new FileInputStream("stopwords.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;
		while ((strLine = br.readLine()) != null){
			stopwords.add(strLine);
		}
		br.close();
		//calculate the score of terms and pick the top 2 most important term.
		for(int t = 0; t< bol.size();t++){
			bol.get(t).description = bol.get(t).description.toLowerCase();
			bol.get(t).title = bol.get(t).title.toLowerCase();
			String[] wordsDes = bol.get(t).description.split(" ");
			for(int i = 0; i<wordsDes.length; i++){
				System.out.println(wordsDes[i]);
			}
			String[] wordsTitle = bol.get(t).title.split(" ");
			for(int i = 0; i<wordsTitle.length; i++){
				System.out.println(wordsTitle[i]);
			}
			String[] words = new String[wordsDes.length+wordsTitle.length];
			for(int i = 0; i<wordsDes.length; i++){
				words[i] = wordsDes[i];
			}
			for(int i = 0; i<wordsTitle.length; i++){
				words[i+wordsDes.length] = wordsTitle[i];
			}

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
				oc.storeSample(words);
				for(int i=0;i<words.length;i++){
					if(words[i].length()<1)
						continue;
					if(queryList.indexOf(words[i]) >= 0)
						continue;
					if(!stopwords.contains(words[i])){
						if(!map.containsKey(words[i])){
							map.put(words[i],score[t]*(double)countnum/10);
						}else{
							map.put(words[i],map.get(words[i])+score[t]*(double)countnum/10);
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
							map.put(words[i], -1*score[t]*(1-(double)countnum/10));
						}else{
							map.put(words[i], map.get(words[i])-score[t]*(1-(double)countnum/10));
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
			System.out.println("key="+key+" count="+map.get(key));
			if(currCount>count[1]){
				count[1]=currCount;
				keys[1]=key;
			}else if(currCount>count[0]){
				count[0]=currCount;
				keys[0]=key;
			}
		}
		oc.setWords(keys);
		try{
			oc.calculateOrder();
		}catch(Exception e){
		}
		return oc.orderedResult();
	}
	/**
	* This method is to fetch the html from the page, now it is deprecated.
	* @param url input url of the target page
	*/
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
/**
* OrderCounter store the used queries, calculate the right order of the two key words
* @author  Luoma Zhang, Huaiyuan Cao
* @version 1.0
* @since   2015.2.25
*/
class OrderCounter{
	String[] words;
	ArrayList<String[]> samples = new ArrayList<String[]>(); 
	int[] score = new int[2];
	/**
	* set the key words
	* @param words the key words
	*/
	public void setWords(String[] words){
		this.words=words.clone();
	}
	/**
	* store the useful samples
	* @param sample useful string array sample
	*/
	public void storeSample(String[] sample){
		samples.add(sample);
	}
	/**
	* calculate the order of the key word
	*/
	public void calculateOrder() throws NonCounterException{
		if(words==null){
			throw new NonCounterException();
		}
		for(String[] sl:samples){
			for(int i=0;i<sl.length-1;i++){
				if(sl[i].equals(words[0])&&sl[i+1].equals(words[1])){
					score[0]++;
				}else if(sl[i].equals(words[1])&&sl[i+1].equals(words[0])){
					score[1]++;
				}
			}
		}
	}
	/**
	* return the right order of key words
	*/
	public String[] orderedResult(){
		String[] result = words.clone();
		if(score[0]<score[1]){
			String tmp = result[1];
			result[1]=result[0];
			result[0]=tmp;
		}
		return result;
	} 
}
class NonCounterException extends Exception{}