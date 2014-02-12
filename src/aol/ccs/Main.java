package aol.ccs;

import java.io.*;


import java.util.*;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import javax.servlet.ServletContext;


import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/server")
public class Main {
	private AtomicInteger count = new AtomicInteger(10000);

	private final String RES = "/res/";
	private final String feed = "-feedback.txt";
	private final String prop = "-delegate.properties";

	@Autowired
	ServletContext ctx;

	private String name = "name";
	private String designation = "designation";
	private String company = "company";
	private String fax = "fax";
	private String tel = "tel";
	private String mobile = "fax";
	private String email = "email";
	private String website = "website";
	private String streetAddress = "streetAddress";
	private String city = "city";
	private String pinCode = "pinCode";
	private String state = "state";
	private String country = "country";
	private String industry = "industry";
	private String id = "id";
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/checkWorking")
	public @ResponseBody
	String checkWorking() {
		Logger.getRootLogger().log(Priority.DEBUG, "jatin1");
		Logger.getRootLogger().log(Priority.INFO, "jatin2");
		Logger.getRootLogger().log(org.apache.log4j.Level.DEBUG, "jatin3");
		Logger.getRootLogger().info("jatin4");
		Logger.getRootLogger().warn("jatin5");
		Logger.getRootLogger().fatal("jatin6");
		Logger.getLogger(Main.class.getName()).fatal("check");
		return "Working";
	}

