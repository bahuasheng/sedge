package edu.umass.cs.pig.test.runtime.pigmix;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.pig.ExecType;
import org.apache.pig.PigServer;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.impl.PigContext;
import org.apache.pig.newplan.Operator;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.umass.cs.pig.test.util.RFile;

public class TestExGen4PigMixReal4 {
	static PigContext pigContext = new PigContext(ExecType.LOCAL, new Properties());
	static int MAX = 100;
	static int MIN = 10;
	static String widerow, widerowX, page_viewsX, power_usersX, usersX, data12X, users_sortedX, power_users_sampleX, widegroupbydataX;
//	static  File fileWiderowX;
//  page_views, , power_users
	static String pScalability;
// pPage_Views, , pPower_Users
	public static Logger logger = Logger.getLogger("MyLog");
	public static FileHandler fh;
	
	{
		try {
			pigContext.connect();
		} catch (ExecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@BeforeClass
	public static void oneTimeSetup() throws Exception {
		try {
			RFile.deleteIfExists("MyLogFile.log");
	        fh = new FileHandler("MyLogFile.log", true);
	        logger.addHandler(fh);
			logger.setLevel(Level.FINE);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			fh.setLevel(Level.FINE);
        } catch (SecurityException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		
		
		
//		widerowX = "'" + fileWiderowX.getPath() + "'";
		widerowX = "/home/kaituo/code/pig3/trunk/PigmixRandomData/100/widerow2X/scalabilityData9";
		
//		writePage_ViewsData(page_views, page_viewsX, pPage_Views);

//		System.out.println("widerowX : " + widerowX + "\n");
//	    System.out.println("Test data created.");
	}
	
	private static void writeScalabilityData() {
		int n = 32;

		String abody = "";
		for (int i = 0; i < n; i++) {
			if (i > 0)
				abody = abody + ", ";
			abody = abody + "c" + i + ": int";
		}

		String cbody = "";
		for (int j = 0; j < n; j++) {
			if (j > 0)
				cbody = cbody + ", ";
			cbody = cbody + "SUM(A.c" + j + ") as c" + j;
		}

		String dbody = "";
		for (int k = 0; k < n; k++) {
			if (k > 0)
				dbody = dbody + " and ";
			dbody = dbody + "c" + k + " > 100";
		}
		
		System.out.println(abody + "\n" + cbody + "\n" + dbody);
	}
	
	public static void main(String[] args) {
		writeScalabilityData();
	}
	
	/**
	 * 
	 * c0: int, c1: int, c2: int, c3: int, c4: int, c5: int, c6: int, c7: int, c8: int, c9: int, c10: int, c11: int, c12: int, c13: int, c14: int, c15: int, c16: int, c17: int, c18: int, c19: int, c20: int, c21: int, c22: int, c23: int, c24: int, c25: int, c26: int, c27: int, c28: int, c29: int, c30: int, c31: int
	 *  SUM(A.c0) as c0, SUM(A.c1) as c1, SUM(A.c2) as c2, SUM(A.c3) as c3, SUM(A.c4) as c4, SUM(A.c5) as c5, SUM(A.c6) as c6, SUM(A.c7) as c7, SUM(A.c8) as c8, SUM(A.c9) as c9, SUM(A.c10) as c10, SUM(A.c11) as c11, SUM(A.c12) as c12, SUM(A.c13) as c13, SUM(A.c14) as c14, SUM(A.c15) as c15, SUM(A.c16) as c16, SUM(A.c17) as c17, SUM(A.c18) as c18, SUM(A.c19) as c19, SUM(A.c20) as c20, SUM(A.c21) as c21, SUM(A.c22) as c22, SUM(A.c23) as c23, SUM(A.c24) as c24, SUM(A.c25) as c25, SUM(A.c26) as c26, SUM(A.c27) as c27, SUM(A.c28) as c28, SUM(A.c29) as c29, SUM(A.c30) as c30, SUM(A.c31) as c31
     *  c0 > 100 and c1 > 100 and c2 > 100 and c3 > 100 and c4 > 100 and c5 > 100 and c6 > 100 and c7 > 100 and c8 > 100 and c9 > 100 and c10 > 100 and c11 > 100 and c12 > 100 and c13 > 100 and c14 > 100 and c15 > 100 and c16 > 100 and c17 > 100 and c18 > 100 and c19 > 100 and c20 > 100 and c21 > 100 and c22 > 100 and c23 > 100 and c24 > 100 and c25 > 100 and c26 > 100 and c27 > 100 and c28 > 100 and c29 > 100 and c30 > 100 and c31 > 100
	 */
	@Test
	  public void testScalability() throws Exception {
	      PigServer pigServer = new PigServer(pigContext);
	      pigServer.setBatchOn();
	      pigServer.registerQuery("A = load '" + widerowX.toString() + "/part-m-00000' using PigStorage() as (name: chararray, c0: int, c1: int, c2: int, c3: int, c4: int, c5: int, c6: int, c7: int, c8: int, c9: int, c10: int, c11: int, c12: int, c13: int, c14: int, c15: int, c16: int, c17: int, c18: int, c19: int, c20: int, c21: int, c22: int, c23: int, c24: int, c25: int, c26: int, c27: int, c28: int, c29: int, c30: int);");
	      pigServer.registerQuery("B = group A by name;");
	      pigServer.registerQuery("C = foreach B generate group, SUM(A.c0) as c0, SUM(A.c1) as c1, SUM(A.c2) as c2, SUM(A.c3) as c3, SUM(A.c4) as c4, SUM(A.c5) as c5, SUM(A.c6) as c6, SUM(A.c7) as c7, SUM(A.c8) as c8, SUM(A.c9) as c9, SUM(A.c10) as c10, SUM(A.c11) as c11, SUM(A.c12) as c12, SUM(A.c13) as c13, SUM(A.c14) as c14, SUM(A.c15) as c15, SUM(A.c16) as c16, SUM(A.c17) as c17, SUM(A.c18) as c18, SUM(A.c19) as c19, SUM(A.c20) as c20, SUM(A.c21) as c21, SUM(A.c22) as c22, SUM(A.c23) as c23, SUM(A.c24) as c24, SUM(A.c25) as c25, SUM(A.c26) as c26, SUM(A.c27) as c27, SUM(A.c28) as c28, SUM(A.c29) as c29, SUM(A.c30) as c30;");
	      pigServer.registerQuery("D = filter C by c0 > 100 and c1 > 100 and c2 > 100 and c3 > 100 and c4 > 100 and c5 > 100 and c6 > 100 and c7 > 100 and c8 > 100 and c9 > 100 and c10 > 100 and c11 > 100 and c12 > 100 and c13 > 100 and c14 > 100 and c15 > 100 and c16 > 100 and c17 > 100 and c18 > 100 and c19 > 100 and c20 > 100 and c21 > 100 and c22 > 100 and c23 > 100 and c24 > 100 and c25 > 100 and c26 > 100 and c27 > 100 and c28 > 100 and c29 > 100 and c30 > 100;");//and c31 > 100
	      //pigServer.registerQuery("store D into '" +  out.getAbsolutePath() + "';");
	      Map<Operator, DataBag> derivedData = pigServer.getExamples("D");
	  
	      assertTrue(derivedData != null);
	  }

}
