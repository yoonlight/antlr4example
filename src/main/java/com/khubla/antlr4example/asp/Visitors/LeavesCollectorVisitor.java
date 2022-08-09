package com.khubla.antlr4example.asp.Visitors;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class LeavesCollectorVisitor extends AbstractParseTreeVisitor<TerminalNode> {
    ArrayList<TerminalNode> m_Leaves = new ArrayList<>();

    public void visitDepthFirst(ParseTree node) {
        if (node instanceof TerminalNode) {
            process((TerminalNode) node);
        }

        int n = node.getChildCount();
        for (int i = 0; i < n; i++) {
            ParseTree c = node.getChild(i);
            c.getParent();
            visitDepthFirst(c);
        }
    }

    public void process(TerminalNode node) {
        if (hasNoChildren(node)) {
            Integer type = node.getSymbol().getType();
            if (!isWS(type)) {
                m_Leaves.add(node);
            }
        }
    }

    private boolean hasNoChildren(TerminalNode node) {
        return node.getChildCount() == 0;
    }

    public ArrayList<TerminalNode> getLeaves() {
        return m_Leaves;
    }

    public boolean isWS(Integer type) {
        return type == 222 || type == 220;
    }
}
