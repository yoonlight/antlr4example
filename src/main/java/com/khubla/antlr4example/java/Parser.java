package com.khubla.antlr4example.java;

import java.io.IOException;
import java.io.InputStream;

import com.khubla.antlr4example.ast.Jsp;

import org.antlr.v4.runtime.CharStreams;

public class Parser {
    final static String classPrefix = "public class Test {";
    final static String classSuffix = "}";
    final static String methodPrefix = "SomeUnknownReturnType f() {";
    final static String methodSuffix = "return noSuchReturnValue; }";

    public static String parseFileWithRetries(InputStream inputStream) throws IOException {
        String originalContent = CharStreams.fromStream(inputStream).toString();
        String content = classPrefix + methodPrefix + originalContent + methodSuffix + classSuffix;

        String parsed = null;
        try {
            parsed = Jsp.parse(content);
        } catch (Exception e) {

            e.printStackTrace();
            try {
                content = classPrefix + methodPrefix + originalContent + methodSuffix + classSuffix;
                parsed = Jsp.parse(content);

            } catch (Exception e2) {

                e2.printStackTrace();
                content = classPrefix + originalContent + classSuffix;
                try {
                    parsed = Jsp.parse(content);
                } catch (Exception e1) {

                    e1.printStackTrace();
                }
            }
        }

        return parsed;
    }
}
