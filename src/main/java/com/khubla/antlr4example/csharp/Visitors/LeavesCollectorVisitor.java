package com.khubla.antlr4example.csharp.Visitors;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.khubla.antlr4example.CSharpParserBaseVisitor;

public class LeavesCollectorVisitor extends CSharpParserBaseVisitor<TerminalNode> {
    ArrayList<TerminalNode> m_Leaves = new ArrayList<>();

    @Override
    protected TerminalNode aggregateResult(TerminalNode aggregate, TerminalNode nextResult) {
        if (hasNoChildren(nextResult)) {
            if (!nextResult.getText().isEmpty()) {
                m_Leaves.add(nextResult);
            }
        }
        return nextResult;
    }

    @Override
    public TerminalNode visitTerminal(TerminalNode node) {
        return node;
    }

    private boolean hasNoChildren(TerminalNode node) {
        return node.getChildCount() == 0;
    }

    public ArrayList<TerminalNode> getLeaves() {
        return m_Leaves;
    }
}
