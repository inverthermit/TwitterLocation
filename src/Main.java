import mpi.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
public class Main {
	
	public static int[] assignArea = new int[Common.AREA_SIZE];//size of areas
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date startDate = new Date();
		String start = (dateFormat.format(startDate));
		MPI.Init(args);
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
	  
	    if (rank == 0){
	      if(size == 1){
	    	  BufferedReader br = new BufferedReader(new FileReader(args[3]));//data/tinyTwitter.json
				while(br.ready()){
					//System.out.println(count);
					String str = br.readLine();
					if(str.equals("[")||str.equals("]")){
						continue;
					}
					int index = json2BoxName(str);
					if(index!=-1){
					      assign2Area(index);
				    }
				}
				br.close();
	      }
	      else{
	    	  BufferedReader br = new BufferedReader(new FileReader(args[3]));//data/tinyTwitter.json
				int count = 0;
				while(br.ready()){
					//System.out.println(count);
					String str = br.readLine();
					if(str.equals("[")||str.equals("]")){
						continue;
					}
					String[] msg = new String[1]; 
					msg[0] = str;
					//System.out.println((count%(size-1)+1));
					int workRank = count%(size-1)+1;
			        MPI.COMM_WORLD.Send(msg, 0, 1, MPI.OBJECT, workRank, 13);
			        if(workRank == (size-1)){
			        	for(int i=1;i<size;i++){
			        		int[] index = new int[1];
					        MPI.COMM_WORLD.Recv(index, 0, 1, MPI.INT, i, 13);
					        if(index[0]!=-1){
							      assign2Area(index[0]);
						    }
			        	}
			        }
			        count++;
				}
				//Last few workRanks
				//System.out.println(count+"-"+count%(size-1));
				for(int i=1;i<=count%(size-1);i++){
	        		int[] index = new int[1];
			        MPI.COMM_WORLD.Recv(index, 0, 1, MPI.INT, i, 13);
			        if(index[0]!=-1){
					      assign2Area(index[0]);
				    }
	        	}
				
				br.close();
				for(int i=0;i<size-1;i++){
					String[] msg = new String[1]; 
					msg[0] = "end";
			        MPI.COMM_WORLD.Send(msg, 0, 1, MPI.OBJECT, i+1, 13);
			        int[] index = new int[1];
			        MPI.COMM_WORLD.Recv(index, 0, 1, MPI.INT, i+1, 13);
			        //System.out.println("Finished Receiving end");
				}
	      }
	      
	    }else {
	    	//System.out.println("rank:"+rank);
	    	while(true){
	    		  String[] message = new String[1]; 
		  	      MPI.COMM_WORLD.Recv(message, 0, 1, MPI.OBJECT, 0, 13);
		  	      //System.out.println(message[0]);
		  	      if(message[0].equals("end")){
		  	    	int[] index = new int[1];
			  	      //System.out.println(message[0]);
			  	      index[0]= -1;
			  	      MPI.COMM_WORLD.Send(index, 0, 1, MPI.INT, 0, 13);
		  	    	  break;
		  	      }
		  	      int[] index = new int[1];
		  	      //System.out.println(message[0]);
		  	      index[0]= json2BoxName(message[0]);
		  	      MPI.COMM_WORLD.Send(index, 0, 1, MPI.INT, 0, 13);
	    	}	      
	    }
	    System.out.println(rank+":Ready to finalize");
	    if(rank == 0){
	    	int[] counts = new int[4];
	    	for(int i=0;i<Common.AREA_SIZE;i++){
	    		 String outLine = Common.BOX_NAMES[i]+":"+assignArea[i];
	    		 if(i<4){
	    			 counts[0]+=assignArea[i];
	    		 }
	    		 else if(i<8){
	    			 counts[1]+=assignArea[i];
	    		 }
	    		 else if(i<13){
	    			 counts[2]+=assignArea[i];
	    		 }
	    		 else if(i<16){
	    			 counts[3]+=assignArea[i];
	    		 }
	    		  System.out.println(outLine);
	    		  Common.append2File(args[4],outLine);
	      }
	      for(int i=0;i<4;i++){
	    	  String line = (char)('A'+i)+":"+counts[i];
	    	  Common.append2File(args[4],line);
	    	  System.out.println(line);
	      }
	    	 Date endDate = new Date();
	 		String end = (dateFormat.format(endDate));
	 		System.out.println("Start:"+start);
	 		System.out.println("End: "+end);
	 		/*for(int i=0;i<args.length;i++){
	 			System.out.println(args[i]);
	 		}*/
	    }
	    MPI.Finalize() ;
		//run();
		

	}
	
	public static void append2File(String path,String content) throws IOException{
		File file = new File(path);
		file.createNewFile(); // if file already exists will do nothing
		BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
		bw.write(content);
		bw.newLine();
		bw.close();
		
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
		str=str+"\r\n";
		str=str.replace(",\r\n", "");
		double[] result = new double[2];
		Gson gson = new Gson();
		JsonElement jelem = gson.fromJson(str, JsonElement.class);
		JsonObject jobj = jelem.getAsJsonObject();
		JsonArray coordinates = jobj.getAsJsonObject("json").getAsJsonObject("coordinates").getAsJsonArray("coordinates");
	    if(coordinates.size() == 2){
	    	result[0] = Double.parseDouble(coordinates.get(0).toString());
	    	result[1] = Double.parseDouble(coordinates.get(1).toString());
	    }
	    else{
	    	return null;
	    }
		return result;
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
		if(index>=0)
		assignArea[index]++;
	}
	
	public static void save2file(){
		for(int i=0;i<assignArea.length;i++){
			String line = Common.BOX_NAMES[i]+":"+assignArea[i];
			//Save Line to a file
		}
	}
	

}
