package starMade;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class Json {
	public static String getPath (JSONObject json, String path){
		if (!path.contains("/")) {
			return json.get(path).toString();
		} else {
			String[] paths = path.split("/");
			JSONObject newJson = (JSONObject) json.get(paths[0]);
			String newPath = path.substring(path.indexOf("/") + 1);
			return getPath(newJson, newPath);
		}
	}
	public static String getPath (String json, String path){
		JSONParser parser = new JSONParser();
		JSONObject jsonobj = null;
		try {
			jsonobj = (JSONObject) parser.parse(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			VoteCheck.out("Failed to parse JSON String");
			VoteCheck.out(json);
		}
		if (jsonobj != null){
			return getPath(jsonobj, path);
		}else{
			return "";
		}
	}
	public static JSONObject getObj(String json){
		JSONParser parser = new JSONParser();
		JSONObject jsonobj = null;
		try {
			jsonobj = (JSONObject) parser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonobj;
	}
}
