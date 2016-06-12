import GEP.RunGEP;
import GEP.Setting;
import Room.RoomGen;
import Tools.Display;
import Tools.Tools;

/**
 * Created by tank on 5/30/16.
 */
public class Evacuate {

    public static void main(String[] args) {
        RunGEP runGEP=new RunGEP();
        runGEP.room=new RoomGen(30,40);
        runGEP.room.addGate(0,10,1);
        runGEP.room.addGate(25,0,1.5f);
        runGEP.room.addGate(15,39,1);
        runGEP.room.addGate(19,39,3.5f);
        runGEP.room.addGate(29,15,2);
        runGEP.room.addGate(5,39,1);
        runGEP.room.agentGen();
        //runGEP.room.show();
        new Setting();
        runGEP.run();
        Result(runGEP);


    }

    private static void Result(RunGEP runGEP)
    {
        System.out.println("\n\033[01;34m[ Gene Expression Programming Result ]\033[0m\n");
        Display.displayChromosome(runGEP.population.chromosomes.get(0));
        Display.displayExpression(runGEP.population.chromosomes.get(0));
        Display.displayMathExpression(runGEP.population.chromosomes.get(0));
        System.out.println("Training Fitness = "+runGEP.population.chromosomes.get(0).trainingFitness);
        System.out.println();
        System.out.println("Runtime = "+runGEP.runtime+" ms");
        RoomGen.Agent a=runGEP.room.agents.get(10);
        System.out.println("d = "+a.dist.get(a.choose)+"\nn = "+a.n.get(a.choose)+"\nchoose = "+a.choose+"\nw = "+runGEP.room.gates.get(a.choose).width);
        System.out.println("test = "+Tools.CalcuFitnessFromData(a,runGEP.room.gates,runGEP.population.chromosomes.get(0).root));
        runGEP.room.showChosen();

    }
}
