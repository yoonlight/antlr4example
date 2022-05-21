package com.khubla.antlr4example.ast;

import org.antlr.v4.runtime.*;
import java.io.*;

import com.khubla.antlr4example.Json;
import com.khubla.antlr4example.VisualBasic6Lexer;
import com.khubla.antlr4example.VisualBasic6Parser;
import com.khubla.antlr4example.VisualBasic6Parser.StartRuleContext;

/**
 * ASTPhp
 */
public class Asp {

    public String parse(InputStream inputStream, String outputFile) {
        String result = "";

        try {
            /*
             * make Lexer
             */
            Lexer lexer = new VisualBasic6Lexer(CharStreams.fromStream(inputStream));
            /*
             * get a TokenStream on the Lexer
             */
            TokenStream tokenStream = new CommonTokenStream(lexer);
            /*
             * make a Parser on the token stream
             */
            VisualBasic6Parser parser = new VisualBasic6Parser(tokenStream);
            /*
             * get the top node of the AST. This corresponds to the topmost rule of
             * equation.q4, "equation"
             */
            StartRuleContext context = parser.startRule();

            result = Json.toJson(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}