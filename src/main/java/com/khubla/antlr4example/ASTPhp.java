package com.khubla.antlr4example;

import org.antlr.v4.runtime.*;
import java.io.*;

import com.khubla.antlr4example.PhpParser.HtmlDocumentContext;

/**
 * ASTPhp
 */
public class ASTPhp {

    public static String parse(InputStream inputStream) {
        String result = "";

        try {
            /*
             * make Lexer
             */
            Lexer lexer = new PhpLexer(CharStreams.fromStream(inputStream));
            /*
             * get a TokenStream on the Lexer
             */
            TokenStream tokenStream = new CommonTokenStream(lexer);
            /*
             * make a Parser on the token stream
             */
            PhpParser parser = new PhpParser(tokenStream);
            /*
             * get the top node of the AST. This corresponds to the topmost rule of
             * equation.q4, "equation"
             */
            HtmlDocumentContext context = parser.htmlDocument();

            result = HTMLJson.toJson(context);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}