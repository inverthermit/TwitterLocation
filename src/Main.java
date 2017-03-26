import net.sf.json.JSONObject;
import mpi.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getLocation();

	}
	
	public static double[] getLocation(){
		JSONObject entry=new JSONObject();//http://docs.oracle.com/javaee/7/api/javax/json/JsonObject.html
	    entry.put("target", "course");
	    entry.put("location", "[111,222]");
	    
	    System.out.println(entry.getJSONArray("location"));
		return null;
	}

}
