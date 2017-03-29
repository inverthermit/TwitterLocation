import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import mpi.*;

import java.io.*;
public class Main {
	public static int[] assignArea = new int[1000];//size of areas
	public static int[] areaNames = new int[1000];//size of areas
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*MPI.Init(args);
		int me = MPI.COMM_WORLD.Rank();
		 int size = MPI.COMM_WORLD.Size();
		 System.out.println("Hi from <"+me+">"+size);
		 MPI.Finalize();*/
		run();
		

	}
	
	
	public static double[] getLocation(String str){
		double[] result = new double[2];
		@SuppressWarnings("deprecation")
		JSONObject entry=new JSONObject(str);//http://docs.oracle.com/javaee/7/api/javax/json/JsonObject.html
	    JSONArray coordinates = entry.getJSONObject("json").getJSONObject("coordinates").getJSONArray("coordinates");
	    if(coordinates.length() == 2){
	    	result[0] = Double.parseDouble(coordinates.get(0).toString());
	    	result[1] = Double.parseDouble(coordinates.get(1).toString());
	    }
	    else{
	    	return null;
	    }
		return result;
	}
	
	public static void run(){
		try{
			//TODO: Read the areas from file into areas
			int areasCount = 100;
			double[][][] areas = new double[areasCount][4][2];
			
			BufferedReader br = new BufferedReader(new FileReader("data/tinyTwitter.json"));
			int count = 0;
			while(br.ready()){
				String str = br.readLine();
				if(str.equals("[")||str.equals("]")){
					continue;
				}
				
				double[] coordinates = getLocation(str);
				if(coordinates != null){
					System.out.println(count+":"+coordinates[0]+" "+coordinates[1]);
					for(int i=0;i<areas.length;i++){
						if(inArea(areas[i],coordinates) == true){
							assign2Area(i);
							break;
						}
					}
					count++;
				}
			}
			br.close();
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	public static boolean inArea(double[][] area, double[] coordinates){ //area: {{1,2},{2,2},{2,3},{1,3}}
		if(coordinates.length != 2) return false;
		double longitude = coordinates[0];
		double latitude = coordinates[1];
		 
		if(longitude>area[0][0]&&latitude<area[0][1]  
				&&longitude<area[1][0]&&latitude<area[1][1] 
				&&longitude<area[2][0]&&latitude>area[2][1] 
				&&longitude>area[3][0]&&latitude>area[3][1]){
			return true;
		}
		else{
			return false;
		}
	}
	public static synchronized void assign2Area(int index){
		assignArea[index]++;
	}
	
	public static void save2file(){
		for(int i=0;i<assignArea.length;i++){
			String line = areaNames[i]+":"+assignArea[i];
			//Save Line to a file
		}
	}
	

}
