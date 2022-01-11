import java.util.*;
import java.util.Arrays;
/**
 * The DTWTable class performs dynamic time warping on two inputted arrays.
 */
public class DTWTable
{
    double[] ar1; //first array
    double[] ar2; // second array
    double[][]d; //dtw table
    public DTWTable(double[] n, double[] m){
        ar1 = n;
        ar2 = m;
        d = new double[n.length][m.length];
        dtw();
    }

    /**
     * The dtw method fills the table using dynamic time warping.
     */
    private void dtw(){
        d[0][0] = 0;
        double dist;
        for(int i = 0; i<ar1.length; i++){
            for(int j = 0; j<ar2.length; j++){
                dist = Math.abs(ar1[i]-ar2[j]); //euclidean distance
                d[i][j] = dist + minimum(i,j).getValue(d);
            }
            
        }
    }

    /**
     * The minimum method is used to calculate the minimum values contained in the surrounding squares of the table.
     * This method is used both when filling the table and when finding the warped path.
     * 
     * @param i     index [i][] in the table
     * @param j     index[][j] in the table
     * @return w    the point on the table with the minimum value
     */
    private Point minimum(int i, int j){
        Point w; //will store the indices of the point
        if(i==0&&j==0){
            w = new Point(i,j);
        }
        else if(i==0){
            w = new Point(i,j-1);
        }
        else if(j==0){
            w = new Point(i-1,j);
        }
        else{
            double a = d[i][j-1];
            double b = d[i-1][j];
            double c = d[i-1][j-1];
            if(a<=b && a<=c){
                w = new Point(i,j-1);
            }
            else if(b <=a && b<=c){
                w = new Point(i-1,j);
            }
            else {
                w = new Point(i-1,j-1);
            }
        }
        return w;
    }

    /**
     * The returnPath method returns the warped path.
     */
    public Point[] returnPath(){
        return returnPath(ar1.length-1,ar2.length-1);
    }
    
    /**
     * The returnPath method returns the warped path up to the specified indices.
     * @param indx1     index from array one that you want the path up to.
     * @param indx2     index from array two that you want the path up tp.
     */
    public Point[] returnPath(int indx1, int indx2){
        Point[]wp = new Point[indx1+indx2+1];//maximum path length
        int i = indx1;
        int j = indx2;
        int wpFiller = wp.length-1; //tracks location in path array
        Point w = new Point(i,j);
        wp[wpFiller] = w;
        wpFiller--;
        while(i>0 || j>0){ //until you have reached the origin
            w = minimum(i,j);
            wp[wpFiller] = w;
            wpFiller--;
            i = w.x;
            j = w.y;
        }
        return resize(wp);
    }
    
    /**
     * The printTable() method prints the dtw table.
     */
    public void printTable(){
        String s = "";
        for(int i = 0; i < ar1.length; i++){
            s += i +": ";
            for(int j = 0; j<ar2.length; j++){
                s += d[i][j] + "\t";
            }
            s += "\n";
        }
        System.out.println(s);
    }
    
    /**
     * The returnmatch method takes in a point on an array and return the point on the other array that it is 
     * matched to.
     * @param ar    which array the point is on
     * @param match which point you want to see the match of
     * @return the matched element
     */
    public int returnMatch(double[] ar, int match){
        int a = -1;
        if(Arrays.equals(ar1,ar)){
             a = 1;
             if(match<0 || match>ar1.length){
                 return -1;
                }
            }
        else if(Arrays.equals(ar2,ar)){
            a = 2;
            if(match<0 || match>ar2.length){
                 return -1;
                }
        }
        else{
            return a;
        }
        
        Point[]wp = returnPath();
        int wpCounter = 0;
        Point w = wp[wpCounter];
        int i = 0; 
        int j = 0;
        //go through until you find the point with the correct index
        if(a==1){
            while(i != match){
                w = wp[wpCounter];
                i = w.x;
                j = w.y;
                wpCounter++;
            }
            return j;
        }
        
        else{
            while(j != match){
                w = wp[wpCounter];
                i = w.x;
                j = w.y;
                wpCounter++;
            }
            return i;
        }
        
    }

    private Point[] resize(Point[] wp){
        int nullCounter = 0;
        while(wp[nullCounter]==null){
            nullCounter++;
            if(nullCounter>wp.length){
                break;
            }
        }
        Point[]wpSized = new Point[wp.length-nullCounter];
        for(int i = 0; i<wpSized.length; i++){
            wpSized[i] = wp[i+nullCounter];
        }
        return wpSized;
    }

    /**
     * The pathToString method returns a string representation of the warped path found.
     */
    public String pathToString(){
        Point[] wp = returnPath();
        return Arrays.toString(wp);
    }
    
    /**
     * The pathToString method returns a string representation of the warped path found from the given indices.
     */
    public String pathToString(int indx1,int indx2){
        Point[] wp = returnPath(indx1,indx2);
        return Arrays.toString(wp);
    }
    
    /**
     * The returnVal method returns the value in the table that is contained at the location of the given
     * index on the warped path.
     */
    public double returnVal(double[] ar, int indx){
        //returns the value of the index's location on the warped path
        int a = -1;
        if(Arrays.equals(ar1,ar)){
             a = 1;
             if(indx<0 || indx>ar1.length){
                 return -1;
                }
            }
        else if(Arrays.equals(ar2,ar)){
            a = 2;
            if(indx<0 || indx>ar2.length){
                 return -1;
                }
        }
        else{
            return a;
        }
        int indx2 = returnMatch(ar,indx);
        if(a==1){
            return d[indx][indx2];
        }
        else {
            return d[indx2][indx];
        }
        
    }
    
    /**
     * The getVal method returns the value at a given spot in the table.
     */
    public double getVal(int ind1, int ind2){
        if(ind1<0||ind2<0){
            return -1;
        }
        else if(ind1 >=ar1.length || ind2 >= ar2.length){
            return -1;
        }
        else {
            return d[ind1][ind2];
        }
    }
    
    /**
     * The getDist() method returns the last index in the table.
     */
    public double getDist(){
        return d[ar1.length-1][ar1.length-1];
    }

    /**
     * The Point class stores a point on the warped path.
     */
    public class Point{
        int x;
        int y;
        public Point(int x, int y){
            this.x = x; //x index 
            this.y = y; //y index
        }

        /**
         * The getValue method returns the value at this point on the table
         */
        public double getValue(double[][]d){
            return d[x][y];
        }
        
        /**
         * The getxData method returns the data in the inputted array at spot x.
         */
        public double getxData(){
            return ar1[x];
        }
        
        /**
         * The getyData method returns the data in the inputted array at spot y.
         */
        public double getyData(){
            return ar2[y];
        }

        /**
         * The toString method returns a string representation of the point.
         */
        public String toString(){
            String s = "(" + this.x + "," + this.y + ")";
            return s;
        }
    }
}
