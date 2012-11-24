package com.ogeidix.lexergenerator.rulegenerators;

import java.util.LinkedHashMap;

import com.ogeidix.lexergenerator.LexerNode;
import com.ogeidix.lexergenerator.Token;
import com.ogeidix.lexergenerator.rules.RuleChar;
import com.ogeidix.lexergenerator.rules.RuleNextRule;

public class RuleGeneratorSignOrNothing implements RuleGenerator {

    @Override
    public LexerNode generate(String input, LinkedHashMap<String, Token> tokens) throws Exception {
        LexerNode result = new LexerNode();
        result.add(new RuleChar('+'));
        result.add(new RuleChar('-'));
        result.add(new RuleNextRule());
        return result;
    }

}
