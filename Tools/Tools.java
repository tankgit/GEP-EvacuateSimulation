package Tools;
import GEP.*;
import Room.RoomGen;


import java.util.*;

import static Room.RoomGen.*;
import static java.lang.Math.abs;
import static java.lang.Math.random;

/**
 * Created by tank on 4/19/16.
 */
public class Tools{
    //TODO: refine the tree from bi-tree to multi-tree
    public static GeneNode CreateTree(Vector<Element> chromosome)
    {
        if(chromosome.size()>0)
        {
            GeneNode root=new GeneNode(chromosome.elementAt(0));
            Queue<GeneNode> opeQueue=new LinkedList<>();
            if(chromosome.elementAt(0).getType()== Type.OPERATOR)
            {
                opeQueue.add(root);
                for(int i=1;i<chromosome.size()&&opeQueue.size()>0;i++)
                {
                    GeneNode next=new GeneNode(chromosome.elementAt(i));
                    if(next.element.getType()==Type.OPERATOR)
                    {
                        opeQueue.add(next);
                    }
                    
                    if(opeQueue.peek().left==null)
                    {
                        opeQueue.peek().left=next;
                    }
                    else
                    {
                        opeQueue.peek().right=next;
                        opeQueue.poll();
                    }
                }
            }
             return root;
        }
        else
        {
             return null;
        }
    }
    
    public static float CalcuFitnessFromData(RoomGen.Agent agent,Vector<RoomGen.Gate> gates, GeneNode root)
    {
        float r;
        float res=Float.MAX_VALUE;
        for(int i=0;i<gates.size();i++)
        {
            r = Calcul(agent,i,gates.get(i).width,root);
            r=r<0?Float.MAX_VALUE:r;
            if(res>r){
                res=r;
                agent.choose=i;
            }
        }
        return res;
    }
    
    private static float Calcul(RoomGen.Agent agent,int gatesNum,float width,GeneNode root)
    {

        Element e=root.element;
        switch(e.getType())
        {
            case CONSTANT:
                return (float)e.getValue();
            case VARIABLE:
                switch((int)e.getValue())
                {
                    case 0:return agent.dist.get(gatesNum);
                    case 1:return agent.n.get(gatesNum);
                    case 2:return width;

                }
            case OPERATOR:
            {
                float ope1=Calcul(agent,gatesNum,width,root.left);
                float ope2=Calcul(agent,gatesNum,width,root.right);
                Operator ope=(Operator)e.getValue();
                float[] operands=new float[]{ope1,ope2};
                return ope.rule(operands);
            }
        }
        return 0;
    }

    public static Chromosome randomChromosome(int head, int tail,RoomGen room)
    {
        Chromosome chromosome=new Chromosome();
        boolean valid=false;
        while(!valid) {
            chromosome.Clear();
            int sizeOfTree = head + tail;
            for (int i = 0; i < sizeOfTree; i++) {
                boolean inHead = (i < head);
                chromosome.Add(randomElement(inHead));
            }
            chromosome.Initialize();
            valid=testValid(chromosome)&&testVarCorrelation(chromosome,room.agents.get(64),room.gates);
        }
        return chromosome;
    }

    public static Element randomElement(boolean inHead)
    {
        Type type= randomType((int)randomNumber(0,3));
        if(!inHead&&type==Type.OPERATOR)
            if(Setting.HaveConstant)
                type=Type.CONSTANT;
            else type=Type.VARIABLE;
        switch(type){
            case CONSTANT: return new Element(randomNumber(Setting.ConstantMin,Setting.ConstantMax));
            case VARIABLE: return new Element(randomVariable());
            case OPERATOR: return new Element(randomOperator());
            default: System.out.println("Element get NULL type!");return null;
        }
    }

    private static Type randomType(int seed)
    {
        switch(seed%3){
            case 0:return Type.VARIABLE;
            case 1:return Setting.HaveConstant?Type.CONSTANT:(randomNumber(0,1)>0.5?Type.VARIABLE:Type.OPERATOR);
            case 2:return Type.OPERATOR;
            default: System.out.println("Type get NULL type!"); return Type.NULL;
        }
    }

    public static float randomNumber(float min,float max)
    {
        return (float)(random()*(max-min)+min);
    }
    // random(int): [min,max-1]
    //TODO: random() need to be changed since its return value is not clear.
    private static int randomVariable()
    {
        return (int)randomNumber(0,Setting.NumberOfVariables-0.01f);
    }

    private static Operator randomOperator()
    {
        return Setting.operators.get((int)randomNumber(0,Setting.operators.size()-0.01f));
    }

    public static void refineConstant(Element e)
    {
        if(e.getType()==Type.CONSTANT)
        {
            float constant = (float) e.getValue() + Setting.ConstantStep * (int) randomNumber(-1, 1);
            e.setConstant(constant);
        }
        else {
            System.out.println("Constant refining error.");
            System.exit(0);
        }
    }


    public static void sortByFitness(Vector<Chromosome> chromosomes,int left,int right)
    {
        float key=chromosomes.get((left+right)/2).trainingFitness;
        int i=left;
        int j=right;

        while(i<j)
        {
            while(chromosomes.get(i).trainingFitness <key)i++;
            while(chromosomes.get(j).trainingFitness >key)j--;
            if(i<=j)
            {
                Chromosome tem=chromosomes.get(i);
                chromosomes.set(i++,chromosomes.get(j));
                chromosomes.set(j--,tem);
            }
        }
        if(j<right)sortByFitness(chromosomes,left,j);
        if(i>left)sortByFitness(chromosomes,i,right);
    }

    public static boolean testValid(Chromosome chromosome)
    {
        int n=0,d=0,w=0;
        String ex=Display.DFS(chromosome.root);
        if(ex.indexOf('n')!=-1)n=1;
        if(ex.indexOf('d')!=-1)d=1;
        if(ex.indexOf('w')!=-1)w=1;
        return n * d * w != 0;
    }

    public static boolean testVarCorrelation(Chromosome chromosome, Agent testAgent, Vector<RoomGen.Gate> gates)
    {
        float f1=Calcul(testAgent,0,gates.get(0).width,chromosome.root);
        testAgent.dist.set(0,testAgent.dist.get(0)+1);
        float f2=Calcul(testAgent,0,gates.get(0).width,chromosome.root);
        testAgent.n.set(0,testAgent.n.get(0)+1);
        float f3=Calcul(testAgent,0,gates.get(0).width,chromosome.root);
        float f4=Calcul(testAgent,0,gates.get(0).width-0.5f,chromosome.root);
        int sign=(int)(f1/abs(f1));
        return !(f2*sign<f1*sign|| f3*sign < f2*sign || f4*sign < f3*sign);
    }
}