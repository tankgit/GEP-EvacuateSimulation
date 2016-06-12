package GEP;
/**
 * Created by tank on 4/19/16.
 */
public class GeneNode{
    
    public Element element;
    
    public GeneNode left;
    
    public GeneNode right;
    
    public GeneNode(Element element)
    {
        this.element=element;
        this.left=null;
        this.right=null;
    }
    
}