package com.ogeidix.lexergenerator.rulegenerators;

import java.util.LinkedHashMap;

import com.ogeidix.lexergenerator.LexerNode;
import com.ogeidix.lexergenerator.Token;
import com.ogeidix.lexergenerator.rules.RuleNextRule;

public class RuleGeneratorTokenOrNothing implements RuleGenerator {

    @Override
    public LexerNode generate(String input, LinkedHashMap<String, Token> tokens) throws Exception {
        Token existingToken = tokens.get(input);
        if (existingToken==null) throw new Exception("Rule generator tokenOrNothing, cannot find previous token: " + input);
        LexerNode node = existingToken.getNode().clone();
        node.add(new RuleNextRule());
        node.removeTokensName();
        return node;
    }

}
