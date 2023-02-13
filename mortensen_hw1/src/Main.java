import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Main {
    static Integer branch1[] = {20, 1, 2, 5, 6, 19};
    static Integer branch2[] = {3, 4, 5, 6, 19};       // Attacker 2
    static Integer branch3[] = {7, 8, 9, 10, 19};
    static Integer branch4[] = {11, 12, 13, 14, 19};   // Attacker 1
    static Integer branch5[] = {15, 16, 17, 18, 19};

    static ArrayList<Packet> packets = new ArrayList<Packet>(); // master list of packets that have arrived at the victim
    
    static int x = 10; // 10, 100, 1000
    static double p = 0.1; // 0.2, 0.4, 0.5, 0.6, 0.8

    static int num_pkts = 1000;
    // int t = 1; // transmission rate
    // double attack_t = t * x; // attacker transmission rate
    //System.out.println("test");

    public static void main(String[] args){
        for(int i=0; i<num_pkts; i++){ // each branch is sending the same num of pkts through,
            Packet pkt = new Packet();
            nodeSampling_marking(branch1, pkt, packets);
            nodeSampling_marking(branch2, pkt, packets);
            nodeSampling_marking(branch3, pkt, packets);
            nodeSampling_marking(branch5, pkt, packets);
        }
        for(int i=0; i<num_pkts*x; i++){ // but the "attacker branch" will be sending a lot more pkts
            Packet pkt = new Packet();
            nodeSampling_marking(branch4, pkt, packets);
        }
        node_path_reconstruction(packets);
        packets.clear(); // reset/clear all sent packets

        // for(){
        //     edgeSampling_marking();
        // }
        // edge_path_reconstruction();
    }

    public static void nodeSampling_marking(Integer branch[], Packet pkt, ArrayList<Packet> packets){
        for(int i=0; i<branch.length; i++){         // for each node/router in the branch
            double k = new Random().nextDouble();   // x (aka k) is a random # from 0-1
            if (k < p){                             // if x < p then,
                pkt.setNode(branch[i]);             // write the router into pkt.node
            }
            packets.add(pkt); // add every packet to master list of packets that have arrived at the victim
        }
    }

    public static void node_path_reconstruction(ArrayList<Packet> packets){
        Map<Integer, Integer> nodeTbl = new HashMap<>();
        for(int i=0; i<packets.size(); i++){        // for each pkt from attacker
            int z = packets.get(i).getNode();       // pkt node
            if(nodeTbl.containsKey(z)){             // if the Packet already exists in nodeTbl,
                nodeTbl.put(z, nodeTbl.get(z)+1);   // increment its count
            }else{
                nodeTbl.put(z, 1);            // else insert into nodeTbl
            }
        }
        nodeTbl.remove(0); // remove the unmarked routers
        Map<Integer, Integer> sortedNodeTbl = sortByValue(nodeTbl, false); // sort by descending order
        System.out.println(sortedNodeTbl);
    }


    public static void edgeSampling_marking(){

    }

    public static void edge_path_reconstruction(){

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // from https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
    private static Map<Integer, Integer> sortByValue(Map<Integer, Integer> unsortMap, final boolean order){
        List<Entry<Integer, Integer>> list = new LinkedList<>(unsortMap.entrySet());
        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}