package org.apache.pig.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


public class TestExampleGenerator3 {
	
	static PigContext pigContext = new PigContext(ExecType.LOCAL, new Properties());

//  private OSValidator z3 = OSValidator.get();
//  private Z3Context z4 = Z3Context.get();
  static int MAX = 100;
  static int MIN = 10;
  static String A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, AA;
  static int start = 0;
  static  File fileA, fileB, fileC, fileD, fileE, fileF, fileG, fileH, fileI, fileJ, fileK, fileL, fileM, fileN, fileO, fileP, fileQ, fileR, fileS, fileT, fileU, fileV, fileW, fileX, fileY, fileZ, fileAA;
  
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
      fileH = File.createTempFile("dataH", ".dat");
      fileI = File.createTempFile("dataI", ".dat");
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
      fileU = File.createTempFile("dataU", ".dat");
      fileV = File.createTempFile("dataV", ".dat");
      fileW = File.createTempFile("dataW", ".dat");
      fileX = File.createTempFile("dataX", ".dat");
      fileY = File.createTempFile("dataY", ".dat");
      fileZ = File.createTempFile("dataZ", ".dat");
      fileAA = File.createTempFile("dataAA", ".dat");
      
      writeData(fileA);
      writeData(fileB);
      writeData2(fileC);
      writeData3(fileD);
      writeData3(fileE);
      writeData4(fileF);
      writeData4(fileG);
      writeData5(fileH);
      writeData6(fileI);
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
      writeData17(fileU);
      writeData18(fileV);
      writeData19(fileW);
      writeData20(fileX);
      writeData22(fileZ);
      writeData23(fileAA, 401);

      fileA.deleteOnExit();
      fileB.deleteOnExit();
      fileC.deleteOnExit();
      fileD.deleteOnExit();
      fileE.deleteOnExit();
      fileF.deleteOnExit();
      fileG.deleteOnExit();
      fileH.deleteOnExit();
      fileI.deleteOnExit();
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
      fileU.deleteOnExit();
      fileV.deleteOnExit();
      fileW.deleteOnExit();
      fileX.deleteOnExit();
      fileY.deleteOnExit();
      fileZ.deleteOnExit();
      fileAA.deleteOnExit();
      
