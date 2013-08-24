package edu.umass.cs.pig.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.pig.ExecType;
import org.apache.pig.PigServer;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.impl.PigContext;
import org.apache.pig.newplan.Operator;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestExGen4PigMix {
	static PigContext pigContext = new PigContext(ExecType.LOCAL, new Properties());

//  private OSValidator z3 = OSValidator.get();
//  private Z3Context z4 = Z3Context.get();
  static int MAX = 100;
  static int MIN = 10;
  static String A, B, C, D, E, F, G, J, K, L, M, N, O, P, Q, R, S, T;
  static int start = 0;
  static  File fileA, fileB, fileC, fileD, fileE, fileF, fileG, fileJ, fileK, fileL, fileM, fileN, fileO, fileP, fileQ, fileR, fileS, fileT;
  
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
     

      fileA = File.createTempFile("dataA", ".dat");
      fileB = File.createTempFile("dataB", ".dat");
      fileC = File.createTempFile("dataC", ".dat");
      fileD = File.createTempFile("dataD", ".dat");
      fileE = File.createTempFile("dataE", ".dat");
      fileF = File.createTempFile("dataF", ".dat");
      fileG = File.createTempFile("dataG", ".dat");
      fileJ = File.createTempFile("dataJ", ".dat");
      fileK = File.createTempFile("dataK", ".dat");
      fileL = File.createTempFile("dataL", ".dat");
      fileM = File.createTempFile("dataM", ".dat");
      fileN = File.createTempFile("dataN", ".dat");
      fileO = File.createTempFile("dataO", ".dat");
      fileP = File.createTempFile("dataP", ".dat");
      fileQ = File.createTempFile("dataQ", ".dat");
      fileR = File.createTempFile("dataR", ".dat");
      fileS = File.createTempFile("dataS", ".dat");
      fileT = File.createTempFile("dataT", ".dat");
      
      writeData(fileA);
      writeData(fileB);
      writeData2(fileC);
      writeData3(fileD);
      writeData3(fileE);
      writeData4(fileF);
      writeData4(fileG);
      writeData8(fileJ);
      writeData7(fileK);
      writeData9(fileL);
      writeData7(fileM);
      writeData10(fileN);
      writeData11(fileO);
      writeData12(fileP);
      writeData13(fileQ);
      writeData14(fileR);
      writeData15(fileS);
      writeData16(fileT);

      fileA.deleteOnExit();
      fileB.deleteOnExit();
      fileC.deleteOnExit();
      fileD.deleteOnExit();
      fileE.deleteOnExit();
      fileF.deleteOnExit();
      fileG.deleteOnExit();
      fileJ.deleteOnExit();
      fileK.deleteOnExit();
      fileL.deleteOnExit();
      fileM.deleteOnExit();
      fileN.deleteOnExit();
      fileO.deleteOnExit();
      fileP.deleteOnExit();
      fileQ.deleteOnExit();
      fileR.deleteOnExit();
      fileS.deleteOnExit();
      fileT.deleteOnExit();

      A = "'" + fileA.getPath() + "'";
      B = "'" + fileB.getPath() + "'";
      C = "'" + fileC.getPath() + "'";
      D = "'" + fileD.getPath() + "'";
      E = "'" + fileE.getPath() + "'";
      F = "'" + fileF.getPath() + "'";
      G = "'" + fileG.getPath() + "'";
      J = "'" + fileJ.getPath() + "'";
      K = "'" + fileK.getPath() + "'";
      L = "'" + fileL.getPath() + "'";
      M = "'" + fileM.getPath() + "'";
      N = "'" + fileN.getPath() + "'";
      O = "'" + fileO.getPath() + "'";
      P = "'" + fileP.getPath() + "'";
      Q = "'" + fileQ.getPath() + "'";
      R = "'" + fileR.getPath() + "'";
      S = "'" + fileS.getPath() + "'";
      T = "'" + fileT.getPath() + "'";

      System.out.println("A : " + A + "\n" + "B : " + B + "\n" + "C : " + C + "\n" + "D : " + D + "\n" + "E : " + E + "\n" + "F : " + F + "\n" + "G : " + G + "\n" + "J : " + J + "\n" +  "K : " + K + "\n" +  "L : " + L + "\n" + "M : " + M + "\n" +  "N : " + N + "\n" +  "O : " + O + "\n"  +  "P : " + P + "\n"+  "Q : " + Q + "\n" +  "R : " + R + "\n" +  "S : " + S + "\n" +  "T : " + T + "\n");
      System.out.println("Test data created.");
  }
  
  private static void writeData(File dataFile) throws Exception {
      // File dataFile = File.createTempFile(name, ".dat");
      FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write((rand.nextInt(10) + "\t" + rand.nextInt(10) + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData2(File dataFile) throws Exception {
      // File dataFile = File.createTempFile(name, ".dat");
      FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write((rand.nextInt(100) + "\t" + rand.nextInt(100) + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData3(File dataFile) throws Exception {
      // File dataFile = File.createTempFile(name, ".dat");
	  start = 0;
      FileOutputStream dat = new FileOutputStream(dataFile);

      for (int i = 0; i < MIN; i++)
          dat.write((start++ + "\t" + start++ + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData4(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + rand.nextInt(10) + "\t" + rand.nextInt(10) + "\n")
                  .getBytes());

      
//      dat.write(( "John" + "\t" + -100 + "\t" + -100 + "\n")
//              .getBytes());
//      dat.write(( "John" + "\t" + 2 + "\t" + 2 + "\n")
//              .getBytes());
//      dat.write(( "John" + "\t" + 100 + "\t" + 100 + "\n")
//              .getBytes());

      dat.close();
  }
  
  private static void writeData7(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData8(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + rand.nextDouble() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData9(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData10(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t"  + rand.nextInt(10) + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData11(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + rand.nextInt() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData12(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + rand.nextInt() + "\t" + rand.nextDouble() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData13(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextInt() + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextInt() + "\t" + rand.nextDouble() + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData14(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextInt() + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextDouble() + "\n" )
                  .getBytes());

      dat.close();
  }
  
  private static void writeData15(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextDouble()  + "\t" + rand.nextInt() + "\n" )
                  .getBytes());

      dat.close();
  }
  
  
  private static void writeData16(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < 1; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextInt() + "\t" + UUID.randomUUID().toString() 
        		  + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextDouble() 
        		  + "\t" + UUID.randomUUID().toString() + "\t"  + UUID.randomUUID().toString() + "\t"
        		  + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t"
        		  + rand.nextInt() + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString()
        		  + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextDouble() + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString()
        		  + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + rand.nextInt() 
        		  + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" + UUID.randomUUID().toString() + "\t" 
        		  + rand.nextDouble() + "\t" + UUID.randomUUID().toString() + "\t"  + UUID.randomUUID().toString() + "\n")
                  .getBytes());

      dat.close();
  }
  
  @Test
  public void testScalability() throws Exception {
      PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + F.toString() + " using PigStorage() as (name: chararray, x : int, y : int);");
      pigServer.registerQuery("B = group A by name;");
      pigServer.registerQuery("C = foreach B generate group, SUM(A.x) as xx, SUM(A.y) as yy;");
      pigServer.registerQuery("D = filter C by xx > 100 and yy > 100;");
      //pigServer.registerQuery("store D into '" +  out.getAbsolutePath() + "';");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("D");
  
      assertTrue(derivedData != null);
  }
  
  /**
   * http://ofps.oreilly.com/titles/9781449302641/advanced_pig_latin.html
   */
  @Test
  public void testMap() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("players = load '/home/kaituo/code/pig2/trunk/baseball' using PigStorage() as (name:chararray, team:chararray, position:bag{t:(p:chararray)}, bat:map[]);");
      pigServer.registerQuery("noempty = foreach players generate name, ((position is null or IsEmpty(position)) ? {('unknown')} : position) as position;");
      pigServer.registerQuery("pos     = foreach noempty generate name, flatten(position) as position;");
      pigServer.registerQuery("bypos   = group pos by position;");
      //pigServer.registerQuery("store D into '" +  out.getAbsolutePath() + "';");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("bypos");
  
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL3() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + J.toString() + " using PigStorage() as (name: chararray, x : double);");
      pigServer.registerQuery("B = load " + K.toString() + " using PigStorage() as (user: chararray);");
      pigServer.registerQuery("C = join A by name, B by user;");
      pigServer.registerQuery("D = group C by $0;");
      pigServer.registerQuery("E = foreach D generate group, SUM(C.x);");
      //pigServer.registerQuery("store D into '" +  out.getAbsolutePath() + "';");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("E");
  
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL4() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + L.toString() + " using PigStorage() as (user: chararray, action : chararray);");
      pigServer.registerQuery("B = group A by user;");
      pigServer.registerQuery("C = foreach B { aleph = A.action; beth = distinct aleph; generate group, COUNT(beth);}");
      //pigServer.registerQuery("store D into '" +  out.getAbsolutePath() + "';");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("C");
  
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL5() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + K.toString() + " using PigStorage() as (user: chararray);");
      pigServer.registerQuery("B = load " + M.toString() + " using PigStorage() as (name: chararray);");
      pigServer.registerQuery("C = cogroup A by user, B by name parallel 40;");
      pigServer.registerQuery("D = filter C by COUNT(B) == 0;");
      pigServer.registerQuery("E = foreach D generate group;");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("E");
  
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL6() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + N.toString() + " using PigStorage() as (user: chararray, action: chararray, timespent:int, query_term: chararray, ip_addr: chararray, timestamp: chararray);");
      pigServer.registerQuery("B = group A by (user, query_term, ip_addr, timestamp);");
      pigServer.registerQuery("C = foreach B generate flatten(group), SUM(A.timespent);");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("C");
  
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL7() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
	  pigServer.setBatchOn();
	  pigServer.registerQuery("A = load " + O.toString() + " using PigStorage() as (user: chararray, timestamp:int);");
	  pigServer.registerQuery("B = group A by user;");
	  pigServer.registerQuery("C = foreach B {morning = filter A by timestamp < 43200; afternoon = filter A by timestamp >= 43200; generate  group, COUNT(morning), COUNT(afternoon);}");
	  Map<Operator, DataBag> derivedData = pigServer.getExamples("C");

	  assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL8() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + P.toString() + " using PigStorage() as (user: chararray, timespent:int, estimated_revenue:double);");
      pigServer.registerQuery("B = group A all;");
      pigServer.registerQuery("C = foreach B generate SUM(A.timespent), AVG(A.estimated_revenue);");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("C");
  
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL9() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + Q.toString() + " using PigStorage() as (user: chararray, action: chararray, timespent:int, query_term: chararray, ip_addr: chararray, timestamp:int, estimated_revenue:double, page_info: chararray, page_links: chararray);");
      pigServer.registerQuery("B = order A by query_term;");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("B");
  
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL10() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + Q.toString() + " using PigStorage() as (user: chararray, action: chararray, timespent:int, query_term: chararray, ip_addr: chararray, timestamp:int, estimated_revenue:double, page_info: chararray, page_links: chararray);");
      pigServer.registerQuery("B = order A by query_term, estimated_revenue desc, timespent;");
      Map<Operator, DataBag> derivedData = pigServer.getExamples("B");
  
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL11() throws Exception {
	  PigServer pigServer = new PigServer(pigContext);
      pigServer.setBatchOn();
      pigServer.registerQuery("A = load " + K.toString() + " using PigStorage() as (user: chararray);");
      pigServer.registerQuery("B = load " + M.toString() + " using PigStorage() as (name: chararray);");      
      pigServer.registerQuery("C = distinct A;");
      pigServer.registerQuery("gamma = distinct B;");
      pigServer.registerQuery("D = union C, gamma;");
      pigServer.registerQuery("E = distinct D;");

      Map<Operator, DataBag> derivedData = pigServer.getExamples("E");
  
      assertTrue(derivedData != null);
  }
  
  /**
   *    Pig:
   *     String query = 
            "a = load '" + file.getAbsolutePath() + "' as (id:int);" + 
            "split a into b if id > 3, c if id < 3, d otherwise;"
            ;
   *    
   *    #-----------------------------------------------
		# New Logical Plan:
		#-----------------------------------------------
		D: (Name: LOSplitOutput Schema: id1#7:int,id2#8:int)
		|   |
		|   (Name: Not Type: boolean Uid: 14)
		|   |
		|   |---(Name: Or Type: boolean Uid: 13)
		|       |
		|       |---(Name: GreaterThan Type: boolean Uid: 11)
		|       |   |
		|       |   |---id1:(Name: Project Type: int Uid: 5 Input: 0 Column: 0)
		|       |   |
		|       |   |---(Name: Constant Type: int Uid: 9)
		|       |
		|       |---(Name: LessThan Type: boolean Uid: 12)
		|           |
		|           |---id1:(Name: Project Type: int Uid: 5 Input: 0 Column: 0)
		|           |
		|           |---(Name: Constant Type: int Uid: 10)
		|
		|---1-2: (Name: LOSplit Schema: id1#5:int,id2#6:int)
		    |
		    |---A: (Name: LOLoad Schema: id1#5:int,id2#6:int)RequiredFields:null
		    
		#-----------------------------------------------
		# New Physical Plan:
		#-----------------------------------------------   
		D: Filter[bag] - scope-9
		|   |
		|   Not[boolean] - scope-17
		|   |
		|   |---Or[boolean] - scope-16
		|       |
		|       |---Greater Than[boolean] - scope-12
		|       |   |
		|       |   |---Project[int][0] - scope-10
		|       |   |
		|       |   |---Constant(3) - scope-11
		|       |
		|       |---Less Than[boolean] - scope-15
		|           |
		|           |---Project[int][0] - scope-13
		|           |
		|           |---Constant(3) - scope-14
		|
		|---1-2: Split - scope-8
		    |
		    |---A: New For Each(false,false)[bag] - scope-7
		        |   |
		        |   Cast[int] - scope-2
		        |   |
		        |   |---Project[bytearray][0] - scope-1
		        |   |
		        |   Cast[int] - scope-5
		        |   |
		        |   |---Project[bytearray][1] - scope-4
		        |
		        |---A: Load(/tmp/dataA6556908519936195013.dat:PigStorage) - scope-0
   */
  @Test
  public void testScriptL12() throws Exception {

  	PigServer pigserver = new PigServer(pigContext);

      String query = "A = load " + R
              + " using PigStorage() as (user : chararray, action : chararray, timespent : int, query_term : chararray, estimated_revenue : double);\n";
      pigserver.registerQuery(query);
      query = "split A into B if user is not null, alpha if user is null;";
      pigserver.registerQuery(query);
      query = "split B into C if query_term is not null, aleph if query_term is null;";
      pigserver.registerQuery(query);
      query = "D = group C by user;";
      pigserver.registerQuery(query);
      query = "E = foreach D generate group, MAX(C.estimated_revenue);";
      pigserver.registerQuery(query);
      query = "beta = group alpha by query_term;";
      pigserver.registerQuery(query);
      query = "gamma = foreach beta generate group, SUM(alpha.timespent);";
      pigserver.registerQuery(query);
      query = "beth = group aleph by action;";
      pigserver.registerQuery(query);
      query = "gimel = foreach beth generate group, COUNT(aleph);";
      pigserver.registerQuery(query);
      Map<Operator, DataBag> derivedData = pigserver.getExamples("E");
      Map<Operator, DataBag> derivedData2 = pigserver.getExamples("gamma");
      Map<Operator, DataBag> derivedData3 = pigserver.getExamples("gimel");
      assertTrue(derivedData != null);
      assertTrue(derivedData2 != null);
      assertTrue(derivedData3 != null);
  }
  
  @Test
  public void testScriptL13() throws Exception {
	  PigServer pigserver = new PigServer(pigContext);

      String query = "A = load " + J
              + " using PigStorage() as (user : chararray, estimated_revenue : double);\n";
      pigserver.registerQuery(query);
      query = "B = load " + L
              + " using PigStorage() as (name : chararray, phone : chararray);\n";
      pigserver.registerQuery(query);
      query = "C = join A by user left outer, B by name;";
      pigserver.registerQuery(query);
      Map<Operator, DataBag> derivedData = pigserver.getExamples("C");
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL14() throws Exception {
	  PigServer pigserver = new PigServer(pigContext);

      String query = "A = load " + J
              + " using PigStorage() as (user : chararray, estimated_revenue : double);\n";
      pigserver.registerQuery(query);
      query = "B = load " + L
              + " using PigStorage() as (name : chararray, phone : chararray);\n";
      pigserver.registerQuery(query);
      query = "C = join A by user left outer, B by name using 'merge';";
      pigserver.registerQuery(query);
      Map<Operator, DataBag> derivedData = pigserver.getExamples("C");
      assertTrue(derivedData != null);
  }
  
  
  @Test
  public void testScriptL15() throws Exception {
	  PigServer pigserver = new PigServer(pigContext);

      String query = "A = load " + S
              + " using PigStorage() as (user : chararray, action: chararray, estimated_revenue : double, timespent : int);\n";
      pigserver.registerQuery(query);
      query = "B = group A by user;\n";
      pigserver.registerQuery(query);
      query = "C = foreach B {beth = distinct A.action; rev = distinct A.estimated_revenue; ts = distinct A.timespent; generate group, COUNT(beth), SUM(rev), (int)AVG(ts); };";
      pigserver.registerQuery(query);
      Map<Operator, DataBag> derivedData = pigserver.getExamples("C");
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL16() throws Exception {
	  PigServer pigserver = new PigServer(pigContext);

      String query = "A = load " + J
              + " using PigStorage() as (user : chararray, estimated_revenue : double);\n";
      pigserver.registerQuery(query);
      query = "B = group A by user;\n";
      pigserver.registerQuery(query);
      query = "C = foreach B {D = order A by estimated_revenue; E = D.estimated_revenue; generate group, SUM(E); };";
      pigserver.registerQuery(query);
      Map<Operator, DataBag> derivedData = pigserver.getExamples("C");
      assertTrue(derivedData != null);
  }
  
  @Test
  public void testScriptL17() throws Exception {
	  PigServer pigserver = new PigServer(pigContext);

      String query = "A = load " + T
              + " using PigStorage() as (user : chararray, action : chararray, timespent : int, query_term : chararray, ip_addr : chararray, timestamp : chararray," 
              + "estimated_revenue : double, page_info : chararray, page_links : chararray, user_1 : chararray, action_1 : chararray, timespent_1 : int, query_term_1 : chararray, ip_addr_1 : chararray, timestamp_1 : chararray,"
              + "estimated_revenue_1 : double, page_info_1 : chararray, page_links_1 : chararray, user_2 : chararray, action_2 : chararray, timespent_2 : int, query_term_2 : chararray, ip_addr_2 : chararray, timestamp_2 : chararray,"
              + "estimated_revenue_2 : double, page_info_2 : chararray, page_links_2 : chararray);\n";
      pigserver.registerQuery(query);
      System.out.print(query);
      query = "B = group A by (user, action, timespent, query_term, ip_addr, timestamp,"
              + "estimated_revenue, user_1, action_1, timespent_1, query_term_1, ip_addr_1, timestamp_1,"
              + "estimated_revenue_1, user_2, action_2, timespent_2, query_term_2, ip_addr_2, timestamp_2,"
              + "estimated_revenue_2);\n";
      pigserver.registerQuery(query);
      System.out.print(query);
      query = "C = foreach B generate SUM(A.timespent), SUM(A.timespent_1), SUM(A.timespent_2), AVG(A.estimated_revenue), AVG(A.estimated_revenue_1), AVG(A.estimated_revenue_2);";
      pigserver.registerQuery(query);
      System.out.print(query);
      Map<Operator, DataBag> derivedData = pigserver.getExamples("C");
      assertTrue(derivedData != null);
  }
}