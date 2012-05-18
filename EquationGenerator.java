package hello;


import java.util.Random;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mazen
 */
public class EquationGenerator {
    
    public static final int WALL = -8;
    public static final int EMPTY  = -1;
    public static final int BOX = 1;
    public static final int MONSTER = -2;
    public static final int PLAYER = -3;
    public static final int ERROR = -99;
    private Random rand = new Random();
    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int TIMES = 2;
    public static final int OPERATORNUM = 3;
    public int getRandom(int max){
        return nextInt(rand, max);
    }
     public static int nextInt(Random rand,int max){
        if(max == 0) return 0;
        int random = rand.nextInt();
        random = random%max;
        if(random < 0)
            return -1*random;
        return random;     
    }
    public int[] generateEquation(int operation){
        
        int[] equationDigits = new int[10];
        for (int i=0; i<4 ;i++)
            equationDigits[i] = EquationGenerator.nextInt(rand,10);
        int firstNumber = equationDigits[0]*10 + equationDigits[1];
//        System.out.print(firstNumber);
        int secondNumber = equationDigits[2]*10 + equationDigits[3];
//        System.out.print(" "+secondNumber);
        int answer = 0;
        switch(operation){
            case PLUS:
                answer = firstNumber+secondNumber;
                break;
            case MINUS:
               if(secondNumber > firstNumber){
                   answer = secondNumber - firstNumber;
                   for(int i=0;i<2;i++){
                    int temp = equationDigits [2+i];
                    equationDigits[2+i] = equationDigits[0+i];
                    equationDigits[0+i] = temp;
                   } 
               }
               else
                   answer = firstNumber - secondNumber;
                break;
            case TIMES:
                answer = firstNumber * secondNumber;      
        }
//        System.out.println(" "+answer);
        int [] answerDigits = new int[4];
        int answerDigitCount = 0;
        while(answer > 0){
            answerDigits[answerDigitCount++] = answer%10;
            answer/=10;
            
        }
        if(answerDigitCount < 2)answerDigitCount = 2;
        for(int i = answerDigits.length-answerDigitCount ; i<answerDigits.length ; i++)
           equationDigits[i-(answerDigits.length-answerDigitCount)+4] = answerDigits[answerDigits.length - i -1] ;
        for(int i=4+answerDigitCount ; i< equationDigits.length ; i++)
            equationDigits[i] = -1;
       
        int hiddenDigit =EquationGenerator.nextInt(rand,answerDigitCount+4);
        equationDigits[equationDigits.length-1] = hiddenDigit;
//        System.out.println(hiddenDigit+" "+equationDigits[hiddenDigit]);
        return equationDigits;
    }
    
    public static void printMap(int[][] map){
         for(int i=0;i<map.length;i++){
           for(int j=0; j<map[i].length ; j++){
               if(map[i][j] > -1)
                   System.out.print(" ");
               System.out.print(map[i][j]);
                   System.out.print(" ");
           }
           System.out.println("");
       }
    }
    public static String EquationToString(int[] equation,int operation){
             
      String eq = "";
       for(int i = 0 ; i< 2 ;i++){
           if(i != equation[equation.length-1])
             eq+=(equation[i]); 
           else
               eq+="?";
       }
          
        switch(operation){
            case PLUS:
                eq+=("+");
                break;
            case MINUS:
                eq+=("-");
                break;
            case TIMES:
                eq+=("*");
        }
        for(int i=2; i<4 ;i++){
           if(i != equation[equation.length-1])
             eq+=(equation[i]); 
           else
               eq+="?";
        }
        eq+=("=");
        int count = 4;
//        System.out.println(equation[equation.length-1]);
        while(count < equation.length && equation[count] != -1){
            if(count !=equation[equation.length-1])
                eq+=(equation[count]);
            else
                eq+="?";
            count++ ;
        }
        return eq;
    }
    public static void main(String [] args){
       EquationGenerator mg = new EquationGenerator();
       int operation = mg.rand.nextInt()%3;
       int [] equation = mg.generateEquation(operation);
       EquationToString(equation, operation);

     }
}
