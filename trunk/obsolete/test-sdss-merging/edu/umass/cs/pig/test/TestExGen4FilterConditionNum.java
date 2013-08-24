package edu.umass.cs.pig.test;

import static org.junit.Assert.assertTrue;

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

public class TestExGen4FilterConditionNum {
	static PigContext pigContext = new PigContext(ExecType.LOCAL,
			new Properties());

	static String GalaxyPair1, GalaxyPair2, GalaxyPair3, GalaxyPair4, neighbors;
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
		/**
		 * select top 10 objid from Galaxy
		 */
		GalaxyPair1 = "'" + "/home/kaituo/code/pig3/trunk/SDSSRealData/GalaxyPair1" + "'";
		GalaxyPair2 = "'" + "/home/kaituo/code/pig3/trunk/SDSSRealData/GalaxyPair2" + "'";
		
		neighbors = "'" + "/home/kaituo/code/pig3/trunk/SDSSRealData/neighbors" + "'";
		
	}
	
	/**
	 * You can always inspect the INFORMATION_SCHEMA views to find all the interesting information about tables and their columns.
	 *
	 *	It's not called "describe" per se - but this query will show you lots of information:
	 * select * from Information_schema.Columns
		where table_name = 'PhotoObj'

	Galaxy g1:
		objid	bigint
		petrorad_u	real
		petrorad_g	real
		petrorad_r	real
		petrorad_i	real	
		petrorad_z	real
		petroRadErr_g	real
		petroMag_g	real
		modelMag_u	real
		modelMag_r	real
		modelMag_i	real
		modelMag_z	real
		petroR50_r	real
		
	Neighbors:
		objID	bitint
		NeighborObjID	bigint
		neighborType	smallint
		distance	float
		
	Galaxy g2:
		objID	bigint
		ra	float
		dec	float
		modelMag_u	real
		modelMag_g	real
		modelMag_r	real
		modelMag_i	real
		modelMag_z	real
		petroR50_r	real
		petrorad_u	real
		petrorad_g	real
		petrorad_r	real
		petrorad_i	real	
		petrorad_z	real
		petroRadErr_g	real
		
		
		SELECT  top 10 objID, ra, dec, modelMag_u, modelMag_g, modelMag_r, modelMag_i, modelMag_z, petroR50_r, petrorad_u, petrorad_g, petrorad_r, petrorad_i, petrorad_z, petroRadErr_g from Galaxy
	 	SELECT top 10 objID, NeighborObjID, neighborType, distance from Neighbors
	 */
	@Test
	public void testFilter0() throws Exception {

		PigServer pigServer = new PigServer(pigContext);

		pigServer.registerQuery("G1 = load " + GalaxyPair1
				+ " using PigStorage() as (objID1 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("G2 = load " + GalaxyPair2
				+ " using PigStorage() as (objID2 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("N = load " + neighbors
				+ " using PigStorage() as (objID3 : long, NeighborObjID : long, neighborType : int, distance : float);");
		pigServer.registerQuery("D = join N by objID3, G1 by objID1;");
		pigServer.registerQuery("E = join D by NeighborObjID, G2 by objID2;");

		Map<Operator, DataBag> derivedData = pigServer.getExamples2("E");

		assertTrue(derivedData != null);
	}//
	
	@Test
	public void testFilter1() throws Exception {

		PigServer pigServer = new PigServer(pigContext);

		pigServer.registerQuery("G1 = load " + GalaxyPair1
				+ " using PigStorage() as (objID1 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("G2 = load " + GalaxyPair2
				+ " using PigStorage() as (objID2 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("N = load " + neighbors
				+ " using PigStorage() as (objID3 : long, NeighborObjID : long, neighborType : int, distance : float);");
		pigServer.registerQuery("D = join N by objID3, G1 by objID1;");
		pigServer.registerQuery("E = join D by NeighborObjID, G2 by objID2;");
		pigServer.registerQuery("F = filter E by objID1 < objID2;");//D::G1::objID < D::G2::objID


		Map<Operator, DataBag> derivedData = pigServer.getExamples2("F");

		assertTrue(derivedData != null);
	}//
	
	@Test
	public void testFilter2() throws Exception {

		PigServer pigServer = new PigServer(pigContext);

		pigServer.registerQuery("G1 = load " + GalaxyPair1
				+ " using PigStorage() as (objID1 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("G2 = load " + GalaxyPair2
				+ " using PigStorage() as (objID2 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("N = load " + neighbors
				+ " using PigStorage() as (objID3 : long, NeighborObjID : long, neighborType : int, distance : float);");
		pigServer.registerQuery("D = join N by objID3, G1 by objID1;");
		pigServer.registerQuery("E = join D by NeighborObjID, G2 by objID2;");
		pigServer.registerQuery("F = filter E by objID1 < objID2 and neighborType == 3;");//D::G1::objID < D::G2::objID


		Map<Operator, DataBag> derivedData = pigServer.getExamples2("F");

		assertTrue(derivedData != null);
	}//
	
	@Test
	public void testFilter3() throws Exception {

		PigServer pigServer = new PigServer(pigContext);

		pigServer.registerQuery("G1 = load " + GalaxyPair1
				+ " using PigStorage() as (objID1 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u1 : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("G2 = load " + GalaxyPair2
				+ " using PigStorage() as (objID2 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u2 : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("N = load " + neighbors
				+ " using PigStorage() as (objID3 : long, NeighborObjID : long, neighborType : int, distance : float);");
		pigServer.registerQuery("D = join N by objID3, G1 by objID1;");
		pigServer.registerQuery("E = join D by NeighborObjID, G2 by objID2;");
		pigServer.registerQuery("F = filter E by objID1 < objID2 and neighborType == 3 and petrorad_u1 > 0.0;");//D::G1::objID < D::G2::objID


		Map<Operator, DataBag> derivedData = pigServer.getExamples2("F");

		assertTrue(derivedData != null);
	}//
	
	@Test
	public void testFilter4() throws Exception {

		PigServer pigServer = new PigServer(pigContext);

		pigServer.registerQuery("G1 = load " + GalaxyPair1
				+ " using PigStorage() as (objID1 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u1 : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("G2 = load " + GalaxyPair2
				+ " using PigStorage() as (objID2 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u2 : float, petrorad_g : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("N = load " + neighbors
				+ " using PigStorage() as (objID3 : long, NeighborObjID : long, neighborType : int, distance : float);");
		pigServer.registerQuery("D = join N by objID3, G1 by objID1;");
		pigServer.registerQuery("E = join D by NeighborObjID, G2 by objID2;");
		pigServer.registerQuery("F = filter E by objID1 < objID2 and neighborType == 3 and petrorad_u1 > 0.0 and petrorad_u2 > 0.0;");//D::G1::objID < D::G2::objID


		Map<Operator, DataBag> derivedData = pigServer.getExamples2("F");

		assertTrue(derivedData != null);
	}//
	
	@Test
	public void testFilter5() throws Exception {

		PigServer pigServer = new PigServer(pigContext);

		pigServer.registerQuery("G1 = load " + GalaxyPair1
				+ " using PigStorage() as (objID1 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u1 : float, petrorad_g1 : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("G2 = load " + GalaxyPair2
				+ " using PigStorage() as (objID2 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u2 : float, petrorad_g2 : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("N = load " + neighbors
				+ " using PigStorage() as (objID3 : long, NeighborObjID : long, neighborType : int, distance : float);");
		pigServer.registerQuery("D = join N by objID3, G1 by objID1;");
		pigServer.registerQuery("E = join D by NeighborObjID, G2 by objID2;");
		pigServer.registerQuery("F = filter E by objID1 < objID2 and neighborType == 3 and petrorad_u1 > 0.0 and petrorad_u2 > 0.0 and petrorad_g1 > 0.0;");//D::G1::objID < D::G2::objID


		Map<Operator, DataBag> derivedData = pigServer.getExamples2("F");

		assertTrue(derivedData != null);
	}//
	
	@Test
	public void testFilter6() throws Exception {

		PigServer pigServer = new PigServer(pigContext);

		pigServer.registerQuery("G1 = load " + GalaxyPair1
				+ " using PigStorage() as (objID1 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u1 : float, petrorad_g1 : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("G2 = load " + GalaxyPair2
				+ " using PigStorage() as (objID2 : long, ra : float, dec : float, modelMag_u : float, modelMag_g : float, modelMag_r : float, modelMag_i : float, modelMag_z : float, petroR50_r : float, petrorad_u2 : float, petrorad_g2 : float, petrorad_r : float, petrorad_i : float, petrorad_z : float, petroRadErr_g : float);");
		pigServer.registerQuery("N = load " + neighbors
				+ " using PigStorage() as (objID3 : long, NeighborObjID : long, neighborType : int, distance : float);");
		pigServer.registerQuery("D = join N by objID3, G1 by objID1;");
		pigServer.registerQuery("E = join D by NeighborObjID, G2 by objID2;");
		pigServer.registerQuery("F = filter E by objID1 < objID2 and neighborType == 3 and petrorad_u1 > 0.0 and petrorad_u2 > 0.0 and petrorad_g1 > 0.0 and petrorad_g2 > 0.0;");//D::G1::objID < D::G2::objID


		Map<Operator, DataBag> derivedData = pigServer.getExamples2("F");

		assertTrue(derivedData != null);
	}//
	
	

}
