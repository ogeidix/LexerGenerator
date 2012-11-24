package com.ogeidix.lexergenerator;

import java.util.HashMap;
import com.ogeidix.lexergenerator.rulegenerators.RuleGenerator;
import com.ogeidix.lexergenerator.rulegenerators.RuleGeneratorChar;
import com.ogeidix.lexergenerator.rulegenerators.RuleGeneratorDigitSequence;
import com.ogeidix.lexergenerator.rulegenerators.RuleGeneratorString;
import com.ogeidix.lexergenerator.rulegenerators.RuleGeneratorAnythingUntil;
import com.ogeidix.lexergenerator.rulegenerators.RuleGeneratorSignOrNothing;

public class NodeChainFactory {
    static private HashMap<String, RuleGenerator> ruleGenerators = new HashMap<String, RuleGenerator>();

    static {
        ruleGenerators.put("char",          new RuleGeneratorChar());
        ruleGenerators.put("string",        new RuleGeneratorString());
        ruleGenerators.put("anythingUntil", new RuleGeneratorAnythingUntil());
        ruleGenerators.put("signOrNothing", new RuleGeneratorSignOrNothing());
        ruleGenerators.put("digitSequence",       new RuleGeneratorDigitSequence());
    }

    public static LexerNode create(String generator, String constructor) throws Exception{
        if (ruleGenerators.get(generator) == null) throw new Exception("Rule Generator not found for '"+generator+"'");
        return ruleGenerators.get(generator).generate(constructor);
    }
}
