package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class VoteTest {
	
	static String vote = "http://localhost:8080/rest/vote/v?";
	
	public static void main(String... args) {
		final AtomicInteger count = new AtomicInteger();
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 500; i++) {
			final int j = ((i%4)==0?4:(i%4));
			
			exec.execute(new Runnable() {

				@Override
				public void run() {
					try {
						
						String s = vote;
						s = s+"id="+(int)(Math.random()*800000)+"&option="+j;
						URL url = new URL(s);
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setRequestMethod("GET");
						conn.connect();
						InputStream in = conn.getInputStream();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(in));
						String text = reader.readLine();
						if(text.equals("false"))
							System.out.println("false");

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
