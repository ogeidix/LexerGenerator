package com.ogeidix.lexergenerator;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.ogeidix.lexergenerator.rulegenerators.*;

public class NodeChainFactory {
    static private HashMap<String, RuleGenerator> ruleGenerators = new HashMap<String, RuleGenerator>();

    static {
        ruleGenerators.put("char",                new RuleGeneratorChar());
        ruleGenerators.put("string",              new RuleGeneratorString());
        ruleGenerators.put("anythingUntil",       new RuleGeneratorAnythingUntil());
        ruleGenerators.put("signOrNothing",       new RuleGeneratorSignOrNothing());
        ruleGenerators.put("sign",                new RuleGeneratorSign());
        ruleGenerators.put("digitSequence",       new RuleGeneratorDigitSequence());
        ruleGenerators.put("caseInsensitiveChar", new RuleGeneratorCaseInsensitiveChar());
        ruleGenerators.put("charOrNothing",       new RuleGeneratorCharOrNothing());
        ruleGenerators.put("token",               new RuleGeneratorToken());
        ruleGenerators.put("tokenOrNothing",      new RuleGeneratorTokenOrNothing());
    }

    public static LexerNode create(String generator, String constructor, LinkedHashMap<String, Token> tokens) throws Exception{
        if (ruleGenerators.get(generator) == null) throw new Exception("Rule Generator not found for '"+generator+"'");
        return ruleGenerators.get(generator).generate(constructor, tokens);
    }
}
