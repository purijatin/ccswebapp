package test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FeedbackCheck {
	public static void main(String... args){
		final AtomicInteger count = new AtomicInteger();
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0;i<100;i++){
			exec.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						URL url = new URL("http://ec2-174-129-64-178.compute-1.amazonaws.com:8080/CCS_App/rest/server/register?name=jatin&designation=a&company=b&fax=fax&tel=tel&mobile=mobile&email=email&website=w&streetAddress=s&city=c&pinCode=p&state=e&country=c&industry=i&id=j");
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						conn.connect();
						InputStream in = conn.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						String text = reader.readLine();
						System.out.println(text);

						conn.disconnect();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(count.incrementAndGet());
					}
					
				}
			});
		}
		exec.shutdown();
	}
}
