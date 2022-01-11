import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.*;
import java.io.*;
/**
 * The CovidCompare class takes in a file that contains the number of new covid cases every day since March
 * from a number of states, and from the United States as a whole. This class uses dynamic time warping to compare 
 * the trend in each state to the national trend. It can return the warped path from any of the states.
 * This is a link to where I found the data:
 * https://covid.cdc.gov/covid-data-tracker/#trends_dailytrendscases
 *
 *
 */
public class CovidCompare
{
    public static void main(String[] args){
        try{
            File file = new File(args[0]);
            Scanner fileReader = new Scanner(file);
            //hashmap stores the state and its array
            HashMap<String,double[]>hm = new HashMap<String,double[]>();
            while(fileReader.hasNextLine()){
                String line = fileReader.nextLine();
                String[] lined = line.split("\t");
                String loc = lined[0].toLowerCase();
                String[] data = Arrays.copyOfRange(lined,1,lined.length-1);
                double[]d = new double[data.length];
                for(int i = 0; i<data.length; i++){
                    d[i] = Double.parseDouble(data[i]);
                }
                
                hm.put(loc,d);
            }
            sortSimilarity(hm);
        }
        catch(Exception e){
            System.out.println("file not found.");
        }
        //292 data points
    }
    
    /**
     * The state method asks the user for the state, then prints the path found through dynamic time warping, along
     * with the total distance.
     */
    private static void state(HashMap<String,double[]> hm){
        System.out.println(" Out of the following states, enter a state whose ");
        System.out.println("data you would like to compare to the nations:");
        System.out.println(hm.keySet());
        Scanner scan = new Scanner(System.in);
        boolean state = false;
        while(!state){
            String userInput = scan.nextLine().toLowerCase();
            if(hm.get(userInput)==null){
                System.out.println("I'm sorry, we don't have data for that state. Please select a state off the list.");
                
            }
            else{
                state = true;
                DTWTable dtw = dtw(userInput,hm);
                System.out.println("Warped path between " + userInput + " and the US:");
                System.out.println(dtw.pathToString());
                System.out.println("Total distance:");
                System.out.println(dtw.getDist());
            }
        }
        
    }
    
    /**
     * The dtw method makes and returns a dtw table comparing the inputted state with the US.
     */
    private static DTWTable dtw(String states,HashMap<String,double[]> hm ){
        double[] us = hm.get("united states");
        double[] state = hm.get(states);
        DTWTable dtw = new DTWTable(us,state);
        return dtw;
    }
    
    /**
     * The sortSimilarity method makes a dtw table for each state and prints the similarity factor found by the table.
     */
    private static void sortSimilarity(HashMap<String,double[]> hm ){
        Set<String> keys = hm.keySet();
        Iterator<String>it = keys.iterator();
        DTWTable dtw;
        String s;
        System.out.println("This is a list of each state and their similarity to the US in terms of covid rates since March:");
        while(it.hasNext()){
            s = it.next();
            if(s.equals("united states")){ //don't print for the united states
                continue;
            }
            double[] us = hm.get("united states");
            double[] state = hm.get(s);
            dtw = new DTWTable(us,state);
            System.out.println(s + ": " + dtw.getDist());
        }
        state(hm);
    }
    
        
    
}
