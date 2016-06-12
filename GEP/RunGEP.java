package GEP;

import Room.RoomGen;
import Tools.*;

import java.io.File;
import java.util.Vector;

/**
 * Created by tank on 4/19/16.
 */
public class RunGEP {

    public RoomGen room;

    public Population population=new Population();

    public float bestFitness=Float.MAX_VALUE;

    public int stablePeriod;

    public int unstablePeriod;

    public boolean period;

    public double runtime;




    public void run()
    {
        this.runtime=System.currentTimeMillis();

        createFirstGeneration();

        //TODO: Need to optimize: sometimes the fitness can't converge to a little value.
        if(Setting.ForceFitness==0)
            evolveGeneration();
        else
            while(Setting.ForceFitness<this.bestFitness)
                evolveGeneration();

        if(Setting.HaveConstant)
            evolveConstant();



        this.runtime=System.currentTimeMillis()-this.runtime;
    }


    public void createFirstGeneration()
    {
        population.firstGeneration(room);
        CalcuFitnessForTrainingDataSet();
        population.sort();
    }

    public void evolveGeneration()
    {
        this.stablePeriod=0;
        this.unstablePeriod=Integer.MAX_VALUE;
        this.period=false;
        int progress=100;
        for(int i=0;i<Setting.MaxIterationOfEvolve;i++)
        {
            bestFitness=population.chromosomes.get(0).trainingFitness;
            if(bestFitness==0.0||bestFitness<Setting.ForceFitness)return;
            if((int)(100*(float)i/Setting.MaxIterationOfEvolve)!=progress)
            {
                progress=(int)(100*(float)i/Setting.MaxIterationOfEvolve);
                Display.displayProgressBar("Evolve",progress+1,bestFitness,0);
            }

            population.nextGeneration(room);

            CalcuFitnessForTrainingDataSet();

            mutatePeriod(this.period);
        }
        System.out.println();
        population.sort();
    }

    public void evolveConstant()
    {
        int progress=100;
        for(int i=0;i<Setting.IterationOfConstant;i++)
        {
            bestFitness=population.chromosomes.get(0).trainingFitness;
            if(bestFitness==0.0)return;
            if((int)(100*(float)i/Setting.IterationOfConstant)!=progress)
            {
                progress=(int)(100*(float)i/Setting.IterationOfConstant);
                Display.displayProgressBar("Improve",progress+1,population.chromosomes.get(0).trainingFitness,1);
            }
            population.evolveConstant();
            CalcuFitnessForTrainingDataSet();
        }
        System.out.println();
        population.sort();
    }

    public void mutatePeriod(boolean period)
    {
        if(!period)
        {
            if (bestFitness == population.chromosomes.get(0).trainingFitness)
                this.stablePeriod++;
            else this.stablePeriod = 0;

            if(this.stablePeriod==Setting.MaxStablePeriodRatio*Setting.MaxIterationOfEvolve && unstablePeriod!=0)
            {
                this.unstablePeriod=(int)(this.stablePeriod* Tools.randomNumber(0.f,1.0f));
                this.period=true;
                this.stablePeriod=0;
            }
        }else {
            this.unstablePeriod--;
            if(this.unstablePeriod==0)
                this.period=false;
        }
    }

    private void CalcuFitnessForTrainingDataSet()
    {
        if(this.room.agents.size()>0)
        {
            for(Chromosome chromosome:population.chromosomes)
            {
                chromosome.trainingFitness=0;
                for(RoomGen.Agent agent:this.room.agents)
                {
                        chromosome.CalcuTrainingFitness(agent,room.gates);

                }
                chromosome.trainingFitness /=this.room.agents.size();
            }
        }
    }



}
