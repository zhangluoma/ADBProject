import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

//Download and add this library to the build path.
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

public class BingSearch {
	public static ArrayList<BingObject> search(String keyWords,float precision) throws IOException {
		keyWords = keyWords.trim();
		String words = keyWords.replaceAll(" ","%20");
		String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27"+words+"%27&$top=10&$format=JSon";
		//Provide your account key here. 
		String accountKey = "le5VXjKjrgc7nRivO9KXOSU+vKyaMr4cnFnktYD8dQE";
		byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		URL url = new URL(bingUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
				
		InputStream inputStream = (InputStream) urlConnection.getContent();		
		byte[] contentRaw = new byte[urlConnection.getContentLength()];
		inputStream.read(contentRaw);
		String content = new String(contentRaw);
		JSONObject json = new JSONObject(content);
		JSONArray result = json.getJSONObject("d").getJSONArray("results");
		ArrayList<BingObject> bol = new ArrayList<BingObject>();
		for(int i=0;i<result.length();i++){
			JSONObject curr = result.getJSONObject(i);
			String title = curr.getString("Title");
			String description = curr.getString("Description");
			String myUrl = curr.getString("Url");
			bol.add(new BingObject(title,description,myUrl));
		}
		return bol;
		//The content string is the xml/json output from Bing.
		//System.out.println(content);
	}
}
class BingObject{
	public String title;
	public String description;
	public String url;
	public boolean flag=false;
	public BingObject(String title,String description,String url){
		this.title=title;
		this.description=description;
		this.url=url;
	}
	public void setRelevant(){
		this.flag=true;
	}
}