	/**
	 * http://localhost:8080/CCSApp/rest/server/register?name=jatin&designation=
	 * a&company=b&fax=fax&tel=tel&mobile=mobile&email=email&website=w&
	 * streetAddress=s&city=c&pinCode=p&state=e&country=c&industry=i
	 * 
	 * If null, then submit "-na-"
	 * 
	 * @param name
	 * @param designation
	 * @param company
	 * @param fax
	 * @param tel
	 * @param mobile
	 * @param email
	 * @param website
	 * @param streetAddress
	 * @param city
	 * @param pinCode
	 * @param State
	 * @param country
	 * @param industry
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/register")
	public @ResponseBody
	synchronized String register(@RequestParam("id") String id,
			@RequestParam("name") String name,
			@RequestParam("designation") String designation,
			@RequestParam("company") String company,
			@RequestParam("fax") String fax, @RequestParam("tel") String tel,
			@RequestParam("mobile") String mobile,
			@RequestParam("email") String email,
			@RequestParam("website") String website,
			@RequestParam("streetAddress") String streetAddress,
			@RequestParam("city") String city,
			@RequestParam("pinCode") String pinCode,
			@RequestParam("state") String State,
			@RequestParam("country") String country,
			@RequestParam("industry") String industry) {
		
		System.out.println("dfdfdfdfd. jatin");
		Logger.getRootLogger().log(Priority.DEBUG, "jatin1");
		Logger.getRootLogger().log(Priority.INFO, "jatin2");
		Logger.getRootLogger().log(org.apache.log4j.Level.DEBUG, "jatin3");
		Logger.getRootLogger().info("jatin4");
		Logger.getRootLogger().warn("jatin5");
		Logger.getRootLogger().fatal("jatin6");
		int c = count.incrementAndGet();
		Properties p = new Properties();
		p.setProperty(this.name, name);
		p.setProperty(this.designation, designation);
		p.setProperty(this.company, company);
		p.setProperty(this.tel, tel);
		p.setProperty(this.mobile, mobile);
		p.setProperty(this.email, email);
		p.setProperty(this.website, website);
		p.setProperty(this.city, city);
		p.setProperty(this.pinCode, pinCode);
		p.setProperty(this.state, state);
		p.setProperty(this.country, country);
		p.setProperty(this.industry, industry);
		p.setProperty(this.streetAddress, streetAddress);
		p.setProperty(this.id, id);
		p.setProperty(this.fax, fax);
		File f = new File(ctx.getRealPath(this.RES + id + this.prop));
		if (f.exists())
			return "Already Registered.";
		OutputStream s;
		try {
			f.createNewFile();
			s = new FileOutputStream(f);
			p.store(s, "" + new Date());
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Registered the user: " + id;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/delegateList")
	public @ResponseBody
	String getAllDelegateJSON() {
		JSONArray json = new JSONArray();
		File[] files = getFiles();
		System.out.println(Arrays.asList(files));
		for (File f : files) {
			JSONObject o = new JSONObject();
			Properties p = loadProperties(f);
			if (p == null)
				continue;
			o.put(this.city, p.get(this.city));
			o.put(this.name, p.get(this.name));
			o.put(this.designation, p.get(this.designation));
			o.put(this.company, p.get(this.company));
			o.put(this.tel, p.get(this.tel));
			o.put(this.mobile, p.get(this.mobile));
			o.put(this.email, p.get(this.email));
			o.put(this.pinCode, p.get(this.pinCode));
			o.put(this.country, p.get(this.country));
			o.put(this.industry, p.get(this.industry));
			o.put(this.id, p.get(this.id));
			o.put(this.streetAddress, p.get(this.streetAddress));
			o.put(this.website, p.get(this.website));
			o.put(this.state, p.get(this.state));
			o.put(this.fax, p.get(this.fax));
			json.add(o);
		}
		return json.toJSONString();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/unregister")
	public @ResponseBody
	synchronized String unregister(@RequestParam("id") String id) {
		File f = new File(ctx.getRealPath(this.RES + id + this.prop));
		if (f.exists()) {
			f.delete();
			return id + " unregistered";
		} else
			return "user did not register";
	}

	/**
	 * true if successful else false
	 * 
	 * @param from
	 * @param to
	 * @param text
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sendMail")
	public @ResponseBody
	synchronized String sendMail(@RequestParam("idFrom") String from,
			@RequestParam("idTo") String to, @RequestParam("text") String text) {
		Delegate ff = null;
		Delegate tt = null;
		synchronized (this) {
			File f = new File(ctx.getRealPath(this.RES + from + this.prop));
			if (!f.exists())
				return false + "";
			File t = new File(ctx.getRealPath(this.RES + to + this.prop));
			if (!t.exists())
				return false + "";
			ff = loadDelegate(f);
			tt = loadDelegate(t);
		}

		return SendMail.sendMail(ff, tt, text) + "";
	}

	private File[] getFiles() {
		final Main m = this;
		File[] f = new File(ctx.getRealPath(this.RES))
				.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						// TODO Auto-generated method stub
						if (pathname.getName().endsWith(m.prop))
							return true;
						else
							return false;
					}
				});
		return f;
	}

	private Properties loadProperties(File f) {
		if (f == null)
			throw new NullPointerException();
		Properties p = new Properties();
		try {
			InputStream i = new FileInputStream(f);
			p.load(i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return p;
	}

	private Delegate loadDelegate(File f) {
		Delegate d = new Delegate();
		Properties p = loadProperties(f);
		d.city = p.getProperty(city);
		d.company = p.getProperty(company);
		d.country = p.getProperty(country);
		d.designation = p.getProperty(designation);
		d.email = p.getProperty(email);
		d.fax = p.getProperty(fax);
		d.id = p.getProperty(id);
		d.industry = p.getProperty(industry);
		d.mobile = p.getProperty(mobile);
		d.name = p.getProperty(name);
		d.pinCode = p.getProperty(pinCode);
		d.state = p.getProperty(state);
		d.streetAddress = p.getProperty(streetAddress);
		d.tel = p.getProperty(tel);
		d.website = p.getProperty(website);
		return d;
	}

	private Object feedbackLock = new Object();

	@RequestMapping(method = RequestMethod.GET, value = "/feedback")
	public @ResponseBody
	String feedback(@RequestParam("id") String id,
			@RequestParam("feedback") String feedback) {
		File f = null;
		synchronized (feedbackLock) {
			f = new File(ctx.getRealPath(this.RES + id + this.feed));
			if (f.exists()) {
				int i = 2;
				for (;;) {
					f = new File(ctx.getRealPath(this.RES + id + "_" + i
							+ this.feed));
					if (f.exists())
						i++;
					else
						break;
				}
			}
		}

		return (writeToaFile(f, feedback)) ? "success"
				: "could not submit feeback";

	}

	private boolean writeToaFile(File f, String s) {
		try {
			FileWriter w = new FileWriter(f);
			w.write(s);
			w.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
