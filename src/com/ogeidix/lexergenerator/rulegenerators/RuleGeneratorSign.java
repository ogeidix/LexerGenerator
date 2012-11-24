package com.ogeidix.lexergenerator.rulegenerators;

import java.util.LinkedHashMap;

import com.ogeidix.lexergenerator.LexerNode;
import com.ogeidix.lexergenerator.Token;
import com.ogeidix.lexergenerator.rules.RuleChar;

public class RuleGeneratorSign implements RuleGenerator {

    @Override
    public LexerNode generate(String input, LinkedHashMap<String, Token> tokens) throws Exception {
        LexerNode result = new LexerNode();
        result.add(new RuleChar('+'));
        result.add(new RuleChar('-'));
        return result;
    }

}
