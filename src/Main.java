import net.sf.json.JSONObject;
import mpi.*;

import java.io.*;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readFile();

	}
	
	public static double[] getLocation(String str){
		
		@SuppressWarnings("deprecation")
		JSONObject entry=new JSONObject(str);//http://docs.oracle.com/javaee/7/api/javax/json/JsonObject.html
	    System.out.println(entry.getJSONObject("json").getJSONObject("coordinates").getJSONArray("coordinates"));
		return null;
	}
	
	public static void readFile(){
		try{
			BufferedReader br = new BufferedReader(new FileReader("data/tinyTwitter.json"));
			int count = 0;
			while(br.ready()){
				String str = br.readLine();
				if(str.equals("[")||str.equals("]")){
					continue;
				}
				System.out.print(count+":");
				getLocation(str);
				count++;
			}
			br.close();
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
	}

}
