package aol.ccs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aol.ccs.vote.Commons;

@Controller
@RequestMapping("/ssummary")
public class Speakers {

	@Autowired
	ServletContext ctx;
	
	@RequestMapping(method = RequestMethod.GET, value="/get")
	public @ResponseBody
	synchronized String get(@RequestParam("id") String id) {
		File f = new File(ctx.getRealPath(Commons.SPEAKERS)+"/"+id+".txt");
		if(!f.exists()){
			return "Summary Not Available";
		}else{
			StringBuilder ans = new StringBuilder(100);
			try {
				BufferedReader r = new BufferedReader(new FileReader(f));
				String g = null;
				while((g=r.readLine())!=null){
					ans = ans.append(g);
				}
				return ans.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logger.getLogger(Speakers.class.getName()).fatal(e);
				return "Summary Not Available";
			}
			
			
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/submit")
	public @ResponseBody
	String submit(@RequestParam("id") String id,@RequestParam("text") String text) {
		Logger.getLogger(Speakers.class).info(id+" id. text "+text);
		File f = new File(ctx.getRealPath(Commons.SPEAKERS)+"/"+id+".txt");
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			w.write(text);
			w.close();
			return "success";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(Speakers.class.getName()).fatal(e);
			return "Summary Not Available";
		}
		
	}
}
