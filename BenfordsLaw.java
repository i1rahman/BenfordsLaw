import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;
import java.text.DecimalFormat;

public class BenfordsLaw {

    public static void main (String[] args) {

    
        try {

            float[] results = loadSalesData();
            generateSalesDataFile(results);


        }
        // Catches all exceptions. 
        catch (Exception e) {
            // Prints the exception. 
            System.out.println(e.getMessage());
        
        }
    
    }

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

    public static void generateSalesDataFile(float[] salesDataPercent) {

        // ensure the customerCollectionData string object has a value.
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