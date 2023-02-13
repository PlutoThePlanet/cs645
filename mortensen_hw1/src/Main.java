import java.util.ArrayList;
import java.util.Random;

public class Main {
    static int branch1[] = {20, 1, 2, 5, 6, 19};
    static int branch2[] = {3, 4, 5, 6, 19};       // Attacker 2
    static int branch3[] = {7, 8, 9, 10, 19};
    static int branch4[] = {11, 12, 13, 14, 19};   // Attacker 1
    static int branch5[] = {15, 16, 17, 18, 19};

    static ArrayList<Packet> packets = new ArrayList<Packet>(); // master list of packets that have arrived at the victim
    
    static int x = 10; // 10, 100, 1000
    static double p = 0.1; // 0.2, 0.4, 0.5, 0.6, 0.8

    static int num_pkts = 10;
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
        node_path_reconstruction();
        packets.clear(); // reset/clear all sent packets

        // for(){
        //     edgeSampling_marking();
        // }
        // edge_path_reconstruction();
    }

    public static void nodeSampling_marking(int branch[], Packet pkt, ArrayList<Packet> packets){
        for(int i=0; i<branch.length; i++){         // for each node/router in the branch
            double k = new Random().nextDouble();   // x (aka k) is a random # from 0-1
            if (k < p){                             // if x < p then,
                pkt.setNode(branch[i]);             // write the router into pkt.node
            }
            packets.add(pkt); // add every packet to master list of packets that have arrived at the victim
        }
    }

    public static void node_path_reconstruction(){

    }


    public static void edgeSampling_marking(){

    }

    public static void edge_path_reconstruction(){

    }
}