package com.khubla.antlr4example;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;

// import com.khubla.antlr4example.PhpParser.HtmlDocumentContext;
import com.khubla.antlr4example.HTMLParser.HtmlDocumentContext;
// import com.khubla.antlr4example.JavaParser.CompilationUnitContext;
// import com.khubla.antlr4example.VisualBasic6Parser.StartRuleContext;

/**
 * @author Tom Everett
 */
class Main {

   public static void main(String[] args) {
      System.out.println("Antlr4 Example");

      try {
         /*
          * get the input file as an InputStream
          */
         InputStream inputStream = Main.class.getResourceAsStream("/example1.txt");
         /*
          * make Lexer
          */
         Lexer HTMLLexer =  new HTMLLexer(CharStreams.fromStream(inputStream));
         /*
          * get a TokenStream on the Lexer
          */
         TokenStream HTMLTokenStream = new CommonTokenStream(HTMLLexer);
         /*
          * make a Parser on the token stream
          */
         HTMLParser HTMLParser = new HTMLParser(HTMLTokenStream);
         /*
          * get the top node of the AST. This corresponds to the topmost rule of equation.q4, "equation"
          */
         // @SuppressWarnings("unused")
         HtmlDocumentContext HTMLContext = HTMLParser.htmlDocument();
         FileWriter file = new FileWriter("employees.json");
         file.write(toJson(HTMLContext));
         file.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}