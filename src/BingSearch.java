import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


//Download and add this library to the build path.
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

public class BingSearch {

	public static void main(String[] args) throws IOException {
		String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27Milky%20Way%27&$top=10&$format=JSon";
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
		for(int i=0;i<result.length();i++){
			JSONObject curr = result.getJSONObject(i);
			String uri = curr.getJSONObject("__metadata").getString("uri");
			String id = curr.getString("ID");
			String title = curr.getString("Title");
			String description = curr.getString("Description");
			String displayUrl = curr.getString("DisplayUrl");
			String myUrl = curr.getString("Url");
			System.out.println(title);
			System.out.println(description);
			System.out.println();
		}
		//The content string is the xml/json output from Bing.
		//System.out.println(content);
	}

}