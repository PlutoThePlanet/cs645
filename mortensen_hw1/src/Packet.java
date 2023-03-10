public class Packet{
    private Integer node;
    private Integer start;
    private Integer end;
    private Integer distance;

    public Packet(){
        this.node = 1001; // 1001 = "unmarked"
        this.start = 1001;
        this.end = 1001;
        this.distance = 1001;
    }

    public int getNode(){
        return node;
    }
    public void setNode(int node){
        this.node = node;
    }

    public int getStart(){
        return start;
    }
    public void setStart(int start){
        this.start = start;
    }

    public int getEnd(){
        return end;
    }
    public void setEnd(int end){
        this.end = end;
    }

    public int getDistance(){
        return distance;
    }
    public void setDistance(int distance){
        this.distance = distance;
    }
}