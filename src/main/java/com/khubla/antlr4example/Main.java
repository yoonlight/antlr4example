package com.khubla.antlr4example;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.io.FileWriter;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// import com.khubla.antlr4example.PhpParser.HtmlDocumentContext;
import com.khubla.antlr4example.HTMLParser.HtmlDocumentContext;
// import com.khubla.antlr4example.JavaParser.CompilationUnitContext;
// import com.khubla.antlr4example.VisualBasic6Parser.StartRuleContext;

/**
 * @author Tom Everett
 */
class Main {
   private static final Gson PRETTY_PRINT_GSON = new GsonBuilder().setPrettyPrinting().create();
   private static final Gson GSON = new Gson();

   public static String toJson(ParseTree tree) {
      return toJson(tree, true);
   }

   public static String toJson(ParseTree tree, boolean prettyPrint) {
      return prettyPrint ? PRETTY_PRINT_GSON.toJson(toMap(tree)) : GSON.toJson(toMap(tree));
   }

   public static Map<String, Object> toMap(ParseTree tree) {
      Map<String, Object> map = new LinkedHashMap<>();
      traverse(tree, map);
      return map;
   }

   public static void traverse(ParseTree tree, Map<String, Object> map) {

      if (tree instanceof TerminalNodeImpl) {
         Token token = ((TerminalNodeImpl) tree).getSymbol();
         if (token.getType()==6) {
            map.put("type", token.getType());
            map.put("text", token.getText());
         }
      } else {
         List<Map<String, Object>> children = new ArrayList<>();
         String name = tree.getClass().getSimpleName().replaceAll("Context$", "");
         map.put(Character.toLowerCase(name.charAt(0)) + name.substring(1), children);

         for (int i = 0; i < tree.getChildCount(); i++) {
            Map<String, Object> nested = new LinkedHashMap<>();
            children.add(nested);
            traverse(tree.getChild(i), nested);
         }
      }
   }

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