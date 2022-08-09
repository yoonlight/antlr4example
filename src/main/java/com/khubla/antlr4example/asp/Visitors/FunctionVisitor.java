package com.khubla.antlr4example.asp.Visitors;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.khubla.antlr4example.VisualBasic6Parser;
import com.khubla.antlr4example.VisualBasic6Parser.FunctionStmtContext;
import com.khubla.antlr4example.VisualBasic6Parser.SubStmtContext;
import com.khubla.antlr4example.VisualBasic6ParserBaseListener;
import com.khubla.antlr4example.common.Common.Common;
import com.khubla.antlr4example.common.Common.MethodContent;

public class FunctionVisitor extends VisualBasic6ParserBaseListener {
    private ArrayList<MethodContent> m_Methods = new ArrayList<>();

    @Override
    public void enterModuleBody(VisualBasic6Parser.ModuleBodyContext node) {
        List<ParseTree> children = node.children;
        ArrayList<TerminalNode> leaves = new ArrayList<>();
        for (ParseTree child : children) {
            if (child instanceof TerminalNode || child.getChild(0) instanceof FunctionStmtContext
                    || child.getChild(0) instanceof SubStmtContext) {
                continue;
            }
            LeavesCollectorVisitor leavesCollectorVisitor = new LeavesCollectorVisitor();
            leavesCollectorVisitor.visitDepthFirst(child);
            leaves.addAll(leavesCollectorVisitor.getLeaves());
        }
        if (node != null && leaves.size() != 0) {
            m_Methods.add(new MethodContent(leaves, "MAIN_" + Common.methodName));
        }
    }

    @Override
    public void enterFunctionStmt(FunctionStmtContext node) {
        LeavesCollectorVisitor leavesCollectorVisitor = new LeavesCollectorVisitor();
        leavesCollectorVisitor.visitDepthFirst(node);
        ArrayList<TerminalNode> leaves = leavesCollectorVisitor.getLeaves();

        if (node.block() != null) {
            m_Methods.add(new MethodContent(leaves, Common.methodName));
        }
    }
    @Override
    public void enterSubStmt(SubStmtContext node) {
        LeavesCollectorVisitor leavesCollectorVisitor = new LeavesCollectorVisitor();
        leavesCollectorVisitor.visitDepthFirst(node);
        ArrayList<TerminalNode> leaves = leavesCollectorVisitor.getLeaves();

        if (node.block() != null) {
            m_Methods.add(new MethodContent(leaves, Common.methodName));
        }
    }

    public ArrayList<MethodContent> getMethodContents() {
        return m_Methods;
    }
}
