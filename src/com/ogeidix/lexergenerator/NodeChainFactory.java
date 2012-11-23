package com.ogeidix.lexergenerator;

import java.util.HashMap;
import com.ogeidix.lexergenerator.rulegenerators.RuleGenerator;
import com.ogeidix.lexergenerator.rulegenerators.RuleGeneratorString;

public class NodeChainFactory {
    static private HashMap<String, RuleGenerator> ruleGenerators = new HashMap<String, RuleGenerator>();

    static {
        ruleGenerators.put("string", new RuleGeneratorString());
    }

    public static LexerNode create(String generator, String constructor) throws Exception{
        if (ruleGenerators.get(generator) == null) throw new Exception("Rule Generator not found for '"+generator+"'");
        return ruleGenerators.get(generator).generate(constructor);
    }
}
