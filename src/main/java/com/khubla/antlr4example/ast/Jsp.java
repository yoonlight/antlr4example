package com.khubla.antlr4example.ast;

import org.antlr.v4.runtime.*;
import java.io.*;

import com.khubla.antlr4example.JavaLexer;
import com.khubla.antlr4example.JavaParser;
import com.khubla.antlr4example.JavaParser.CompilationUnitContext;
import com.khubla.antlr4example.json.Json;

/**
 * ASTPhp
 */
public class Jsp {

    public static String parse(String code) throws Exception {
        String result = "";
        System.out.println(CharStreams.fromString(code));
        try {
            /*
             * make Lexer
             */
            Lexer lexer = new JavaLexer(CharStreams.fromString(code));

            /*
             * get a TokenStream on the Lexer
             */
            TokenStream tokenStream = new CommonTokenStream(lexer);
            /*
             * make a Parser on the token stream
             */
            JavaParser parser = new JavaParser(tokenStream);
            /*
             * get the top node of the AST. This corresponds to the topmost rule of
             * equation.q4, "equation"
             */
            try {
                
                CompilationUnitContext context = parser.compilationUnit();
    
                result = Json.toJson(context);
    
                // System.out.println(result);
            } catch (RecognitionException e) {
                //TODO: handle exception
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}