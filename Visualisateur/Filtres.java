/* ---------------------------------------------------------------------------------------------------------------------
       Package
----------------------------------------------------------------------------------------------------------------------*/

package Visualisateur;

/* ---------------------------------------------------------------------------------------------------------------------
       Importations
----------------------------------------------------------------------------------------------------------------------*/

import java.util.ArrayList;
import java.util.List;

import Outils.Tools;

/* ---------------------------------------------------------------------------------------------------------------------
       Filtrage du trafic
----------------------------------------------------------------------------------------------------------------------*/

public class Filtres implements Visualisateur {

    /* -----------------------------------------------------------------------------------------------------------------
        Méthodes et assesseurs
    ------------------------------------------------------------------------------------------------------------------*/

    public static List<Trame> ipSrcFilter(List<Trame> trames, String ipSrc) {

        if (trames != null) {
            ArrayList<Trame> res = new ArrayList<>();

            for (int i = 0; i < trames.size(); i++) {
                if (trames.get(i).getIpSrc() != null && trames.get(i).getIpSrc().equals(ipSrc)) {
                    res.add(trames.get(i));
                }
            }

            return res;
        }
        return null;
    }

    public static List<Trame> ipDstFilter(List<Trame> trames, String ipDst) {

        if (trames != null) {
            ArrayList<Trame> res = new ArrayList<>();

            for (int i = 0; i < trames.size(); i++) {
                if (trames.get(i).getIpSrc() != null && trames.get(i).getIpSrc().equals(ipDst)) {
                    res.add(trames.get(i));
                }
            }

            return res;
        }
        return null;
    }

    public static List<Trame> portSrcFilter(List<Trame> trames, String portSrc) {

        if (trames != null) {
            ArrayList<Trame> res = new ArrayList<>();

            for (int i = 0; i < trames.size(); i++) {
                if (trames.get(i).getPortSrc() != null) {
                    if (trames.get(i).getPortSrc() != null) {
                        String portAComparer = ""+Tools.getDecValues(trames.get(i).getPortSrc());
                        if(portSrc.equals(portAComparer)){
                            res.add(trames.get(i));
                        }
                    }
                }
            }

            return res;
        }
        return null;
    }

    public static List<Trame> portDstFilter(List<Trame> trames, String portDst) {

        if (trames != null) {
            ArrayList<Trame> res = new ArrayList<>();

            for (int i = 0; i < trames.size(); i++) {

                if (trames.get(i).getPortDst() != null) {
                    String portAComparer = ""+Tools.getDecValues(trames.get(i).getPortDst());
                    if(portDst.equals(portAComparer)){
                        res.add(trames.get(i));
                    }
                }
            }

            return res;
        }
        return null;
    }

    public static List<Trame> protocolFilter(List<Trame> trames, String protocol) {

        if (trames != null) {
            ArrayList<Trame> res = new ArrayList<>();

            for (int i = 0; i < trames.size(); i++) {
                if (trames.get(i).getProtocol() != null && trames.get(i).getProtocol().toLowerCase().equals(protocol.toLowerCase())) {
                    res.add(trames.get(i));
                }
            }

            return res;
        }
        return null;
    }

    public static List<Trame> execFilter(String command, List<Trame> trames) {
        List<Trame> res;

        if (command.substring(5, 7).equals("==")) {

            if (command.substring(0, 5).equals("ipsrc")) {
                res = ipSrcFilter(trames, command.substring(7, command.length()));
                return res;
            }

            if (command.substring(0, 5).equals("ipdst")) {
                res = ipDstFilter(trames, command.substring(7, command.length()));
                return res;
            }
            if (command.substring(0, 5).equals("ptsrc")) {
                res = portSrcFilter(trames, command.substring(7, command.length()));
                return res;
            }

            if (command.substring(0, 5).equals("ptdst")) {
                res = portDstFilter(trames, command.substring(7, command.length()));
                return res;
            }
            if (command.substring(0, 5).equals("proto")) {
                res = protocolFilter(trames, command.substring(7, command.length()));
                return res;
            }
        }

        return null;
    }

    public static boolean isFilter(String cmd){
        if(cmd.length() >= 7){
            if(cmd.substring(0,7).toLowerCase().equals("ipsrc==") || cmd.substring(0,7).toLowerCase().equals("ipdst==") || cmd.substring(0,7).toLowerCase().equals("ptsrc==") || cmd.substring(0,7).toLowerCase().equals("ptdst==") || cmd.substring(0,7).toLowerCase().equals("proto==")){
                return true;
            }
        }
        return false;
    }
}