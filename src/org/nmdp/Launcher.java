package org.nmdp;

import java.io.File;

import org.nmdp.ParseExon.GeneType;
import org.nmdp.ParseExon.ParseExon;
import org.nmdp.databaseAccess.DatabaseUtil;

public class Launcher {

	public static void main(String[] args) throws Exception {
		ParseExon pe = new ParseExon();
		DatabaseUtil.connectDatabase();
		DatabaseUtil.createTable();
		DatabaseUtil.creatExonTable();
		pe.run(new File("HLA-A_pb.clu"), new File("HLA-A_Freq.txt"), GeneType.HLA_AC);
		DatabaseUtil.cleanUp();
	}

}
