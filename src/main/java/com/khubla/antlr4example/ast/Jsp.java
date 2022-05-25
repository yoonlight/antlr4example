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

    public static String parse(InputStream inputStream) {
        String result = "";

        try {
            /*
             * make Lexer
             */
            Lexer lexer = new JavaLexer(CharStreams.fromStream(inputStream));
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
            CompilationUnitContext context = parser.compilationUnit();

            result = Json.toJson(context);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}