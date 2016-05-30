package org.nmdp.HLAGene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nmdp.ParseExon.*;

public abstract class ExonIntronData {

    private static final char DIVIDER = '-';
    private String sampleID;
    private String gls;
    private String phaseSet;
    protected Map<SectionName, String> geneData = new HashMap<>();
    protected Map<SectionName, String> plData = new HashMap<>();

    private String fullLength = "";


    public String getFullLength() {
        return fullLength;
    }

    public void setFullLength(String fullLength) {
        this.fullLength = fullLength;
    }

    public void setSampleId(String sID){
        sampleID = sID;
    }

    public String getSampleID(){
        return sampleID;
    }

    public void setGls(String gls){
        this.gls = gls;
    }

    public String getGls(){
        return gls;
    }


    public void setPhase(String phase){
        this.phaseSet = phase;
    }

    public String getPhase(){
        return phaseSet;
    }

    public String getFive_NS() {
        return geneData.get(SectionName.DS);
    }

    public void setFive_UTR(String five_NS) {
        geneData.put(SectionName.DS, five_NS);
    }

    public String getExon(SectionName sn) {
        if(geneData.containsKey(sn))
        return geneData.get(sn);
        else
            return "";
    }

    public String getIntron(SectionName sn) {
        if(geneData.containsKey(sn))
            return geneData.get(sn).toLowerCase();
        else
            return "";
    }





    public String getExon_pl(SectionName sn) {
        if(plData.containsKey(sn)){
            return plData.get(sn);
        }
        else {
            return "";
        }
    }

    public void setExon_pl(SectionName sn, String pl){
        plData.put(sn, pl);
    }



    protected String filterDivider(String seq){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < seq.length(); i++){
            if(seq.charAt(i) != DIVIDER){
                sb.append(seq.charAt(i));
            }
        }
        return sb.toString();
    }
    /**
     * Separate exon and intron sequence from original geneData.
     * @param data One line text from alignment file.
     * @param extron List of index of exon start and end position.
     * @param intron List of index of intron start and end position.
     */
    abstract public void setExonIntron(String data, List<Integer> extron, List<Integer> intron);

    abstract public String getCDS();


}

