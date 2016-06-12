package GEP;
/**
 * Created by tank on 4/19/16.
 */
public class Element{
    
    private Type type;
    private Object value;


    public Element(float value)
    {
        this.type=Type.CONSTANT;
        this.value=value;
    }

    public Element(int value)
    {
        this.type=Type.VARIABLE;
        this.value=value;
    }

    public Element(Operator value)
    {
        this.type=Type.OPERATOR;
        this.value=value;
    }

    public void setConstant(float value)
    {
        if(this.type==Type.CONSTANT)
        this.value=value;
        else{
            System.out.println("this method do not support this type of element");
            System.exit(0);
        }
    }
    
    public Type getType()
    {
        return this.type;
    }
    
    public Object getValue()
    {
        return this.value;
    }
}