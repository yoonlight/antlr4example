package com.khubla.antlr4example.json;

import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HTMLJson {
    private static final Gson PRETTY_PRINT_GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson GSON = new Gson();
    public static List<Map<String, Object>> result = new ArrayList<>();
    public static List<String> scriptList = new ArrayList<>();

    public static String filterScript(ParseTree tree) {
        toJson(tree, true);
        return String.join("\n", scriptList);
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
            map.put("type", token.getType());
            map.put("text", token.getText());
            if (token.getType() == 6 || token.getType() == 18) {
                scriptList.add(token.getText());
                addResult(token);
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
