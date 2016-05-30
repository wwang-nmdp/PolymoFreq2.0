package org.nmdp.ParseExon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.nmdp.HLAGene.*;
import org.nmdp.databaseAccess.DatabaseUtil;
import org.nmdp.statics.PolymorphStaticsProessor;

public class    ParseExon{
private Scanner scannerAlign;
private Scanner scannerFreq;
//cut the first 100 positions because of the low coverage.
private int looper = 50;
private String refSeq;
private ArrayList<Integer> indexIntron = new ArrayList<Integer>();
private ArrayList<Integer> indexExon = new ArrayList<Integer>();
private ArrayList<ExonIntronData> seqList = new ArrayList<ExonIntronData> ();
private ArrayList<BaseFreq> freqList = new ArrayList<BaseFreq>();
private File inputAlign;
private File inputFreq;
private PrintWriter pw;
private static final char DIVIDER = '-';


/**
 * Input files contains one alignment and frequency table and genetype.
 * @param align alignment result from Clustal_Omega output in .Clu format.
 * @param freq 
 * @param start
 * @param end
 * @throws Exception
 */
	public void run(File align, File freq, SectionName start, SectionName end) throws Exception{
		inputAlign = align;
		inputFreq = freq;
		
		setPrinter();
		HLAGeneData.setType(start, end);
		countExonIndex();
		extratExons();
		//extraFreq();
		//PolymorphStaticsProessor.processPolyMoph(freqList, seqList, indexExon, indexIntron, pw);
	}
	private void extraFreq() {
		try {
			scannerFreq = new Scanner(inputFreq);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//skip title: 8 lines
		for(int i = 0; i < 8; i++){
			scannerFreq.nextLine();
		}
		while(scannerFreq.hasNextLine()){
			String line = scannerFreq.nextLine();
			Scanner lineSc = new Scanner(line);
			if(!lineSc.hasNextInt()){
				lineSc.close();
				return;
			}
			lineSc.nextInt();	
			freqList.add(new BaseFreq(lineSc.nextInt(), lineSc.nextInt(), lineSc.nextInt(), lineSc.nextInt()));
			lineSc.close();
		}
		scannerFreq.close();
		
	}
	private void setPrinter() {
		File output = new File("./HLA-A_PolyMoFreq_out.csv");
		try {
			pw = new PrintWriter(output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * extra the sequences of exons from alignment results which might contains gaps.
	 */
	private void extratExons()  {
		try {
			scannerAlign = new Scanner(inputAlign);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Skip four lines to first row of geneData
		scannerAlign.nextLine();
		scannerAlign.nextLine();
		scannerAlign.nextLine();
		scannerAlign.nextLine();
		while(scannerAlign.hasNext()){
			try {
				processSample(scannerAlign.nextLine());
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		scannerAlign.close();
	}
	private void processSample(String data) throws Exception {
		if(data.charAt(0) == ' '){
			//If the geneData is the last line, do not process
			return;
		}
		//split the geneData by white space or |
		String[] split = data.split(" |\\|");
		ExonIntronData ei = new HLAGeneData();
		ei.setSampleId(split[0]);
		ei.setGls(split[1]);
		ei.setPhase(split[2]);
		ei.setExonIntron(data, indexExon, indexIntron);
		ei.setFullLength(data);
		seqList.add(ei);
		DatabaseUtil.insertExonData(ei);
	}
		
	/**
	 * find the index of all exon and intron
	 */
	private void countExonIndex() {
		try {
			scannerAlign = new Scanner(inputAlign);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Skip three lines to reference
		scannerAlign.nextLine();
		scannerAlign.nextLine();
		scannerAlign.nextLine();
		
		refSeq = scannerAlign.nextLine();
		//Find the first divider
		while(refSeq.charAt(looper) != DIVIDER){
			looper ++;
		}
		while(looper < refSeq.length()){
			findIntron();
			findExon();
		}
		scannerAlign.close();
		
	}
	private void findIntron() {
		if(looper >= refSeq.length()){
			return;
		}
		while(looper < refSeq.length() && !Character.isLowerCase(refSeq.charAt(looper))){
			looper++;
		}
		int start = looper;
		while(looper < refSeq.length() && (Character.isLowerCase(refSeq.charAt(looper)) || refSeq.charAt(looper) == DIVIDER) ){
			looper++;
		}
		int end = looper-1;
		indexIntron.add(start);
		indexIntron.add(end);
		System.out.println("intron: "+refSeq.substring(start, end+1));
	}
	
	private void findExon(){
		if(looper >= refSeq.length()){
			return;
		}
		while(looper < refSeq.length() && Character.isLowerCase(refSeq.charAt(looper)) ){
			looper++;
		}
		int start = looper;
		while(looper < refSeq.length() && (!Character.isLowerCase(refSeq.charAt(looper)) || refSeq.charAt(looper) == DIVIDER)){
			looper++;

		}
		int end = looper-1;
		indexExon.add(start);
		indexExon.add(end);
		System.out.println("extron: "+refSeq.substring(start, end+1));
	}
	

}
