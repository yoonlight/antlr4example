package com.khubla.antlr4example;

import java.io.IOException;
import java.io.InputStream;

import com.khubla.antlr4example.ast.HTML;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * @author Tom Everett
 */
@Command
class Main implements Runnable {
   @Option(names = { "-i", "--input" })
   private static String inputFile = "example.asp";
   @Option(names = { "-o", "--output" })
   private static String outputFile = "asp.json";
   @Option(names = { "-p", "--print" })
   private static boolean printString;
   @Option(names = { "-w", "--write" })
   private static boolean writeString;

   public static void main(String[] args) {
      CommandLine.run(new Main(), args);

      try {

         /*
          * get the input file as an InputStream
          */
         File readFile = new File(inputFile);

         InputStream inputStream = new FileInputStream(readFile);
         String result = HTML.parse(inputStream);
         if (printString) {
            System.out.println(result);
         }
         if (writeString) {

            try (FileWriter file = new FileWriter(outputFile)) {
               file.write(result);
               file.flush();
            }
         }

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void run() {

      System.out.println("ANTLR Command Line Interface");
   }

}