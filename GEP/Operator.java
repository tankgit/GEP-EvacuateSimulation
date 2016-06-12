package GEP;

import java.util.Objects;
import java.util.Vector;


/**
 * Created by tank on 4/19/16.
 */
public class Operator{

    public enum operatorType{
        ADDITION,
        MINUS,
        MULTIPLY,
        DIVISION
    }


    private OperatorBase operator;

    private Addition addition=new Addition();

    private Minus minus=new Minus();

    private Multiply multiply=new Multiply();

    private Division division=new Division();

    private float result=0;



    private class OperatorBase{
        private int numOperand;
        private char sign;
        private operatorType type;

        OperatorBase(operatorType type)
        {
            this.type=type;
            switch(this.type)
            {
                case ADDITION:
                    numOperand=2;
                    sign='+';
                    break;
                case MINUS:
                    numOperand=2;
                    sign='-';
                    break;
                case MULTIPLY:
                    numOperand=2;
                    sign='*';
                    break;
                case DIVISION:
                    numOperand=2;
                    sign='/';
                    break;
            }
        }

        public int getNumOperand()
        {
            return this.numOperand;
        }

        public char getSign()
        {
            return this.sign;
        }

        public operatorType getType()
        {
            return this.type;
        }
    }

    private class Addition extends OperatorBase{

        Addition()
        {
            super(operatorType.ADDITION);
        }

        float rule(float a, float b)
        {
            return a+b;
        }
    }

    private class Minus extends OperatorBase{

        Minus(){
            super(operatorType.MINUS);
        }

        float rule(float a, float b) {
            return a-b;
        }
    }

    private class Multiply extends OperatorBase{
        Multiply()
        {
            super(operatorType.MULTIPLY);
        }

        float rule(float a, float b)
        {
            return a*b;
        }
    }

    private class Division extends OperatorBase {
        Division()
        {
            super(operatorType.DIVISION);
        }

        float rule(float a, float b)
        {
            return b==0?Float.MAX_VALUE:a/b;
        }
    }



    public Operator(operatorType type)
    {
        this.operator=new OperatorBase(type);
    }

    public Operator(String sign)
    {
        switch(sign)
        {
            case "+":this.operator=new OperatorBase(operatorType.ADDITION);
                break;
            case "-":this.operator=new OperatorBase(operatorType.MINUS);
                break;
            case "*":this.operator=new OperatorBase(operatorType.MULTIPLY);
                break;
            case "/":this.operator=new OperatorBase(operatorType.DIVISION);
        }
    }

    public float rule(float[] operands)
    {
        if(operands.length!=operator.getNumOperand())
        {
            System.out.println("Number of operands doesn't match!");
            System.exit(0);
        }
        switch(this.operator.getType())
        {
            case ADDITION:result=addition.rule(operands[0],operands[1]);break;
            case MINUS:result=addition.rule(operands[0],operands[1]);break;
            case MULTIPLY:result=multiply.rule(operands[0],operands[1]);break;
            case DIVISION:result=division.rule(operands[0],operands[1]);break;
        }
        return this.result;
    }

    public int getNumOperand()
    {
        return operator.getNumOperand();
    }

    public char getSign()
    {
        return operator.getSign();
    }

    public operatorType getType()
    {
        return operator.getType();
    }
    
	
}
