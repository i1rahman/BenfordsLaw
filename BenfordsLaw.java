import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;

/**
 * Date: May 12, 2021
 * Teacher: Mr. Ho
 * <p>
 * This program takes sales data and determines whether or not fraud has occured using Benford's law.
 * Sales data is passed in as a .csv file and then a graph of the distribution of the leading digit
 * is displayed. Following that, a message is printed that displays whether fraud likely occured. At the
 * end, a .csv file is saved which contains the distribution of leading digits as a percentage, rounded
 * to 1 decimal place. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 * @author Tony Cheng <>
 */
public class BenfordsLaw {

    /**
     * Main method. Allows user to enter the filepath of their sales data and check for fraud within 
     * their sales data. 
     * 
     * @param args Command line arguments will be ignored
     */
    public static void main (String[] args) {

    
        try {

            float[] results = loadSalesData();

            boolean fraud = checkForFraud(results);
            if (fraud == false)
                System.out.println("Fraud likely did not occur.");
            else
                System.out.println("Fraud likely did occur.");

            generateSalesDataFile(results);


        }
        // Catches all exceptions. 
        catch (Exception e) {
            // Prints the exception. 
            System.out.println(e.getMessage());
        
        }
    
    }

    /**
     * This method loads the sale data from the sales.csv file. It takes the first digit of the sales data
     * and tallies the occurences in an array. Then, the number of leading digits is divided by the total to
     * determine their percentage of a total and saved to an array, which is returned.
     *  
     * @return Array of floats containing the percentage of first digits in the sales data. 
     * @throws FileNotFoundException
     */
    public static float[] loadSalesData () throws FileNotFoundException {
        

        Scanner scnr = new Scanner(new FileReader(".\\sales.csv"));

        // check if the file has at least one line. Assume it's the header. Read it, and throw it away.
        if (scnr.hasNextLine())
            scnr.nextLine();

        
        int[] salesData = new int[9];
        float[] salesDataPercent = new float[9];
        
        String line;
        String number;
        char digit;
        int digit_int;

        int total = 0;

        while (scnr.hasNextLine()) {
            line =  scnr.nextLine();
            number = line.split(",[ ]*")[1];
            digit = number.charAt(0);
            digit_int = Character.getNumericValue(digit);
            salesData[digit_int-1] += 1;
            total += 1;
        }

        for (int i = 0; i < salesData.length; i++)
            salesDataPercent[i] = (float) (salesData[i] * 100) / total;

        return salesDataPercent;

    }

    public static void generateChart(float[] salesDataPercent) {
        // salesDataPercent is an array of floats. Index 0 contains the percentage of 1 as a first digit, index 1 contains
        // the percentage of 2 as the first digit, etc. 

        /**
         * Here's the code if you would like to have a numeric representation of the first digit distribution. 
         * 
         * Example output: 
         * 1,31.481482
         * 2,13.827161
         * 3,12.716049
         * 4,11.049383
         * 5,9.012345
         * 6,6.7901235
         * 7,5.6790123
         * 8,4.259259
         * 9,5.185185
         */
        for (int i = 0; i < salesDataPercent.length; i++) {
            System.out.println((i+1) + "," + salesDataPercent[i]);
        }








    }

    /**
     * This method checks the sales data percent array for fraud using Benford's law. 
     * 
     * @param salesDataPercent Array of sales data as a percent of the leading digit. 
     * @return <code> false </code> if fraud likely did not occur; <code> true </code> if
     *         fraud likely did occur. 
     */
    public static boolean checkForFraud(float[] salesDataPercent) {

        if (salesDataPercent[0] >= 29 && salesDataPercent[0] <= 32)
            return false; // fraud likely DID NOT occur
        return true; // fraud likely DID occur

    }

    /**
     * This method generates the sales data file containing the distribution of leading digits as a percentage, rounded
     * to 1 decimal place. This is outputted in the same directory as the program. The outputted file is named "results.csv".
     * 
     * @param salesDataPercent Array of sales data as a percent of the leading digit.
     */
    public static void generateSalesDataFile(float[] salesDataPercent) {

        // Ensure the salesDataPercent array has a value.
        if (salesDataPercent == null) {
            System.out.print("There is no sales data in that file. ");
            return;
        }

        //Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner reader = new Scanner(System.in);

        // Creates a new output file reference.
        File outFile = new File(".\\results.csv");

        // If the file already exists, then it will ask the user if they want to overwrite the file. Otherwise, write the file.
        if (outFile.exists()) {
            System.out.print("File already exists, ok to overwrite? (y/n) ");
            if (!reader.nextLine().startsWith("y"))
                return;
        }

        // Creates a new PrintWriter with the specified file. 
        // Used a resource block so we don't have to close the PrintWriter. 
        try (PrintWriter printWriter = new PrintWriter(outFile)) {
            
            double roundedSalesPercent = 0;
            for (int i = 0; i < salesDataPercent.length; i++) {
                roundedSalesPercent = Math.round(salesDataPercent[i] * 10)/10.0;
                printWriter.println((i+1) + "," + roundedSalesPercent + "%");
            }
    
        }
        // Catches IOException exceptions. 
        catch (IOException e) {       
            System.out.println(e.getMessage()); // Prints the exception.
        }
    }
    

}