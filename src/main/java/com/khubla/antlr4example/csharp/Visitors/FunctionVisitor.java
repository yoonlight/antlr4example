package com.khubla.antlr4example.csharp.Visitors;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
        leavesCollectorVisitor.visitMethod_declaration(node);
        ArrayList<TerminalNode> leaves = leavesCollectorVisitor.getLeaves();

        String normalizedMethodName = Common.normalizeName(node.getText(), Common.BlankWord);
		ArrayList<String> splitNameParts = Common.splitToSubTokens(node.getText());
		String splitName = normalizedMethodName;
		if (splitNameParts.size() > 0) {
			splitName = splitNameParts.stream().collect(Collectors.joining(Common.internalSeparator));
		}

        if (node.method_body() != null) {
            m_Methods.add(new MethodContent(leaves, splitName));
        }
    }

    public ArrayList<MethodContent> getMethodContents() {
        return m_Methods;
    }
}
