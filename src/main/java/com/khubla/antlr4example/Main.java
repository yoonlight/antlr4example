package com.khubla.antlr4example;

import java.io.IOException;
import java.io.InputStream;

import com.khubla.antlr4example.ast.HTML;
import com.khubla.antlr4example.ast.Php;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

enum Lang {
   java, html, php, asp
}

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
   @Option(names = { "-l", "--lang" }, required = true)
   private static Lang lang;

   public static void main(String[] args) {
      CommandLine.run(new Main(), args);
   }

   @Override
   public void run() {
      try {

         /*
          * get the input file as an InputStream
          */
         File readFile = new File(inputFile);

         InputStream inputStream = new FileInputStream(readFile);
         String result = "";

         if (lang == Lang.html) {
            result = HTML.parse(inputStream);
         } else if (lang == Lang.php) {
            result = Php.parse(inputStream);
         }

         if (printString) {
            System.out.println(result);
         }

         if (writeString) {
            writeFile(outputFile, result);
         }

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void writeFile(String outputFile, String result) throws IOException {
      FileWriter file = new FileWriter(outputFile);
      file.write(result);
      file.flush();
      file.close();
   }

}