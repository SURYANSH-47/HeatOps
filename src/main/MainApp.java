package main;

import java.io.*;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("=== Temperature Converter ===");
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter temperature value: ");
            double value = sc.nextDouble();

            System.out.print("Enter temperature type (C / F / K): ");
            String choice = sc.next().toUpperCase();

            String result = "";

            if (choice.equals("C")) {
                result = "Celsius to Fahrenheit: " + ((value * 9 / 5) + 32);
            } else if (choice.equals("F")) {
                result = "Fahrenheit to Celsius: " + ((value - 32) * 5 / 9);
            } else if (choice.equals("K")) {
                result = "Kelvin to Celsius: " + (value - 273.15);
            } else {
                System.out.println("Invalid choice!");
                return;
            }

            // --- THE FIX IS HERE ---
            System.out.println(result);
            System.out.println("HELLOW ORKD");
            
            // Force the terminal to show the text NOW
            System.out.flush(); 

            // Save to File (If this fails, the prints above already happened)
            saveToFile(result);

        } catch (Exception e) {
            System.out.println("\n!!! AN ERROR OCCURRED !!!");
            // This is the most important line for you to see WHY it's failing
            e.printStackTrace(); 
        } finally {
            sc.close();
        }
    }

   public static void saveToFile(String text) {
    
    File file = new File("conversions.txt");

    
    try (FileOutputStream fos = new FileOutputStream(file, true)) {
        
        String dataw = text + "\n";
    
        fos.write(dataw.getBytes());
        
        fos.flush();
        
        System.out.println("Successfully saved using FileOutputStream.");
        

    } catch (IOException e) {
        System.err.println("File Save Error: " + e.getMessage());
    }
}
}