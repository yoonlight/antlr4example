package com.khubla.antlr4example.common.FeatureEntities;

import java.util.function.Function;

import org.antlr.v4.runtime.tree.TerminalNode;

import com.khubla.antlr4example.common.Common.Common;

public class ProgramRelation {
    private TerminalNode m_Source;
    private TerminalNode m_Target;
    private String m_HashedPath;
    private String m_Path;
    public static Function<String, String> s_Hashes = (s) -> Integer.toString(s.hashCode());

    public ProgramRelation(TerminalNode sourceName, TerminalNode targetName, String path) {
        m_Source = sourceName;
        m_Target = targetName;
        m_Path = path;
        m_HashedPath = s_Hashes.apply(path);
    }

    public String normalizeName(String token) {
        return Common.normalizeName(token, Common.BlankWord);
    }

    public String toString() {
        return String.format("%s,%s,%s", normalizeName(m_Source.toString()), m_HashedPath,
        normalizeName(m_Target.toString()));
    }

    public String getPath() {
        return m_Path;
    }

    public TerminalNode getSource() {
        return m_Source;
    }

    public TerminalNode getTarget() {
        return m_Target;
    }

    public String getHashedPath() {
        return m_HashedPath;

    }
}
