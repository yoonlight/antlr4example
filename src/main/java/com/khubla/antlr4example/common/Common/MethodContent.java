package com.khubla.antlr4example.common.Common;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.TerminalNode;

public class MethodContent {
    private ArrayList<TerminalNode> leaves;
    private String name;
    private long length;

    public MethodContent(ArrayList<TerminalNode> leaves, String name
    // , long length
    ) {
        this.leaves = leaves;
        this.name = name;
        // this.length = length;
    }

    public ArrayList<TerminalNode> getLeaves() {
        return leaves;
    }

    public String getName() {
        return name;
    }

    public long getLength() {
        return length;
    }
}
