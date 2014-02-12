package aol.ccs.vote;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.management.RuntimeErrorException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vote")
public class Vote {

	/**
	 * Question along with options string
	 */
	private volatile String jsonString;
	/**
	 * If a person with id has voted or not
	 */
	private Map<String, Boolean> idMap = new ConcurrentHashMap<String, Boolean>();

	/**
	 * if true, voting on. Else false.
	 */
	private volatile boolean votingOn = false;

	private volatile AtomicIntegerArray array;
	private static Logger log = Logger.getLogger(Vote.class.getName());
	
	/**
	 * to be only used by /question and /removeQ
	 */
	private volatile String question;
	private volatile List<String> options = new ArrayList<String>();

	@Autowired
	ServletContext ctx;

	/**
	 * Post question.
	 * 
	 * @param question
	 * @param number
	 * @param options
	 *            . To be split by ____
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/question")
	public @ResponseBody
	synchronized String postQuestion(@RequestParam("q") String question,
			@RequestParam("num") String number,
			@RequestParam("options") String options) {
		log("postQuestion", question, number, options);
		removeQ();
		int num = -1;
		try {
			num = Integer.parseInt(number);
		} catch (NumberFormatException ex) {
			return "failure";
		}
		this.question = question;
		this.array = new AtomicIntegerArray(num);
		JSONObject ob = new JSONObject();
		ob.put("question", question);
		ob.put("num", number);
		ob.put("status", "on");
		String[] in = options.split("____");
		for (int i = 0; i < in.length; i++) {
			ob.put("option" + (i + 1), in[i]);
			this.options.add(in[i]);
		}
		this.jsonString = ob.toJSONString();
		this.votingOn = true;
		log("postQuestion", this.jsonString);
		try {
			Commons.writeToFile(ctx.getRealPath(Commons.VOTE_DIR) + "/"
					+ question + ".txt", this.jsonString);
		} 
		catch(Exception ex){
			log(this.jsonString);
			Commons.writeToFile(ctx.getRealPath(Commons.VOTE_DIR) + "/"
					+ Feedback.getDate() + ".txt", this.jsonString);
		}
		finally {
			return this.jsonString;
		}

	}

	/**
	 * 
	 * @return JSON string if voting on. Else "false" if voting closed
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getQ")
	public @ResponseBody
	String getQ() {
		log("getQ");
		if (votingOn)
			return jsonString;
		else {
			JSONObject o = new JSONObject();
			o.put("status", "off");
			return o.toJSONString();
		}
	}

	/**
	 * Returns false if already voted. true if vote taken successfully. Returns
	 * "closed" if voting is closed
	 * 
	 * @param id
	 * @param option
	 *            . An integer. starting from 1
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v")
	public @ResponseBody
	String vote(@RequestParam("id") String id,
			@RequestParam("option") String option) {
		log("/v",id,option);
		if (!this.votingOn)
			return "off";
		if (idMap.get(id) != null)
			return "alreadyVoted";
		int num = -1;
		try {
			num = Integer.parseInt(option) - 1;
		} catch (NumberFormatException ex) {
			return "Invalid Request";
		}
		if (num < 0)
			return "Invalid Request";
		this.array.getAndIncrement(num);
		idMap.put(id, true);
		return "on";

	}

	private String getJSON(String key, String value) {
		JSONObject o = new JSONObject();
		o.put(key, value);
		return o.toJSONString();
	}

	/**
	 * true if removed else false
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/removeQ")
	public @ResponseBody
	synchronized String removeQ() {
		try {
			Commons.writeToFile(ctx.getRealPath(Commons.VOTE_DIR) + "/"
					+ question + "-ans.txt",getCount());
			Commons.writeToFile(ctx.getRealPath(Commons.VOTE_DIR) + "/"
					+ question + "-ans2.txt",getResult());
		} 
		catch(Exception ex){
			log(this.jsonString);
			Commons.writeToFile(ctx.getRealPath(Commons.VOTE_DIR) + "/"
					+ Feedback.getDate() + "-ans.txt", getCount());
		}
		question = null;
		this.options.clear();
		votingOn = false;
		this.array = null;
		this.idMap.clear();
		return true + "";
	}

	/**
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/newResult")
	public @ResponseBody
	String getResult() {
		if (this.array == null)
			return "Voting Still not On";
		JSONObject ans = new JSONObject();
		ans.put("q", this.question);
		int sum = 0;
		for (int i = 0; i < this.array.length(); i++) {
			sum = sum +this.array.get(i);
		}
		JSONArray options = new JSONArray();
		for (int i = 1; i <= this.array.length(); i++) {
			JSONObject o = new JSONObject();
			o.put("option", this.options.get(i-1));
			o.put("votes", (array.get(i - 1)/sum)*100);
			//o.put(this.options.get(i-1), array.get(i - 1));
			options.add(o);
		}
		ans.put("options", options);
		log("getCount", ans.toJSONString());
		return ans.toJSONString();
	}
	
	/**
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/result")
	public @ResponseBody
	String getCount() {
		if (this.array == null)
			return "Voting Still not On";
		JSONObject ans = new JSONObject();
		ans.put("question", this.question);
		for (int i = 1; i <= this.array.length(); i++) {
			ans.put(this.options.get(i-1), array.get(i - 1));
		}
		log("getCount", ans.toJSONString());
		return ans.toJSONString();
	}

	/**
	 * Logs the incoming parameters. First element is the emthod name
	 * 
	 * @param args
	 */
	private void log(String... args) {
		log.info(Arrays.asList(args));
	}
	
	/**
	 * generates the unique number for each request 
	 */
	AtomicInteger aadhar = new AtomicInteger((int)(Math.random()*100000));
	
	@RequestMapping(method = RequestMethod.GET, value = "/aadhar")
	public @ResponseBody String getAadhar(){
		log.info("aadhar is "+(aadhar.get()+10));
		return getJSON("uid", aadhar.addAndGet(10)+"");
	}
	
	

}
