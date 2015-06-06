package starMade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import starNet.StarMadeNetUtil;

public class VoteCheck {
	public static void main(String[] args) {
		//long unixTime = System.currentTimeMillis() / 1000L;
		out(" == CraftAU Starmade Vote Checker == ", false);
		out(" ==          by Caraxian          == ", false);
		File f = new File("./SMVC_settings.json");
		if(!(f.exists() && !f.isDirectory())) {
			if (!createSettingsFile()){
				out("Unable To Run");
				System.exit(0);
			}
		}
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get("./SMVC_settings.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject settings = Json.getObj(new String(encoded, StandardCharsets.UTF_8));

		if (Json.getPath(settings, "messages/start").toString().length() != 0){
			execute("/chat " + Json.getPath(settings, "messages/start"), settings);
		}
		String url = Json.getPath(settings,"passthrough") + "http://starmade-servers.com/api/?object=servers&element=votes&key=" + Json.getPath(settings,"serverkey");
		out(url);
		String content = HttpRequest.get(url).accept("application/json").body();
		String votesstr = Json.getPath(content,"votes");
		if (votesstr != ""){
			Object votesobj=JSONValue.parse(votesstr);
			JSONArray votes=(JSONArray)votesobj;
			String message = Json.getPath(settings,"messages/vote");
			for(int i=0; i<votes.size(); i++){
				try {Thread.sleep(Integer.parseInt(settings.get("throttle").toString()));} catch(InterruptedException ex) {Thread.currentThread().interrupt();}
				JSONObject vote = Json.getObj(votes.get(i).toString());
				out(vote.toString());	//DEBUG
				String url2 = "";
				if (Integer.parseInt(vote.get("claimed").toString()) == 0 ){
					out(vote.get("nickname") + " Vote Detected. Checking...");
					url2 = Json.getPath(settings,"passthrough") 
							+ "https://starmade-servers.com/api/?object=votes&element=claim&key=" 
							+ Json.getPath(settings,"serverkey") + "&username=" + vote.get("nickname");
					Integer content2 = Integer.parseInt(HttpRequest.get(url2).body());
					if (content2 == 1){
						out("	Unclaimed");
						String Reward = execute("/give_credits " + vote.get("nickname") + " " + settings.get("votereward"),settings);
						if (Reward.contains("[ERROR] Player not found")){
							out("Player Offline!");
						}else{
							String url3 = "";
							url3 = Json.getPath(settings,"passthrough") 
									+ "https://starmade-servers.com/api/?action=post&object=votes&element=claim&key=" 
									+ Json.getPath(settings,"serverkey") + "&username=" + vote.get("nickname");
							Integer content3 = Integer.parseInt(HttpRequest.get(url3).body());
							if (content3 != 1){
								if (settings.get("revertonclaimfail") == "true"){
								out("Failed Claim! Reverting Reward!");
								execute("/give_credits " + vote.get("nickname") + " -"+settings.get("votereward"),settings);
								}else{
								out("	Failed Claim!");
								}
							}else{
								if (message.toString().length() != 0){
									String uMessage = message.replace("%player", vote.get("nickname").toString());
									execute("/chat " + uMessage, settings);
								}
							}
						}
					}else{
						out("	Already Claimed");
					}
				}
			}
		}
	}
	public static void out(String str, Boolean log){
		System.out.println(str);
		if (log == true){
			//Log
		}
	}
	public static void out(String str){
		out(str,true);
	}
	public static boolean createSettingsFile(){
		File file = new File("./SMVC_settings.json");
		if (!file.exists()) {
			InputStream link = (VoteCheck.class.getClass().getResourceAsStream("/SMVC_settings.json"));
			try {
				Files.copy(link, file.getAbsoluteFile().toPath());
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		out("Created Default Settings File");
		return true;
	}
	public static String execute(String Function, JSONObject settings){
		StarMadeNetUtil u = new StarMadeNetUtil();
		try {
			return u.executeAdminCommand(settings.get("serverhost").toString(),
					Integer.parseInt(settings.get("serverport").toString()),settings.get("adminpassword").toString(), Function);
		} catch (UnknownHostException e1) {
			out("Faied to send command! Unknown Host!");
			return "Faied to send command! Unknown Host!";
		} catch (IOException e1) {
			out("Faied to send command! IO Exception!");
			return "Faied to send command! IO Exception!";
		}
	}
}
