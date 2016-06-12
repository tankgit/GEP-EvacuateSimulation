package Room;

import java.util.Vector;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by tank on 5/30/16.
 */
public class RoomGen {

    public class Gate{
        float x;
        float y;
        public float width;
        Gate(float x,float y,float w)
        {
            this.x=x;
            this.y=y;
            this.width=w;
        }
    }

    public static class Agent{
        float x;
        float y;
        public Vector<Float> dist=new Vector<>();
        public Vector<Integer> n=new Vector<>();
        public int choose=-1;
    }

    public Vector<Gate> gates=new Vector<>();
    public Vector<Agent> agents=new Vector<>();

    private float height;
    private float width;

    private float acc=1;

    public RoomGen(float w,float h)
    {
        this.height=h;
        this.width=w;
    }

    public void addGate(float x,float y,float w)
    {
        gates.add(new Gate(x,y,w));
    }



    public void agentGen()
    {
        for(float i=0+acc;i<this.height-acc;i+=acc)
            for(float j=0+acc;j<this.width-acc;j+=acc)
            {
                Agent a=new Agent();
                a.x=j;
                a.y=i;
                for(int k=0;k<gates.size();k++)
                {
                    a.dist.add((float) sqrt(pow(a.x-gates.get(k).x,2.f)+pow(a.y-gates.get(k).y,2.f)));
                    a.n.add(0);
                }
                agents.add(a);
            }
        for(int i=0;i<agents.size();i++)
            for(int j=0;j<agents.size();j++)
                for(int k=0;k<gates.size();k++)
                    if(agents.get(i).dist.get(k)>agents.get(j).dist.get(k))
                        agents.get(i).n.set(k,agents.get(i).n.get(k)+1);
    }

    public void show()
    {
        for(int i=0;i<agents.size();i++)
        {
            if(i%(this.width-this.acc*2)==0)System.out.println();
            System.out.print(String.format("%-2d",(int)agents.get(i).x)+" "+String.format("%-2d",(int)agents.get(i).y)+" "+String.format("%-2.1f",agents.get(i).dist.get(0))+" "+String.format("%2d",agents.get(i).n.get(0))+"\t\t");
        }
    }

    public void showChosen()
    {
        for(int i=0;i<this.width;i+=this.acc)
            for(int j=0;j<gates.size();j++)
            {
                if(gates.get(j).y==0&&gates.get(j).x==i) {
                    System.out.print("" + j+"  ");
                    break;
                }
                else if(j==gates.size()-1)System.out.print("#  ");
            }
        System.out.println();
        for(int i=0;i<this.height-this.acc*2;i+=this.acc) {
            for (int k = 0; k < gates.size(); k++) {
                if (gates.get(k).x == 0 && gates.get(k).y == i) {
                    System.out.print("" + k+"  ");
                    break;
                }
                else if(k==gates.size()-1)System.out.print("#  ");
            }
            for (int j = 0; j < this.width-this.acc*2; j+=this.acc) {
                System.out.print(String.format("\033[%dm%d\033[0m", agents.get((int)(i*(width-2*this.acc)+j)).choose+31,agents.get((int) (i*(width-2*this.acc)+j)).choose)+"  ");
            }
            for (int k = 0; k < gates.size(); k++) {
                if (gates.get(k).x == this.width-this.acc*1 && gates.get(k).y == i) {
                    System.out.print("" + k + "\n");
                    break;
                }
                else if(k==gates.size()-1)System.out.print("#\n");
            }
        }
        for(int i=0;i<this.width;i+=this.acc)
            for(int j=0;j<gates.size();j++)
            {
                if(gates.get(j).y==this.height-this.acc*1&&gates.get(j).x==i) {
                    System.out.print("" + j+"  ");
                    break;
                }
                else if(j==gates.size()-1)System.out.print("#  ");
            }
    }
}
