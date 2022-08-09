package com.khubla.antlr4example.asp.Visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.khubla.antlr4example.VisualBasic6Parser;
import com.khubla.antlr4example.VisualBasic6Parser.FunctionStmtContext;
import com.khubla.antlr4example.VisualBasic6ParserBaseListener;
import com.khubla.antlr4example.common.Common.Common;
import com.khubla.antlr4example.common.Common.MethodContent;

public class FunctionVisitor extends VisualBasic6ParserBaseListener {
    private ArrayList<MethodContent> m_Methods = new ArrayList<>();

    @Override
    public void enterModuleBody(VisualBasic6Parser.ModuleBodyContext node) {
        List<ParseTree> children = node.children;
        ArrayList<TerminalNode> leaves = new ArrayList<>();;
        for (ParseTree child : children) {
            if (child instanceof TerminalNode || child.getChild(0) instanceof FunctionStmtContext) {
                continue;
            }
            LeavesCollectorVisitor leavesCollectorVisitor = new LeavesCollectorVisitor();
            leavesCollectorVisitor.visitDepthFirst(child);
            leaves.addAll(leavesCollectorVisitor.getLeaves());
        }
        if (node != null) {
            m_Methods.add(new MethodContent(leaves, Common.methodName));
        }
    }

    @Override
    public void enterFunctionStmt(FunctionStmtContext node) {
        LeavesCollectorVisitor leavesCollectorVisitor = new LeavesCollectorVisitor();
        leavesCollectorVisitor.visitDepthFirst(node);
        ArrayList<TerminalNode> leaves = leavesCollectorVisitor.getLeaves();

        String normalizedMethodName = Common.normalizeName(node.getText(), Common.BlankWord);
        ArrayList<String> splitNameParts = Common.splitToSubTokens(node.getText());
        String splitName = normalizedMethodName;
        if (splitNameParts.size() > 0) {
            splitName = splitNameParts.stream().collect(Collectors.joining(Common.internalSeparator));
        }

        if (node.block() != null) {
            m_Methods.add(new MethodContent(leaves, splitName));
        }
    }

    public ArrayList<MethodContent> getMethodContents() {
        return m_Methods;
    }
}
