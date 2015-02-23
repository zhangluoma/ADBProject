public class IRHelper{
	public static String[] generateNewKey(ArrayList<BingObject> bol){
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(BingObject bo : bol){

		}
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