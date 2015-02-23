import java.util.*;
public class test {
	public static void main(String[] args){
		System.out.print("please input the precison and query:");
		Scanner stdin = new Scanner(System.in);
		String query = stdin.nextLine();
		String[] inp = query.split("\\ ");
		float precision = Float.parseFloat(inp[0]);
		query = inp[1];

		ArrayList<BingObject> result = new ArrayList<BingObject> ();
		try{
		result = BingSearch.search(query, precision);
		}catch(Exception e){

		}

		for(int i = 0; i< result.size(); i++){
			System.out.println(result.get(i).title);
			System.out.println(result.get(i).description);
			System.out.println(result.get(i).url);
			System.out.println();

		}
		String[] tmp = IRHelper.generateNewKey(result);
		System.out.println(tmp[0]+"\n"+tmp[1]);
	}
}
