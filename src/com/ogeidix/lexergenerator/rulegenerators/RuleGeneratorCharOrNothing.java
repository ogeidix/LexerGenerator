package com.ogeidix.lexergenerator.rulegenerators;

import com.ogeidix.lexergenerator.LexerNode;
import com.ogeidix.lexergenerator.rules.RuleChar;
import com.ogeidix.lexergenerator.rules.RuleNextRule;

public class RuleGeneratorCharOrNothing implements RuleGenerator {

    @Override
    public LexerNode generate(String input) throws Exception {
        LexerNode result = new LexerNode();
        if (input == null || input.length()!=1) throw new Exception("Wrong rule format for generator char: " + input); 
        result.add(new RuleChar(input.charAt(0)));
        result.add(new RuleNextRule());
        return result;
    }

}
