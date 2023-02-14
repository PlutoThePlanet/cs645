import java.util.ArrayList;

public class Tree{
    private Node root;
    static ArrayList<Node> children = new ArrayList<Node>();

    public Tree(Node root){
        this.root = root;
    }

    public void addChild(Node node){
        children.add(node);
    }

    public Node getRoot(){
        return root;
    }
}