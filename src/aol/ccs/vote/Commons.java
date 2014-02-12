package aol.ccs.vote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Commons {

	public static final String RES = "/res/";
	public static final String FEEDBACK = RES+"feedback/";
	/**
	 * Feedback directory for session 1
	 */
	public static final String F_S1 = FEEDBACK+"session1/";
	public static final String F_S2 = FEEDBACK+"session2/";
	public static final String F_S3 = FEEDBACK+"session3/";
	public static final String F_S4 = FEEDBACK+"session4/";
	public static final String F_S5 = FEEDBACK+"session5/";
	public static final String F_GENRAL = FEEDBACK+"general/";
	public static final String F_CONF = FEEDBACK+"conference/";;
	public static final String VOTE_DIR = RES+"votes/";
	public static final String DETAILS = RES+"details/";
	public static final String SPEAKERS = RES+"speaker/";
	
	public static void writeToFile(String filePath, String content){
		File f = new File(filePath);
		if(f.exists())
			throw new IllegalArgumentException("This file already exists");
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			w.write(content);
			w.close();
		} catch (IOException e) {
			Logger.getLogger(Commons.class.getName()).fatal(e);
		}
	}
	
}
