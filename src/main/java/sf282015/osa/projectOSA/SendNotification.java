package sf282015.osa.projectOSA;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// https://stackoverflow.com/questions/37876257/push-notification-works-incorrectly-when-app-is-on-background-or-not-running/37876727#37876727
public class SendNotification {

	private final static String apiKey = "AIzaSyC5htftRsCubYF8aM5-QOmmVEQTYStlUPs";
	private final static String fbUrl = "https://fcm.googleapis.com/fcm/send";
	
	
	public static void sendNotification(String title, String message, 
			List<String> devices, long auctionId) throws Exception{
		if(devices.isEmpty()) return;
		
		URL url = new URL(fbUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization","key=" + apiKey);
		conn.setRequestProperty("Content-Type","application/json");
		
		JSONObject json = new JSONObject();
		
//		JSONObject info = new JSONObject();
//		info.put("title", title); // Notification title
//		info.put("body", message); // Notification body
		
		JSONObject data = new JSONObject();
		data.put("auctionId", auctionId);
		data.put("title", title); // Notification title
		data.put("body", message); // Notification body
		
		

		JSONArray array = new JSONArray();
		for(String s : devices){
			array.add(s);
		}
		
		json.put("data", data);
		json.put("registration_ids", array);
//		json.put("notification", info);
		
		System.out.println(json);
		
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		conn.getInputStream();
		
	}
	
}
