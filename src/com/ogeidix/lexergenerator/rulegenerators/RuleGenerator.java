package com.ogeidix.lexergenerator.rulegenerators;

import java.util.LinkedHashMap;

import com.ogeidix.lexergenerator.LexerNode;
import com.ogeidix.lexergenerator.Token;

public interface RuleGenerator {
    public LexerNode generate(String input, LinkedHashMap<String, Token> tokens) throws Exception;
}
