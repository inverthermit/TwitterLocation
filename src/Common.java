import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Common {
	public static final double[][][] BOXES = {
		{{ 144.7, -37.5 }, { 144.85, -37.5 }, { 144.85, -37.65 }, { 144.7, -37.65 }},
		{{ 144.85, -37.5 }, { 145.0, -37.5 }, { 145.0, -37.65 }, { 144.85, -37.65 }},
		{{ 145.0, -37.5 }, { 145.15, -37.5 }, { 145.15, -37.65 }, { 145.0, -37.65 }},
		{{ 145.15, -37.5 }, { 145.3, -37.5 }, { 145.3, -37.65 }, { 145.15, -37.65 }},
		{{ 144.7, -37.65 }, { 144.85, -37.65 }, { 144.85, -37.8 }, { 144.7, -37.8 }},
		{{ 144.85, -37.65 }, { 145.0, -37.65 }, { 145.0, -37.8 }, { 144.85, -37.8 }},
		{{ 145.0, -37.65 }, { 145.15, -37.65 }, { 145.15, -37.8 }, { 145.0, -37.8 }},
		{{ 145.15, -37.65 }, { 145.3, -37.65 }, { 145.3, -37.8 }, { 145.15, -37.8 }},
		{{ 144.7, -37.8 }, { 144.85, -37.8 }, { 144.85, -37.95 }, { 144.7, -37.95 }},
		{{ 144.85, -37.8 }, { 145.0, -37.8 }, { 145.0, -37.95 }, { 144.85, -37.95 }},
		{{ 145.0, -37.8 }, { 145.15, -37.8 }, { 145.15, -37.95 }, { 145.0, -37.95 }},
		{{ 145.15, -37.8 }, { 145.3, -37.8 }, { 145.3, -37.95 }, { 145.15, -37.95 }},
		{{ 145.3, -37.8 }, { 145.45, -37.8 }, { 145.45, -37.95 }, { 145.3, -37.95 }},
		{{ 145.0, -37.95 }, { 145.15, -37.95 }, { 145.15, -38.1 }, { 145.0, -38.1 }},
		{{ 145.15, -37.95 }, { 145.3, -37.95 }, { 145.3, -38.1 }, { 145.15, -38.1 }},
		{{ 145.3, -37.95 }, { 145.45, -37.95 }, { 145.45, -38.1 }, { 145.3, -38.1 }}
		};
	public static final int ROW_NUM = 4;
	public static final int COLUMN_NUM = 5;
	public static final String[] BOX_NAMES ={
		"A1","A2","A3","A4",
		"B1","B2","B3","B4",
		"C1","C2","C3","C4","C5",
		"D3","D4","D5"
	};
	public static final int AREA_SIZE = BOX_NAMES.length;
	
	public static void append2File(String path,String content) throws IOException{
		File file = new File(path);
		file.createNewFile(); // if file already exists will do nothing
		BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
		bw.write(content);
		bw.newLine();
		bw.close();
		
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> 
    sortByValue( Map<K, V> map )
	{
	    List<Map.Entry<K, V>> list =
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return -(o1.getValue()).compareTo( o2.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}
}
