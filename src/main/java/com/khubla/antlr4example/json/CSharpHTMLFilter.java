package com.khubla.antlr4example.json;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CSharpHTMLFilter {
    private static final Gson PRETTY_PRINT_GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson GSON = new Gson();
    public static List<Map<String, Object>> result = new ArrayList<>();
    public static List<String> scriptList = new ArrayList<>();
    public static List<String> methodList = new ArrayList<>();
    final static String classPrefix = "public class Test {";
    final static String classSuffix = "}";
    final static String methodPrefix = "void f() {";
    final static String methodSuffix = "}";

    public static String filterScript(ParseTree tree) {
        toJson(tree, true);
        String method = String.join("\n", scriptList);
        String definedMethod = String.join("\n", methodList);
        method = classPrefix + methodPrefix + method + methodSuffix + definedMethod + classSuffix;
        return method;
    }

    public static String toJson(ParseTree tree) {
        toJson(tree, true);
        return PRETTY_PRINT_GSON.toJson(result);
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
            String text = token.getText();
            Integer type = token.getType();
            map.put("type", type);
            map.put("text", text);

            if (type == 18) {
                String scriptOpenTag = tree.getParent().getChild(0).getText();
                boolean isServerCode = scriptOpenTag.equals("<script runat=\"server\">");
                if (isServerCode) {
                    String result = text.replaceAll("</script>", "");
                    methodList.add(result);
                    addResult(token);
                }
            }
            Boolean cond5 = text.contains("<%--");
            if (cond5) {
                return;
            }
            if (type == 6) {
                Boolean cond1 = text.contains("<%@");
                Boolean cond3 = text.contains("<%!");
                Boolean cond4 = text.contains("<%=");
                if (cond1) {
                    return;
                }
                if (cond4) {
                    String result = text
                            .replaceAll("<%=", "Response.Write(")
                            .replaceAll("%>", ");");
                    scriptList.add(result);
                    addResult(token);
                }
                if (cond3) {
                    String result = text
                            .replaceAll("<%!", "")
                            .replaceAll("%>", "");
                    methodList.add(result);
                    addResult(token);
                }
                if (!cond1 && !cond3 && !cond4) {
                    String result = text
                            .replaceAll("<%", "")
                            .replaceAll("%>", "");
                    scriptList.add(result);
                    addResult(token);
                }
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

    public static void addResult(Token token) {
        Map<String, Object> nested = new LinkedHashMap<>();
        nested.put("text", token.getText());
        result.add(nested);
    }
}
