package com.khubla.antlr4example.csharp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.khubla.antlr4example.CSharpLexer;
import com.khubla.antlr4example.CSharpParser;
import com.khubla.antlr4example.CSharpParser.Compilation_unitContext;
import com.khubla.antlr4example.csharp.Visitors.FunctionVisitor;
import com.khubla.antlr4example.common.Common.Common;
import com.khubla.antlr4example.common.Common.MethodContent;
import com.khubla.antlr4example.common.FeatureEntities.ProgramFeatures;

public class FeatureExtractor {

    final static String lparen = "(";
    final static String rparen = ")";
    final static String upSymbol = "^";
    final static String downSymbol = "_";

    public ArrayList<ProgramFeatures> extractFeatures(String code) throws IOException {
        Compilation_unitContext context = parseFileWithRetries(code);
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionVisitor functionVisitor = new FunctionVisitor();

        walker.walk(functionVisitor, context);
        ArrayList<MethodContent> methods = functionVisitor.getMethodContents();
        ArrayList<ProgramFeatures> programs = generatePathFeatures(methods);
        return programs;
    }

    public Compilation_unitContext parseFileWithRetries(String code) throws IOException {
        Lexer lexer = new CSharpLexer(CharStreams.fromString(code));
        TokenStream tokenStream = new CommonTokenStream(lexer);
        CSharpParser parser = new CSharpParser(tokenStream);
        Compilation_unitContext context = parser.compilation_unit();
        return context;
    }

    public ArrayList<ProgramFeatures> generatePathFeatures(ArrayList<MethodContent> methods) {
        ArrayList<ProgramFeatures> methodsFeatures = new ArrayList<>();
        for (MethodContent content : methods) {
            ProgramFeatures singleMethodFeatures = generatePathFeaturesForFunction(content);
            if (!singleMethodFeatures.isEmpty()) {
                methodsFeatures.add(singleMethodFeatures);
            }
        }
        return methodsFeatures;
    }

    private ProgramFeatures generatePathFeaturesForFunction(MethodContent methodContent) {
        ArrayList<TerminalNode> functionLeaves = methodContent.getLeaves();
        ProgramFeatures programFeatures = new ProgramFeatures(methodContent.getName());
        for (int i = 0; i < functionLeaves.size(); i++) {
            for (int j = i + 1; j < functionLeaves.size(); j++) {
                String separator = Common.EmptyString;
                TerminalNode source = functionLeaves.get(i);
                TerminalNode target = functionLeaves.get(j);
                if (source.getText() == "(" || source.getText() == ")") {
                    continue;
                }
                if (target.getText() == "(" || target.getText() == ")") {
                    continue;
                }
                String path = generatePath(source, target, separator);
                if (path != Common.EmptyString) {
                    programFeatures.addFeature(source, path, target);
                }
            }
        }

        return programFeatures;
    }

    private static ArrayList<ParseTree> getTreeStack(TerminalNode tree) {
        ArrayList<ParseTree> upStack = new ArrayList<>();
        ParseTree current = tree;
        while (current != null) {
            upStack.add(current);
            current = current.getParent();
        }
        return upStack;
    }

    private String generatePath(TerminalNode source, TerminalNode target, String separator) {
        String down = downSymbol;
        String up = upSymbol;
        String startSymbol = lparen;
        String endSymbol = rparen;

        StringJoiner stringBuilder = new StringJoiner(separator);
        ArrayList<ParseTree> sourceStack = getTreeStack(source);
        ArrayList<ParseTree> targetStack = getTreeStack(target);

        int commonPrefix = 0;
        int currentSourceAncestorIndex = sourceStack.size() - 1;
        int currentTargetAncestorIndex = targetStack.size() - 1;
        while (currentSourceAncestorIndex >= 0 && currentTargetAncestorIndex >= 0
                && sourceStack.get(currentSourceAncestorIndex) == targetStack.get(currentTargetAncestorIndex)) {
            commonPrefix++;
            currentSourceAncestorIndex--;
            currentTargetAncestorIndex--;
        }

        int pathLength = sourceStack.size() + targetStack.size() - 2 * commonPrefix;
        if (pathLength > 8) {
            return Common.EmptyString;
        }

        for (int i = 0; i < sourceStack.size() - commonPrefix; i++) {
            ParseTree currentNode = sourceStack.get(i);
            String childId = Common.EmptyString;

            stringBuilder.add(String.format("%s%s%s%s%s", startSymbol,
                    currentNode.getPayload(), childId, endSymbol, up));
        }

        ParseTree commonNode = sourceStack.get(sourceStack.size() - commonPrefix);
        String commonNodeChildId = Common.EmptyString;

        stringBuilder.add(String.format("%s%s%s%s", startSymbol,
                commonNode.getPayload(), commonNodeChildId, endSymbol));

        for (int i = targetStack.size() - commonPrefix - 1; i >= 0; i--) {
            ParseTree currentNode = targetStack.get(i);
            String childId = Common.EmptyString;
            stringBuilder.add(String.format("%s%s%s%s%s", down, startSymbol,
                    currentNode.getPayload(), childId, endSymbol));
        }

        return stringBuilder.toString();
    }
}
