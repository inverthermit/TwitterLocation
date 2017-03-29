import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import mpi.*;

import java.io.*;
public class Main {
	
	public static int[] assignArea = new int[Common.AREA_SIZE];//size of areas
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MPI.Init(args);
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
	  
	    if (rank == 0){
	      String[] msg = new String[10]; 
	      msg[0] = new String("{\"json\":{\"coordinates\":{\"coordinates\":[145.19870907,-37.72320424]}}}");
	      MPI.COMM_WORLD.Send(msg, 0, 1, MPI.OBJECT, 1, 13);
	      int[] index = new int[10];
	      MPI.COMM_WORLD.Recv(index, 0, 1, MPI.INT, 1, 13);
	      //System.out.println(Common.AREA_SIZE);
	      
	    }else {
	    	System.out.println("rank:"+rank);
	      String[] message = new String[10]; 
	      MPI.COMM_WORLD.Recv(message, 0, 1, MPI.OBJECT, 0, 13);
	      //System.out.println(message[0]);
	      int[] index = new int[10];
	      index[0]= json2BoxName(message[0]);
	      assign2Area(index[0]);
	      MPI.COMM_WORLD.Send(index, 0, 1, MPI.INT, 0, 13);
	      
	    }
	    System.out.println("Ready to finalize");
	    if(rank == 0){
	    	 for(int i=0;i<Common.AREA_SIZE;i++){
	    		  System.out.println(Common.BOX_NAMES[i]+":"+assignArea[i]);
	      }
	    }
	    MPI.Finalize() ;
		//run();
		

	}
	
	public static int json2BoxName(String str){
		double[] coo = getLocation(str);
	      for(int i=0;i<Common.BOXES.length;i++){
				if(inArea(Common.BOXES[i],coo) == true){
					//System.out.println(Common.BOX_NAMES[i]);
					return i;
				}
			}
	      return -1;
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
		 
		if(longitude>area[0][0]
		   &&latitude<area[0][1]  
		   &&longitude<area[1][0]
		   //&&latitude<area[1][1] 
		   //&&longitude<area[2][0]
		   &&latitude>area[2][1] 
		   //&&longitude>area[3][0]
		   //&&latitude>area[3][1]
		   ){
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
			String line = Common.BOX_NAMES[i]+":"+assignArea[i];
			//Save Line to a file
		}
	}
	

}
