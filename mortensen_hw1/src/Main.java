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
    static int num_pkts = 1;
    
    static int x = 1000; // 10, 100, 1000
    static double p = 0.2; // 0.2, 0.4, 0.5, 0.6, 0.8

    public static void main(String[] args){
        int successes = 0;
        for(int j=0; j<100; j++){
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
            boolean test = node_path_reconstruction(packets);
            if(test){
                successes++;
            }
            packets.clear(); // reset/clear all sent packets
        }
        System.out.println(successes + " out of 100 nodeSampling tests were successful.");
        


        for(int i=0; i<num_pkts; i++){ // each branch is sending the same num of pkts through,
            Packet pkt = new Packet();
            edgeSampling_marking(branch1, pkt, packets);
            edgeSampling_marking(branch2, pkt, packets);
            edgeSampling_marking(branch3, pkt, packets);
            edgeSampling_marking(branch5, pkt, packets);
        }
        for(int i=0; i<num_pkts*x; i++){ // but the "attacker branch" will be sending a lot more pkts
            Packet pkt = new Packet();
            edgeSampling_marking(branch4, pkt, packets);
        }
        edge_path_reconstruction(packets);
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

    public static boolean node_path_reconstruction(ArrayList<Packet> packets){
        Map<Integer, Integer> nodeTbl = new HashMap<>();
        for(int i=0; i<packets.size(); i++){        // for each pkt from attacker
            int z = packets.get(i).getNode();       // pkt node
            if(nodeTbl.containsKey(z)){             // if the Packet already exists in nodeTbl,
                nodeTbl.put(z, nodeTbl.get(z)+1);   // increment its count
            }else{
                nodeTbl.put(z, 1);            // else insert into nodeTbl
            }
        }
        nodeTbl.remove(1001); // remove the unmarked routers
        Map<Integer, Integer> sortedNodeTbl = sortByValue(nodeTbl, false); // sort by descending order
        //System.out.println(sortedNodeTbl);
        Set<Integer> keys = sortedNodeTbl.keySet(); // check is path was successfully found or not
        String routers = "";
        for(Integer key: keys) {
            routers += key + " ";
        }
        String[] route = routers.split(" ");
        String route_str = "";
        String branch_str = "";
        try{
            for(int i=0; i<5; i++){
                route_str += route[i] + " ";
            }
        }catch(Exception e){
            return false;
        }
        for(int i=branch4.length-1; i>=0; i--){
            branch_str += branch4[i] + " ";
        }
        if(route_str.equals(branch_str)){
            return true;
        }else{
            return false;
        }
    }


    public static void edgeSampling_marking(Integer branch[], Packet pkt, ArrayList<Packet> packets){
        for(int i=0; i<branch.length; i++){             // for each node/router in the branch
            double k = new Random().nextDouble();       // x (aka k) is a random # from 0-1
            if (k < p){                                 // if x < p then,
                pkt.setStart(branch[i]);                // write the router into pkt.start 
                pkt.setDistance(0);             // and set pkt.distance to 0
            }else{
                if(pkt.getDistance() == 0){
                    pkt.setEnd(branch[i]);              // write the router into pkt.end
                }
                pkt.setDistance(pkt.getDistance()+1);   // increment the distance
            }
            packets.add(pkt); // add every packet to master list of packets that have arrived at the victim
        }
    }

    // Collections.sort(packets, new Comparator<Packet>(){
    //     public Integer compare(Packet p1, Packet p2){
    //         return p1.getDistance().compareTo(p2.getDistance());
    //     }
    // });

    public static void edge_path_reconstruction(ArrayList<Packet> packets){
        Node root = new Node();
        Tree tree = new Tree(root);

        for(int i=0; i<packets.size(); i++){        // remove any "unmarked" packets
            if(packets.get(i).getEnd() == 1001){
                packets.remove(i);
            }
        }
        // System.out.println(packets);
        // for(int i=0; i<packets.size(); i++){        // sort packets by pkt.distance //////////////////////////////////
        //     Collections.sort(packets.get(i).getDistance());
        // }

        // for(int i=0; i<packets.size(); i++){        // for each packet w
        //     if(packets.get(i).getDistance() == 0){  // if pkt.distance = 0 then
        //         Node node = new Node(packets.get(i).getNode());
        //         tree.addChild(node);// insert edge (pkt.start, v, 0) into G
        //     }else{
        //         // insert edge (pkt.start, w.end, w.distance) into G
        //     }
        // }
        // remove any edge (x,y,d) with d != distance from x to v in G
        // get path
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