      A = "'" + fileA.getPath() + "'";
      B = "'" + fileB.getPath() + "'";
      C = "'" + fileC.getPath() + "'";
      D = "'" + fileD.getPath() + "'";
      E = "'" + fileE.getPath() + "'";
      F = "'" + fileF.getPath() + "'";
      G = "'" + fileG.getPath() + "'";
      H = "'" + fileH.getPath() + "'";
      I = "'" + fileI.getPath() + "'";
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
      U = "'" + fileU.getPath() + "'";
      V = "'" + fileV.getPath() + "'";
      W = "'" + fileW.getPath() + "'";
      X = "'" + fileX.getPath() + "'";
      Y = "'" + fileY.getPath() + "'";
      Z = "'" + fileZ.getPath() + "'";
      AA = "'" + fileAA.getPath() + "'";
      System.out.println("A : " + A + "\n" + "B : " + B + "\n" + "C : " + C + "\n" + "D : " + D + "\n" + "E : " + E + "\n" + "F : " + F + "\n" + "G : " + G + "\n" + "H : " + H + "\n" + "I : " + I + "\n" + "J : " + J + "\n" +  "K : " + K + "\n" +  "L : " + L + "\n" +  "M : " + M + "\n" +  "N : " + N + "\n" +  "O : " + O + "\n" +  "P : " + P + "\n" +  "Q : " + Q + "\n" +  "R : " + R + "\n" +  "S : " + S + "\n" +  "T : " + T + "\n" +  "U : " + U + "\n" +  "V : " + V + "\n" +  "W : " + W + "\n" +  "X : " + X + "\n" +  "Y : " + Y + "\n" +  "Z : " + Z + "\n" +  "AA : " + AA + "\n");
      System.out.println("Test data created.");
  }
  
  private static void writeData(File dataFile) throws Exception {
      // File dataFile = File.createTempFile(name, ".dat");
      FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

//      for (int i = 0; i < MIN; i++)
//          dat.write((rand.nextInt(10) + "\t" + rand.nextInt(10) + "\n")
//                  .getBytes());
      dat.write((2 + "\t" + 1 + "\n").getBytes());
      dat.write((-2 + "\t" + 1 + "\n").getBytes());

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
  
  private static void writeData5(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( rand.nextFloat() + "\t" + 10*rand.nextFloat() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData6(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( rand.nextDouble() + "\t" + 10*rand.nextDouble() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData7(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

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

      for (int i = 0; i < MIN; i++)
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
  
  private static void writeData17(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
    	  dat.write(( rand.nextDouble() + "\t" + rand.nextDouble() + "\t" + rand.nextDouble() + "\n")
                  .getBytes());

          
      dat.close();
  }
  
  private static void writeData18(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( rand.nextFloat() + "\t" + rand.nextFloat() + "\t" + rand.nextFloat() + "\t" + rand.nextFloat() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData19(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
    	  dat.write(( rand.nextDouble() + "\t" + rand.nextDouble() + "\t" + rand.nextDouble() + "\t" + rand.nextDouble() + "\t" + rand.nextDouble() + "\n")
                  .getBytes());

          
      dat.close();
  }
  
  private static void writeData20(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( rand.nextLong() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData21(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( 6*rand.nextDouble() + "\t" + 5*rand.nextDouble() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData22(File dataFile) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();

      for (int i = 0; i < MIN; i++)
          dat.write(( rand.nextLong() + "\t" + rand.nextInt() + "\n")
                  .getBytes());

      dat.close();
  }
  
  private static void writeData23(File dataFile, int n) throws Exception {
	  FileOutputStream dat = new FileOutputStream(dataFile);

      Random rand = new Random();
      
      String gbody = "";
	  for (int i = 0; i < n; i++) {
	     if (i > 0) gbody = gbody + "\t";
	     gbody = gbody + rand.nextInt();
	  }

      for (int i = 0; i < MIN; i++)
          dat.write(( UUID.randomUUID().toString() + "\t" + gbody + "\n")
                  .getBytes());

      dat.close();
  }
  
  
  /**
   *    A = load '$widerow' using PigStorage('\u0001') as (name: chararray, c0: int, c1: int, ..., c500: int);
		B = group A by name parallel $parrallelfactor;
		C = foreach B generate group, SUM(A.c0) as c0, SUM(A.c1) as c1, ... SUM(A.c500) as c500;
		D = filter C by c0 > 100 and c1 > 100 and c2 > 100 ... and c500 > 100;
		store D into '$out';
   * @throws Exception
   */
//  @Test
//  public void testScalabilityWith() throws Exception {
//	  int n = 401;
//	  PigServer pigServer = new PigServer(pigContext);
//      pigServer.setBatchOn();
//      
//	  String abody = "";
//	  for (int i = 0; i < n; i++) {
//	     if (i > 0) abody = abody + ", ";
//	     abody = abody + "c"+i+": int";
//	  }
//	  
//	  String cbody = "";
//	  for (int j = 0; j < n; j++) {
//	     if (j > 0) cbody = cbody + ", ";
//	     cbody = cbody + "SUM(A.c" + j + ") as c" + j;
//	  }
//	  
//	  String dbody = "";
//	  for (int k = 0; k < n; k++) {
//	     if (k > 0) dbody = dbody + " and ";
//	     dbody = dbody + "c" + k + " > 100";
//	  }
//	  
//	  pigServer.registerQuery("A = load " + AA.toString() + " using PigStorage() as (name: chararray, " + abody + ");");
//	  pigServer.registerQuery("B = group A by name;");
//      pigServer.registerQuery("C = foreach B generate group, " + cbody + ";");
//      pigServer.registerQuery("D = filter C by " + dbody + ";");
//      //pigServer.registerQuery("store D into '" +  out.getAbsolutePath() + "';");
//      Map<Operator, DataBag> derivedData = pigServer.getExamples("D");
//  
//      assertTrue(derivedData != null);
//	}
  
  @Test
  public void testGroup3() throws Exception {
      PigServer pigServer = new PigServer(pigContext);
      pigServer.registerQuery("A = load " + A.toString() + " as (x:int, y:int);");
      pigServer.registerQuery("B = FILTER A by  x+y > 0 and x-y< 0;");
    
      Map<Operator, DataBag> derivedData = pigServer.getExamples2("B");

      assertTrue(derivedData != null);

  }
  
  public static void main(String[] args) {
	  int index = 0;

      for (int i = 0; i < 2; ++i) {
              index += (1 << i);
              System.out.println(index);
      }
  }
}
