package aol.ccs.vote;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedback")
public class Feedback {

	@Autowired
	ServletContext ctx;

	AtomicInteger count = new AtomicInteger(0);

	/**
	 * num is the feedback for the session
	 * 
	 * @param num
	 * @param text
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/fb")
	public @ResponseBody
	FileSystemResource submitFeedback(@RequestParam("sessionNum") String number,
			@RequestParam("text") String text) {
		int num = -1;
		try {
			num = Integer.parseInt(number);
		} catch (NumberFormatException ex) {
			throw new RuntimeException();
		}
		String st = null;
		switch (num) {
		case (1):
			st = ctx.getRealPath(Commons.F_S1);
			break;
		case (2):
			st = ctx.getRealPath(Commons.F_S2);
			break;
		case (3):
			st = ctx.getRealPath(Commons.F_S3);
			break;
		case (4):
			st = ctx.getRealPath(Commons.F_S4);
			break;
		case (5):
			st = ctx.getRealPath(Commons.F_S5);
			break;
		case (6):
			st = ctx.getRealPath(Commons.F_CONF);
			break;
		default:
			st = ctx.getRealPath(Commons.F_GENRAL);
		}
		System.out.println(number+" "+num);
		synchronized(this){
			File f = new File(Commons.RES);
			if(!f.exists())
				f.mkdir();
			f = new File(Commons.FEEDBACK);
			if(!f.exists())
				f.mkdir();
			f = new File(st);
			if(!f.exists())
				f.mkdir();
		}
		st = st+"/"+getDate()+" "+count.incrementAndGet()+".txt";
		Commons.writeToFile(st, text);
		return new FileSystemResource(ctx.getRealPath(Commons.FEEDBACK + "/feedbackresponse.html"));
				
	}
	
	public static String getDate(){
		Calendar cal = Calendar.getInstance();
		StringBuilder ans = new StringBuilder(20);
		ans.append(cal.get(Calendar.DAY_OF_MONTH))
		.append("-")
		.append(cal.get(Calendar.MONTH))
		.append("-")
		.append(cal.get(Calendar.YEAR))
		.append("  ")
		.append(cal.get(Calendar.HOUR_OF_DAY))
		.append("-")
		.append(cal.get(Calendar.MINUTE))
		.append("-")
		.append(cal.get(Calendar.SECOND));
		return ans.toString();
	}
}
