package com.khubla.antlr4example.common.FeatureEntities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.khubla.antlr4example.common.Common.Common;

public class ProgramNode {
    public int Id;
    public String Type;
    public String Name;
    public boolean IsMethodDeclarationName;

    public ProgramNode(String name) {
        Name = name;
        try {
            Name = URLEncoder.encode(name, Common.UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
