package GEP;

import Room.RoomGen;
import Tools.*;

import java.util.Vector;


/**
 * Created by tank on 4/19/16.
 */
public class Population {


    public Vector<Chromosome> chromosomes=new Vector<>(Setting.NumberOfChromosome);

    private int tail;

    private int head;

    private int sizeOfTree;

    private int numberOfSurvivors;

    private boolean sort=false;

    Population()
    {
        this.numberOfSurvivors =(int)(Setting.NumberOfChromosome*(1-Setting.DropOutRatio));
        head=Setting.HeadSize;

        //TODO: need to be redefined globally after finishing operator operands settings.
        int MaxNumOfOperands=2;
        tail=head*(MaxNumOfOperands-1)+1;
        sizeOfTree=head+tail;
    }

    void firstGeneration(RoomGen room)
    {
        for(int i=0;i<Setting.NumberOfChromosome;i++)
            chromosomes.add(Tools.randomChromosome(head,tail,room));
    }

    void nextGeneration(RoomGen room)
    {
        if(!this.sort)
            sort();
        this.sort=false;
        for(int i = this.numberOfSurvivors; i<this.chromosomes.size(); i++)
        {
            Chromosome child = this.chromosomes.get(i);
            if (!crossing(child)) continue;
            boolean valid=false;
            while(!valid) {
                mutation(child);
                child.Initialize();
                valid=Tools.testValid(child)&&Tools.testVarCorrelation(child,room.agents.get(64),room.gates);
            }
            this.chromosomes.set(i,child);
        }

    }

    private void mutation(Chromosome child)
    {
        for(int i=0;i<child.chromosome.size();i++)
        {
            if(Tools.randomNumber(0,1)<Setting.MutationRatio)
            {
                boolean inHead=i<this.head;
                child.chromosome.set(i,Tools.randomElement(inHead));
            }
        }
    }

    private boolean crossing(Chromosome child)
    {
        Chromosome parent1= this.chromosomes.get((int)Tools.randomNumber(0,this.numberOfSurvivors));
        Chromosome parent2= this.chromosomes.get((int)Tools.randomNumber(0,this.numberOfSurvivors));
        if(parent1==parent2)
            return false;
        int cross=(int)Tools.randomNumber(0,this.sizeOfTree+1);
        child.Clear();

        int i;
        for(i=0;i<cross;i++)
            child.Add(parent1.chromosome.get(i));
        for(;i<this.sizeOfTree;i++)
            child.Add(parent2.chromosome.get(i));
        return true;
    }

    void evolveConstant()
    {
        if(!this.sort)
            sort();

        for(int i = this.numberOfSurvivors; i<this.chromosomes.size(); i++)
        {
            Chromosome child=this.chromosomes.get(i);
            int parent=(int)Tools.randomNumber(0,(float)this.numberOfSurvivors);
            //TODO: maybe it can be changed ?
            //NOTE: this is more like a assimilation ,not just to improve Constant.
            //      The assimilator's will compete with its product next time.
            child.chromosome=this.chromosomes.get(parent).chromosome;
            for(Element e:child.chromosome)
            {
                if(e.getType()==Type.CONSTANT)
                {
                    Tools.refineConstant(e);
                }
            }
            child.Initialize();
        }
        this.sort=false;

    }

    void sort()
    {
        Tools.sortByFitness(chromosomes,0,chromosomes.size()-1);
        sort=true;
    }

}
