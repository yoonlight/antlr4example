package com.khubla.antlr4example.ast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;

import com.khubla.antlr4example.HTMLLexer;
import com.khubla.antlr4example.HTMLParser;
import com.khubla.antlr4example.HTMLParser.HtmlDocumentContext;
import com.khubla.antlr4example.json.CSharpHTMLFilter;

public class CsharpHTML {
    public static String parse(String code) throws IOException {
        String result = "";

        String cleaned = code
                .replaceAll("\"<%.*%>\"", "\"\"") // JSP
                .replaceAll("\"<%.*%>.\"", "\"\"")
                .replaceAll("\"<%.*%>\\\"", "\"\"")
                .replaceAll("=<%.*%>", "")
                .replaceAll("\"<%.*%>", "\"")
                .replaceAll("\"<<\"", "\"\"")
                .replaceAll("\'<%.*%>\'", "\'\'")
                .replaceAll("\"<%=.*%>\"", "\"\"");
        // System.out.println(cleaned);
        InputStream inputStream = new ByteArrayInputStream(cleaned.getBytes());
        /*
         * make Lexer
         */
        Lexer HTMLLexer = new HTMLLexer(CharStreams.fromStream(inputStream));
        /*
         * get a TokenStream on the Lexer
         */
        TokenStream HTMLTokenStream = new CommonTokenStream(HTMLLexer);
        /*
         * make a Parser on the token stream
         */
        HTMLParser HTMLParser = new HTMLParser(HTMLTokenStream);
        /*
         * get the top node of the AST. This corresponds to the topmost rule of
         * equation.q4, "equation"
         */

        HtmlDocumentContext HTMLContext = HTMLParser.htmlDocument();

        result = CSharpHTMLFilter.filterScript(HTMLContext);

        return result;
    }
}
