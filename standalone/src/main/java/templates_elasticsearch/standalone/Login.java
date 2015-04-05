package templates_elasticsearch.standalone;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login {
	
	public static void main(String [] args){
		
		Login login = new Login();
		if(login.doLogin("lhzsantana@hotmail.com", "1")) System.out.println("OK");
		else  System.out.println("Failed");
	}
	

	public boolean doLogin(String email, String password) {

		HttpURLConnection connection = null;
		
		try {
			String urlParameters="email="+email+"&password="+password;
			System.out.println(urlParameters);
			URL url;
						
			// Create connection
			url = new URL("http://198.23.188.82/randomnumberlist/login.php");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			System.out.println(response.toString());
			
			
			return response.toString().contains("OK")?true:false;

		} catch (Exception e) {

			e.printStackTrace();
			return false;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}

	}
}
