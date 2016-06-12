package GEP;

import Room.RoomGen;
import Tools.Tools;

import java.util.Vector;
/**
 * Created by tank on 4/19/16.
 */
public class Chromosome{
    
    public Vector<Element> chromosome=new Vector<>();
    
    public float trainingFitness;

    public float testFitness;
    
    public GeneNode root;
    
    public Chromosome()
    {
        this.trainingFitness =0;
        this.root=null;
    }
    
    public void Add(Element element)
    {
        this.chromosome.add(element);
    }
    
    public void Clear()
    {
        this.chromosome.clear();
        this.root=null;
    }
    
    public void Initialize()
    {
        this.trainingFitness =0;
        this.testFitness=0;
        this.root= Tools.CreateTree(this.chromosome);
    }
    
    public void CalcuTrainingFitness(RoomGen.Agent agent,Vector<RoomGen.Gate> gates)
    {
        this.trainingFitness +=Tools.CalcuFitnessFromData(agent,gates,this.root);

        if(!(this.trainingFitness>=0))
            this.trainingFitness=Float.MAX_VALUE;
    }

}