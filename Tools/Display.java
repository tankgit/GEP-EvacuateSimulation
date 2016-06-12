package Tools;



import GEP.*;

import java.util.Objects;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by tank on 4/19/16.
 */
public class Display {


    private static String printElement(Element element)
    {
        String c=null;
        switch(element.getType()){
            case CONSTANT:
                c=Objects.toString(element.getValue());
                break;
            case VARIABLE:
                switch (Objects.toString(element.getValue())){
                    case "0":c="d";
                        break;
                    case "1":c="n";
                        break;
                    case "2":c="w";
                        break;
                }
                break;
            case OPERATOR:
                Operator ope=(Operator)element.getValue();
                c=""+ope.getSign();
                break;
        }
        return c;
    }

    public static void displayExpression(Chromosome chromosome)
    {
        System.out.println("\033[31mEffective Gene Fragment:");
        Queue<GeneNode> q=new LinkedList<>();
        q.add(chromosome.root);
        System.out.print("|");
        while(q.size()>0)
        {
            if(q.peek().left!=null)
            {
                q.add(q.peek().left);
            }
            if(q.peek().right!=null)
            {
                q.add(q.peek().right);
            }
            System.out.print(printElement(q.peek().element));
            System.out.print("|");
            q.poll();
        }
        System.out.println("\033[0m\n");
    }

    public static void displayChromosome(Chromosome chromosome)
    {
        System.out.println("Chromosome:");
        System.out.print("|");
        for(Element e:chromosome.chromosome)
        {
            System.out.print((printElement(e)));
            System.out.print("|");
        }
        System.out.println("\n");
    }

    public static void displayMathExpression(Chromosome chromosome)
    {
        System.out.println("\033[32mMath Expression:");
        GeneNode root=chromosome.root;
        System.out.print("F( X0");
        for(int i=1;i< Setting.NumberOfVariables;i++)
            System.out.print(", X"+i);
        System.out.print(" ) = ");
        System.out.println(DFS(root));
        System.out.println("\033[0m");
    }


    public static String DFS(GeneNode node)
    {
        if(node.element.getType()!=Type.OPERATOR)
        {
            return printElement(node.element);
        }
        else
        {
            return " ("+DFS(node.left)+printElement(node.element)+DFS(node.right)+") ";
        }
    }

    public static void displayProgressBar(String title,int progress,float fitness,int color)
    {
        System.out.print("\n\033[1A");
        System.out.print("\033[20D");
        System.out.print("\033[K\033[0m");
        System.out.print(title+"\t\033["+(color+31)+"m[ ");
        System.out.print(progress+"% ]\t"+"Current best fitness: "+fitness+"\033[0m");
    }

}
