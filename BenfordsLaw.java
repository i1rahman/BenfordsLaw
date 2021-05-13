import java.io.FileNotFoundException;
import java.util.Scanner;
import org.jfree.ui.RefineryUtilities;

import java.io.*;

/**
 * Date: May 12, 2021
 * Teacher: Mr. Ho
 * <p>
 * This program takes sales data and determines whether or not fraud has occured using Benford's law.
 * Sales data is passed in as a .csv file and then a chart of the distribution of the leading digit
 * is displayed. Following that, a message is printed that displays whether fraud likely occured. At the
 * end, a .csv file is saved which contains the distribution of leading digits as a percentage, rounded
 * to 1 decimal place. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class BenfordsLaw {

    /**
     * Main method. Allows user to enter the filepath of their sales data and check for fraud within 
     * their sales data. 
     * 
     * @param args Command line arguments will be ignored
     */
    public static void main (String[] args) {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner reader = new Scanner(System.in);
        char inputOption = ' ';
        final char LOADCSV = '1';
        final char ANALYSIS = '2'; 
        final char QUIT = '3';

        float[] salesData = null;

        do{
            printMenu();

            // get the menu input.
            inputOption = reader.next().charAt(0); 
            try {

                // Load the CSV and generate statistcs. 
                if (inputOption == LOADCSV) {
                    String salesPath = getFile ("Please enter the path of your sales file ", "sales.csv");
                    salesData = loadSalesData(salesPath);
                    System.out.println ("Sales data loaded from " + salesPath);
                } 
                
                // Analyze the sales data, generate the chart, and export the results to the screen and file.
                else if (inputOption == ANALYSIS) {
                    generateChart (salesData);
                    printNumericDisplay(salesData);
                    checkForFraud(salesData);
                    generateSalesDataFile(salesData, ".\\results.csv");
                }        
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (inputOption != QUIT);

        reader.close();
    }

    /**
     * Prints the menu
     */
    public static void printMenu(){
        System.out.print("\nSales Analysis System\n"
        .concat("1. Load sales data CSV\n")
        .concat("2. Analyze sales data and Export\n")
        .concat("3. Quit\n")
        .concat("Enter menu option (1-3): ")
        );
    }

    /**
     * This method is used to get the file path for loading the Sales data. It also validates that the file exists.
     * 
     * @param message String of a prompt message to ask user to enter the file name.
     * @param defaultFilename String of the default filename. Allows the user to press enter to accept the default.
     * @return String of the file path of the sales data.
     */
    public static String getFile (String message, String defaultFilename) {
        Scanner scnr = new Scanner(System.in);
        do {
            System.out.print(message + "[Enter for "+ defaultFilename +"]: ");
            String input = scnr.nextLine();

            // Allow the user to press Enter to accept the default filename.
            if (input.equals("")) {
                input = ".\\" + defaultFilename; 
            }
            
            File inFile = new File(input);

            // check to see if the file exists. If it exists, return the filename. If it doesn't, allow re-entry.
            if (inFile.exists()) {
                return input;
            }
            System.out.println("The file " + input + " does not exist.");
        } while (true);  
    }


    /**
     * This method loads the sale data from the sales.csv file. It takes the first digit of the sales data
     * and tallies the occurences in an array. Then, the number of leading digits is divided by the total to
     * determine their percentage of a total and saved to an array, which is returned.
     *  
     * @return Array of floats containing the percentage of first digits in the sales data. 
     * @throws FileNotFoundException Signals that an attempt to open a file denoted by a specified pathname has failed. 
     */
    public static float[] loadSalesData (String salesPath) throws FileNotFoundException {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scnr = new Scanner(new FileReader(salesPath));

        // check if the file has at least one line. Assume it's the header. Read it, and throw it away.
        if (scnr.hasNextLine())
            scnr.nextLine();

        // Creates an array of integers to tally how many of each digit occur.
        int[] salesData = new int[9];
        
        // Creates an array of floats to store the percentage of digit occurrences. 
        float[] salesDataPercent = new float[9];
        
        // Holds the line of sales data.
        String line;

        // Holds the sales number
        String number;

        // Holds the leading digit in the sales number as a char. 
        char digit;

        // Holds the leading digit in the sales number as an integer. 
        int digit_int;

        // tallies the total amount of sales numbers.
        int total = 0;

        // While there's another line, this will continue to get the leading digit and assign it's integer value to an array,
        // then keep the total amount of all digits. 
        while (scnr.hasNextLine()) {
            line =  scnr.nextLine();
            number = line.split(",[ ]*")[1];
            digit = number.charAt(0);
            digit_int = Character.getNumericValue(digit);
            salesData[digit_int-1] += 1;
            total += 1;
        }

        // For loop that goes through each index in the salesData array and calculates the percentage of the leading digit. 
        // Then, adds the percentage to the salesDataPercent array. 
        for (int i = 0; i < salesData.length; i++)
            // salesDataPercent is an array of floats. Index 0 contains the percentage of 1 as a first digit, index 1 contains
            // the percentage of 2 as the first digit, etc.
            salesDataPercent[i] = (float) (salesData[i] * 100) / total;

        // returns the salesDataPercent array. 
        return salesDataPercent;

    }

    /**
     * Generates a bar chart using the JFreeChart API of the distribution of leading digit in the sales data. 
     * 
     * @param salesDataPercent Array of sales data as a percent of the leading digit.
     * @throws Exception Signals that sales data is not loaded.
     */
    public static void generateChart(float[] salesDataPercent) throws Exception {
         
        // Make sure sales data is loaded. If sales data is null, then a throw an exception. 
        if (salesDataPercent == null)
            throw new Exception("generateChart: Sales data is not loaded");

        // Create a new BenfordBarChart object. 
        BenfordBarChart chart = new BenfordBarChart("Benford's Law Distribution Leading Digit", "Digit", "Percent", salesDataPercent);
        chart.pack(); 
        RefineryUtilities.centerFrameOnScreen(chart);        
        chart.setVisible(true); 

    }

    /**
     * Prints the numberical representation of the distribution of leading digit in the sales data. 
     * 
     * @param salesDataPercent
     * @throws Exception Signals that sales data is not loaded.
     */
    public static void printNumericDisplay(float[] salesDataPercent) throws Exception {

        // Make sure sales data is loaded. If sales data is null, then a throw an exception.
        if (salesDataPercent == null)
            throw new Exception("printNumericDisplay: Sales data is not loaded");

        // Holds the rounded sales percentage as a double. 
        double roundedSalesPercent = 0;

        System.out.println(); // newline for formatting.

        // For loop goes through every index in the array and rounds the value to 1 decimal place. Then, it adds the number it
        // represents at the beginning and a percentage sign at the end. 
        for (int i = 0; i < salesDataPercent.length; i++) {

            // rounds sales data percentage to 1 decimal place.
            roundedSalesPercent = Math.round(salesDataPercent[i] * 10)/10.0;

            // Prints the rounded sales data percentage with the number it represents and a percentage sign at the end. 
            System.out.println((i+1) + " = " + roundedSalesPercent + "%");
        }

    }

    /**
     * This method checks the sales data percent array for fraud using Benford's law. 
     * 
     * @param salesDataPercent Array of sales data as a percent of the leading digit. 
     * @return <code> false </code> if fraud likely did not occur; <code> true </code> if
     *         fraud likely did occur. 
     */
    public static void checkForFraud(float[] salesDataPercent) throws Exception {

        // Make sure sales data is loaded. If sales data is null, then a throw an exception.
        if (salesDataPercent == null)
            throw new Exception("checkForFraud: Sales data is not loaded");

        // Fraud = True if salesDataPercent is not between 29-32
        if (salesDataPercent[0] >= 29 && salesDataPercent[0] <= 32) // fraud not likely to have occured
            System.out.println("\nFraud likely did not occur.");
        else // fraud likely did occur
            System.out.println("\nFraud likely did occur.");
    }

    /**
     * This method generates the sales data file containing the distribution of leading digits as a percentage, rounded
     * to 1 decimal place. This is outputted in the same directory as the program. The outputted file is named "results.csv".
     * 
     * @param salesDataPercent Array of sales data as a percent of the leading digit.
     */
    public static void generateSalesDataFile(float[] salesDataPercent, String exportFilename) throws Exception {
        

        // Make sure sales data is loaded. If sales data is null, then a throw an exception.
        if (salesDataPercent == null)
            throw new Exception("generateSalesDataFile: Sales data is not loaded");

        //Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner reader = new Scanner(System.in);

        // Creates a new output file reference.
        File outFile = new File(exportFilename);

        // If the file already exists, then it will ask the user if they want to overwrite the file. Otherwise, write the file.
        if (outFile.exists()) {
            System.out.print(exportFilename + " file already exists. Overwrite? (y/n) ");
            if (!reader.nextLine().toLowerCase().startsWith("y"))
            {   
                System.out.println(exportFilename + " exists already and will not be overwritten.");
                return;
            }
        }

        // Creates a new PrintWriter with the specified file. 
        // Used a resource block so we don't have to close the PrintWriter. 
        try (PrintWriter printWriter = new PrintWriter(outFile)) {
            
            // Holds the rounded sales percentage as a double. 
            double roundedSalesPercent = 0;

            // For loop goes through every index in the array and rounds the value to 1 decimal place. Then, it adds the number it
            // represents at the beginning and a percentage sign at the end. This gets added to the results.csv file. 
            for (int i = 0; i < salesDataPercent.length; i++) {

                // rounds sales data percentage to 1 decimal place.
                roundedSalesPercent = Math.round(salesDataPercent[i] * 10)/10.0;

                // Prints the rounded sales data percentage with the number it represents and a percentage sign at the end.
                printWriter.println((i+1) + "," + roundedSalesPercent + "%");
            }
            System.out.println("Results export to " + exportFilename);
        }
        // Catches IOException exceptions. 
        catch (IOException e) {       
            System.out.println(e.getMessage()); // Prints the exception.
        }
    }
    

}