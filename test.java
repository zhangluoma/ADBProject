import java.io.IOException;
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

		try {
			result = BingSearch.search(query, precision);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for(int i = 0; i< result.size(); i++){
			System.out.println(result.get(i).title);
			System.out.println(result.get(i).description);
			System.out.println(result.get(i).url);
			System.out.println();

		}
		try {
			String[] tmp = IRHelper.generateNewKey(result, query);
			System.out.println(tmp[0]+"\n"+tmp[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double[] score = IRHelper.getScore(result, query);
		for(int i = 0; i< score.length; i++){
			System.out.println(score[i]);
		}

	}
}
