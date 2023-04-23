/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Protocoles;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations
----------------------------------------------------------------------------------------------------------------------*/

import java.util.ArrayList;
import java.util.List;
import Outils.*;

/* ---------------------------------------------------------------------------------------------------------------------
       Protocole TCP
----------------------------------------------------------------------------------------------------------------------*/

public class TCP implements Protocole {

    /* -----------------------------------------------------------------------------------------------------------------
        Attributs
    ------------------------------------------------------------------------------------------------------------------*/

    List<String> segment; // Segment TCP

    private String srcPort; // Numero de port de la source (2 octets)
    private String dstPort; // Numero de port de la destination (2 octets)
    private String sq; // Numero de sequence (4 octets)
    private String ackNum; // Numero d'acquittement (4 octets)
    private String thl;
    private String reserved;
    private String urg;
    private String ack;
    private String psh;
    private String rst;
    private String syn;
    private String fin;
    private String wdw; // Fenêtre (2 octets)
    private String checksum; // Code de detection d'erreurs (2 octets)
    private String UP; // Pointeur d'urgence (2 octets)
    private String optionsPadding; // Options et remplissage (4 octets)
    private HTTP data;

    /* -----------------------------------------------------------------------------------------------------------------
        Constructeurs
    ------------------------------------------------------------------------------------------------------------------*/

    public TCP(List<String> segment) {
        this.segment = segment;
        
        this.extractSrcPort(segment);
        this.extractDstPort(segment);
        this.extractSq(segment);
        this.extractAckNum(segment);
        this.extractThl_Reserved_Flags(segment);
        this.extractWindow(segment);
        this.extractChecksum(segment);
        this.extractUP(segment);
        this.extractOptionsPadding(segment);
        this.extractData(segment);

    }
    
    /* -----------------------------------------------------------------------------------------------------------------
        Méthodes et assesseurs
    ------------------------------------------------------------------------------------------------------------------*/

    private void extractSrcPort(List<String> segment){
        this.srcPort = segment.get(0);
        this.srcPort += segment.get(1);
    }

    private void extractDstPort(List<String> segment){
        this.dstPort = segment.get(2);
        this.dstPort += segment.get(3);
    }

    private void extractSq(List<String> segment){
        this.sq = segment.get(4);
        this.sq += segment.get(5);
        this.sq += segment.get(6);
        this.sq += segment.get(7);
    }

    private void extractAckNum(List<String> segment){
        this.ackNum = segment.get(8);
        this.ackNum += segment.get(9);
        this.ackNum += segment.get(10);
        this.ackNum += segment.get(11); 
    }

    private String flags;

    private void extractThl_Reserved_Flags(List<String> segment){
        String value = segment.get(12);
        value += segment.get(13);
        flags = value.substring(1);
        this.thl = value.charAt(0)+"";
        value = Tools.hexToBin(value.substring(1));
        this.reserved = value.substring(0,6);

        this.urg = value.charAt(6) + "";
        this.ack = value.charAt(7) + "";
        this.psh = value.charAt(8) + "";

        this.rst = value.charAt(9) + "";
        this.syn = value.charAt(10) + "";
        this.fin = value.charAt(11) + "";
    }

    private void extractWindow(List<String> segment){
        this.wdw = segment.get(14);
        this.wdw += segment.get(15);
    }

    private void extractChecksum(List<String> segment){
        this.checksum = segment.get(16);
        this.checksum += segment.get(17);
    }

    private void extractUP(List<String> segment){
        this.UP = segment.get(18);
        this.UP += segment.get(19);
    }

    private void extractOptionsPadding(List<String> segment){
        /*Option que si THL* 4>20  */
        for(int i = 20 ; i < (Integer.parseInt(thl, 16) * 4) ; i++){
            if(i == 20){
                this.optionsPadding = segment.get(i);
            }else{
                this.optionsPadding += segment.get(i);
            }
        }
        
    }

