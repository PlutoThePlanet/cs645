import java.util.ArrayList;

public class Node{
    static ArrayList<Node> children = new ArrayList<Node>();
    Integer router = null;

    public void addChild(Node node){
        children.add(node);
    }

    public int getRouter(){
        return router;
    }
    public void setRouter(int router){
        this.router = router;
    }
}