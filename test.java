import java.io.IOException;
import java.util.*;
public class test {
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);
		System.out.print("your precision: ");
		float precision = Float.parseFloat(stdin.nextLine());
		System.out.print("your key words: ");
		String query = stdin.nextLine().trim();
		ArrayList<BingObject> result = new ArrayList<BingObject> ();
		while(true){
			try {
				result = BingSearch.search(query, precision);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int count=0;
			for(int i = 0; i< result.size(); i++){
				int index = i+1;
				System.out.println();
				System.out.println("--------------- No. "+index+" -------------------");
				System.out.println(result.get(i).title);
				System.out.println(result.get(i).description);
				System.out.println(result.get(i).url);
				System.out.println("do you think it is relevant? Y/N");
				if(stdin.nextLine().toLowerCase().equals("y")){
					result.get(i).setRelevant();
					count++;
				}
			}
			if(count==0){
				System.out.println("precision is 0, I can only help you to here =.=");
				System.out.println();
				System.exit(1);
			}else if(count<precision*result.size()){
				try {
					String[] tmp = IRHelper.generateNewKey(result, query);
					System.out.println("=====================");
					System.out.println("FEEDBACK");
					System.out.println("precision:"+(float)count/10);
					System.out.println("we are going to add new key words: "+tmp[0]+" "+tmp[1]);
					System.out.println();
					query+=" "+tmp[0]+" "+tmp[1];
					count = 0;
					//System.out.println(tmp[0]+"\n"+tmp[1]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println("precision:"+(float)count/10);
				System.out.println("precision is high enough, byebye =.=");
				System.out.println();
				System.exit(1);
			}
		}
	}
}