    private Boolean hasHttp() {
        /* a HTTP que si port = 80 et si size segment > thl */
        if((Tools.getIntValue(srcPort).intValue() == 80) || (Tools.getIntValue(dstPort).intValue() == 80)){
            if(segment.size() > (Integer.parseInt(thl, 16) * 4)){
                return true;
            }
        }
        return false;
    }

    private boolean isResquest() {
        return (Tools.getIntValue(dstPort).intValue() == 80);
    }

    private void extractData(List<String> segment){
        if (!hasHttp()) return;
        data = new HTTP(segment.subList((Integer.parseInt(thl, 16) * 4), segment.size()), isResquest());
    }
    public HTTP getData(){
        return data;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nTransmission Control Protocol, ");
        sb.append("Src Port : ").append(Tools.getDecValues(srcPort)).append(", Dst Port : ").append(Tools.getDecValues(dstPort));

        sb.append("\n\tSource Port : ").append(Tools.getDecValues(srcPort));
        sb.append("\n\tDestination Port : ").append(Tools.getDecValues(dstPort));
        sb.append("\n\tSequence number (raw): ").append(Tools.getDecValues(sq));
        sb.append("\n\tAcknowledgment number (raw) : ").append(Tools.getDecValues(ackNum));
        sb.append("\n\t"+Tools.hexToBin(thl) + " .... = Header Length : ").append((Tools.getDecValues(thl))*4);
        sb.append(" bytes (").append(Tools.getDecValues(thl)).append(")");
        sb.append("\n\tFlags : 0x" + flags);
        sb.append("\n\t\t" + reserved.substring(0,4) + " " +reserved.substring(4) + ".. .... = Reserved : " + Tools.isSet(reserved));
        sb.append("\n\t\t.... .." + urg + ". .... = Urgent : " + Tools.isSet(urg));
        sb.append("\n\t\t.... ..." + ack + " .... = Acknowledgment : " + Tools.isSet(ack));
        sb.append("\n\t\t.... .... " + psh + "... = Push : " + Tools.isSet(psh));
        sb.append("\n\t\t.... .... ." + rst + ".. = Reset : " + Tools.isSet(rst));
        sb.append("\n\t\t.... .... .." + syn + ". = Syn : " + Tools.isSet(syn));
        sb.append("\n\t\t.... .... ..." + syn + " = Fin : " + Tools.isSet(fin));
        sb.append("\n\tWindow : ").append(Tools.getDecValues(wdw));
        sb.append("\n\tChecksum : 0x").append(checksum);
        sb.append("\n\tUrgent pointer : ").append(Tools.getDecValues(UP.toString()));
        if(optionsPadding!=null){
            sb.append("\n\tOptions (raw) : "+ optionsPadding);
        }else{
            sb.append("\n\tNo option");
        }
        return sb.toString();
    }

    public String getPortSrc(){
        return srcPort;
    }

    public String getPortDst(){
        return dstPort;
    }

    public String getWdw(){
        return wdw;
    }

    public String getSeq(){
        return sq;
    }

    public String getUrg(){
        return urg;
    }

    public String getAck(){
        return ack;
    }

    public String getPsh(){
        return psh;
    }

    public String getRst(){
        return rst;
    }

    public String getSyn(){
        return syn;
    }

    public String getFin(){
        return fin;
    }
    public List<String> getFlagSet(){ // avoir la liste des flags set pour le visualisateur de trafic
        List<String> flagSet = new ArrayList<>();
        if(Integer.parseInt(urg, 2) != 0){flagSet.add("URG");}
        if(Integer.parseInt(ack, 2) != 0){flagSet.add("ACK");}
        if(Integer.parseInt(psh, 2) != 0){flagSet.add("PSH");}
        if(Integer.parseInt(rst, 2) != 0){flagSet.add("RST");}
        if(Integer.parseInt(syn, 2) != 0){flagSet.add("SYN");}
        if(Integer.parseInt(fin, 2) != 0){flagSet.add("FIN");}
        return flagSet;
    }
}
