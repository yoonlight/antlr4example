package com.khubla.antlr4example.csharp.Visitors;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.khubla.antlr4example.CSharpParser.Method_declarationContext;
import com.khubla.antlr4example.CSharpParserBaseListener;
import com.khubla.antlr4example.common.Common.Common;
import com.khubla.antlr4example.common.Common.MethodContent;

public class FunctionVisitor extends CSharpParserBaseListener {
    private ArrayList<MethodContent> m_Methods = new ArrayList<>();

    @Override
    public void enterMethod_declaration(Method_declarationContext node) {
        LeavesCollectorVisitor leavesCollectorVisitor = new LeavesCollectorVisitor();
        leavesCollectorVisitor.visitDepthFirst(node);
        ArrayList<TerminalNode> leaves = leavesCollectorVisitor.getLeaves();

        if (node.method_body() != null) {
            m_Methods.add(new MethodContent(leaves, Common.methodName));
        }
    }

    public ArrayList<MethodContent> getMethodContents() {
        return m_Methods;
    }
}
