package com.khubla.antlr4example;

import java.io.IOException;
import java.io.InputStream;

import com.khubla.antlr4example.ast.HTML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * @author Tom Everett
 */
class Main {

   public static void main(String[] args) {
      try {
         String inputFile = "example.asp";
         String outputFile = "asp.json";

         if (args.length == 2) {
            inputFile = args[0];
            outputFile = args[1];
         }
         /*
          * get the input file as an InputStream
          */
         File readFile = new File(inputFile);

         InputStream inputStream = new FileInputStream(readFile);
         String result = HTML.parse(inputStream);

         System.out.println(result);

         try (FileWriter file = new FileWriter(outputFile)) {
            file.write(result);
            file.flush();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}