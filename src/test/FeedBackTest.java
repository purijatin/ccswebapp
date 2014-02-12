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

public class FeedBackTest {
	public static void main(String... args) {
		final AtomicInteger count = new AtomicInteger();
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 300; i++) {
			exec.execute(new Runnable() {

				@Override
				public void run() {
					try {
						
						String s = "http://localhost:8080/rest/feedback/fb?";
						s = s+"sessionNum="+(int)(Math.random()*8)+"&text="+Math.random();
						URL url = new URL(s);
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setRequestMethod("GET");
						conn.connect();
						InputStream in = conn.getInputStream();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(in));
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
