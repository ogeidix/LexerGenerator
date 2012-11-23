package com.ogeidix.lexergenerator;

import static com.ogeidix.lexergenerator.Fixtures.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class LexerNodeAdd {

    @Test
    public void NodeRuleRuleNodeNode() {
        LexerNode node = new LexerNode();
        node.append(rule);
        node.add(rule2);
        node.appendTokenName(token_name);
        assertEquals(" ( " + rule_name +token_tostring + " || " + rule2_name + token_tostring + " ) ", node.toString());
        assertEquals(rule_match+"{"
                        +"\n" + rule_action
                        +"\n" +token_return
                     +"}"
                     +rule2_match+"{"
                        +"\n"+rule2_action
                        +"\n"+token_return
                     +"}"
                     +token_parseerror , node.toJava());
    }
    
    @Test
    public void NodeSwitchCase() {
        LexerNode node = new LexerNode();
        node.append(ruleA);
        node.add(ruleB);
        node.add(ruleC);
        node.appendTokenName(token_name);
        assertEquals(" ( a" + token_tostring + " || b" + token_tostring + " || c" + token_tostring + " ) ", node.toString());
        assertEquals("switch(currentChar){\n" +
                "case 'a':" +
                "\n" + ruleABC_action +
                "\n" + token_return   +
                "case 'b':" +
                "\n" + ruleABC_action +
                "\n" + token_return   +
                "case 'c':" +
                "\n" + ruleABC_action +
                "\n" + token_return   +
                "}"+ token_parseerror , node.toJava());
    }

}
