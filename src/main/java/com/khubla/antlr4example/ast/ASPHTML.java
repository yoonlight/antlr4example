package com.khubla.antlr4example.ast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;

import com.khubla.antlr4example.HTMLLexer;
import com.khubla.antlr4example.HTMLParser;
import com.khubla.antlr4example.HTMLParser.HtmlDocumentContext;
import com.khubla.antlr4example.json.ASPHTMLFilter;

public class ASPHTML {
    // https://stackoverflow.com/questions/15161553/how-to-convert-fileinputstream-into-string-in-java
    public static String getFileContent(
            InputStream fis,
            String encoding) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    public static String parse(InputStream inputStream) {
        String result = "";

        try {
            String cleaned = getFileContent(inputStream, "utf-8")
                .replaceAll("\"<%.*%>\"", "\"\"")
                .replaceAll("\"<%.*%>.\"", "\"\"")
                .replaceAll("\"<%.*%>\\\"", "\"\"")
                .replaceAll("=<%.*%>", "")
                .replaceAll("\"<%.*%>", "\"")
                .replaceAll("\"<<\"", "\"\"")
                .replaceAll("\'<%.*%>\'", "\'\'")
                .replaceAll("\"<%=.*%>\"", "\"\"");
            // System.out.println(cleaned);
            inputStream = new ByteArrayInputStream(cleaned.getBytes());
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

            result = ASPHTMLFilter.filterScript(HTMLContext);

        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return result;
    }
}
