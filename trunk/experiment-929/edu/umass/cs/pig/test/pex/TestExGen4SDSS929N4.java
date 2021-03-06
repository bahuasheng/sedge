package edu.umass.cs.pig.test.pex;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.pig.ExecType;
import org.apache.pig.PigServer;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.impl.PigContext;
import org.apache.pig.newplan.Operator;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestExGen4SDSS929N4 {
	static PigContext pigContext = new PigContext(ExecType.LOCAL,
			new Properties());

	static String PhotoObj, Galaxy, SpecClass, SpecObj, Galaxy2, Galaxy3, PhotoPrimary, Galaxy4, PhotoPrimary2, Galaxy5, PhotoObj2, PhotoObj3;
//	static File filePhotoObj;

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
//		filePhotoObj = File.createTempFile("dataPhotoObj", ".dat");
//		writeData(filePhotoObj);
//		filePhotoObj.deleteOnExit();
		SpecClass = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/10/SpecClassFdr/specClass0.dat" + "'";

		PhotoObj = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/BasicFdr/basic3.dat" + "'";
		Galaxy = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/MergingCondition/GalaxyPair3"  + "'";
		SpecObj = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/specObjIDFdr/specObj3.dat" + "'";
		Galaxy2 = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/multipleCriteria/galaxy2N3.dat" + "'";
		Galaxy3 = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/spatialUnitVectors/galaxy3N3.dat" + "'";
		PhotoPrimary = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/CVs/PhotoPrimary3.dat" + "'";
		Galaxy4 = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/lowzQSOs/Galaxy4N3.dat" + "'";
		PhotoPrimary2 = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/velocitiesErrors/photoPrimary2N3.dat" + "'";
		Galaxy5 = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/100/bETWEEN/Galaxy5N3.dat" + "'";
		PhotoObj2 = "'" + "/home/kaituo/code/pig3/trunk/SDSSRandomData/EmptyFileFolder/part-m-00000" + "'";
//		System.out.println("Test data created.");
	}
	
	/**
	 * SELECT  top 10 objID, field, ra, dec, run from PhotoObj
	 * 
	 * objID	bigint
	 * field	smallint	
	 * ra	float
	 * dec	float
	 * run	smallint
	 * 
	 * objID	       field	    ra	                 dec	     run
	758882625380943288	12	50.7087978370105	76.9631177132159	6074
	758882625380942911	12	50.7990706615098	77.0751444481533	6074
	758882625380942429	12	50.7447936093687	77.0303566953601	6074
	758882625380942275	12	50.7502262046164	76.9432157376269	6074
	758882625380942005	12	50.6069052556651	77.0319862609625	6074
	758882625380941861	12	50.5479970090686	76.9939765386199	6074
	758882625380942008	12	50.6200418736017	77.0308681539771	6074
	758882625380942031	12	50.7019597858277	77.0215462421214	6074
	758882625380942091	12	50.7423458829802	77.0451894855714	6074
	758882625380942193	12	50.8090358164206	77.0809052417237	6074
	 */
	private static void writeData(File dataFile) throws Exception {
		FileOutputStream dat = new FileOutputStream(dataFile);

		dat.write(( 758882625380943288L + "\t" + 12 + "\t" + 50.7087978370105 + "\t" + 76.9631177132159 + "\t" + 6074 + "\n")
					.getBytes());
		dat.write(( 758882625380942911L + "\t" + 12 + "\t" + 50.7990706615098 + "\t" + 77.0751444481533 + "\t" + 6074 + "\n")
				.getBytes());
		dat.write(( 758882625380942429L + "\t" + 12 + "\t" + 50.7447936093687 + "\t" + 77.0303566953601 + "\t" + 6074 + "\n")
				.getBytes());
		dat.write(( 758882625380942275L + "\t" + 12 + "\t" +  50.7502262046164 + "\t" + 76.9432157376269 + "\t" + 6074 + "\n")
				.getBytes());
		dat.write(( 758882625380942005L + "\t" + 12 + "\t" + 50.6069052556651 + "\t" + 77.0319862609625 + "\t" + 6074 + "\n")
				.getBytes());
		dat.write(( 758882625380941861L + "\t" + 12 + "\t" + 50.5479970090686 + "\t" + 76.9939765386199 + "\t" + 6074 + "\n")
				.getBytes());
		dat.write(( 758882625380942008L + "\t" +  12 + "\t" + 50.6200418736017 + "\t" + 77.0308681539771 + "\t" + 6074 + "\n")
				.getBytes());
		dat.write(( 758882625380942031L + "\t" +  12 + "\t" + 50.7019597858277 + "\t" + 77.0215462421214 + "\t" + 6074 + "\n")
				.getBytes());
		dat.write(( 758882625380942091L + "\t" + 12 + "\t" + 50.7423458829802 + "\t" + 77.0451894855714 + "\t" + 6074 + "\n")
				.getBytes());
		dat.write(( 758882625380942193L + "\t" + 12 + "\t" + 50.8090358164206 + "\t" + 77.0809052417237 + "\t" + 6074 + "\n")
				.getBytes());

		dat.close();
	}
	
	/**
	 * -- Provide a list of moving objects consistent with an asteroid.
	-- Also a simple query, but we introduce the 'as' syntax, which allows us to
	-- name derived quantities in the result file.
	
	SELECT
	    objID, 
	    sqrt( power(rowv,2) + power(colv, 2) ) as velocity 
	FROM PhotoObj
	WHERE
    (power(rowv,2) + power(colv, 2)) > 50 
    and rowv >= 0 and colv >=0 
    
    * SELECT TOP 10
  		objID, rowv, colv
		FROM PhotoObj 
		
	* objID	bigint
	* rowv	real
	* colV	real
	* 
	 * @throws Exception
	 */
	@Test
	public void testMovingAsteroids() throws Exception {
		System.out.println("testMovingAsteroids");

		  PigServer pigServer = new PigServer(pigContext);

		  pigServer.registerQuery("A = load " + PhotoObj2
	              + " using PigStorage() as (objID : long, rowv : double, colv : double);");
	      pigServer.registerQuery("B = filter A by  (org.apache.pig.piggybank.evaluation.math.POW(rowv,2.0) + org.apache.pig.piggybank.evaluation.math.POW(colv, 2.0)) > 50.0 and rowv >= 0.0 and colv >=0.0 ;");
	      pigServer.registerQuery("C = foreach B generate org.apache.pig.piggybank.evaluation.math.SQRT(org.apache.pig.piggybank.evaluation.math.POW(rowv,2.0) + org.apache.pig.piggybank.evaluation.math.POW(colv, 2.0)) as velocity;");

	      Map<Operator, DataBag> derivedData = pigServer.getExamples2("C");

	      assertTrue(derivedData != null);
	}
	
//	@Test
//	public void testMovingAsteroids2() throws Exception {
//		  PigServer pigServer = new PigServer(pigContext);
//
//		  pigServer.registerQuery("A = load " + PhotoObj3
//	              + " using PigStorage() as (objID : long, rowv : double, colv : double);");
//	      pigServer.registerQuery("B = filter A by  (org.apache.pig.piggybank.evaluation.math.POW(rowv,2.0) + org.apache.pig.piggybank.evaluation.math.POW(colv, 2.0)) > 50.0 and rowv >= 0.0 and colv >=0.0 ;");
//	      pigServer.registerQuery("C = foreach B generate org.apache.pig.piggybank.evaluation.math.SQRT(org.apache.pig.piggybank.evaluation.math.POW(rowv,2.0) + org.apache.pig.piggybank.evaluation.math.POW(colv, 2.0)) as velocity;");
//
//	      Map<Operator, DataBag> derivedData = pigServer.getExamples2("C");
//
//	      assertTrue(derivedData != null);
//	}

}
