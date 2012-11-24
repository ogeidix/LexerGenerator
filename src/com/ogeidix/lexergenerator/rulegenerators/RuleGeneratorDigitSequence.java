package com.ogeidix.lexergenerator.rulegenerators;

import java.util.LinkedHashMap;

import com.ogeidix.lexergenerator.LexerNode;
import com.ogeidix.lexergenerator.Token;
import com.ogeidix.lexergenerator.rules.RuleDigitSequence;

public class RuleGeneratorDigitSequence implements RuleGenerator {

    @Override
    public LexerNode generate(String input, LinkedHashMap<String, Token> tokens) throws Exception {
        LexerNode result = new LexerNode(); 
        result.append(new RuleDigitSequence());
        return result;
    }

}
