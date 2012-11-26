package com.ogeidix.lexergenerator;

import static com.ogeidix.lexergenerator.Fixtures.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.ogeidix.lexergenerator.rules.RuleEpsilon;

public class LexerNodeAppendNode {

    @Test
    public void AppendIsMergeIfNoActions() throws Exception {
        LexerNode node = new LexerNode();
        LexerNode node2 = new LexerNode();
        node2.append(createRule("rule"));
        node2.appendTokenName(token_name);
        node.append(node2);
        assertEquals("rule_clone! ", node.toString());
    }

    @Test
    public void AppendIsAppend() throws Exception {
        LexerNode node = new LexerNode();
        node.append(createRule("A"));
        LexerNode node2 = new LexerNode();
        node2.append(createRule("rule"));
        node2.appendTokenName(token_name);
        node.append(node2);
        assertEquals("Arule_clone! ", node.toString());
    }
    
    @Test
    public void AppendedNodesAreCloned() throws Exception {
        LexerNode node = new LexerNode();
        node.append(createRule("A"));
        node.appendTokenName(token_name);
        LexerNode node2 = new LexerNode();
        node2.append(createRule("B"));
        node2.appendTokenName(token2_name);
        node.append(node2);
        
        assertEquals("A! B_clone! ", node.toString());
        
        LexerNode node3 = new LexerNode();
        node3.append(createRule("C"));
        node3.append(createRule("D"));
        node3.appendTokenName(token2_name);
        node.append(node3);
        
        assertEquals("A! B_clone! C_cloneD_clone! ", node.toString());
    }

    @Test
    public void EpsilonRuleDoesNotPropagateAppended() throws Exception {
        LexerNode node = new LexerNode();
        node.append(new RuleEpsilon());
        LexerNode node2 = new LexerNode();
        node2.append(createRule("A"));
        node2.appendTokenName(token2_name);
        node.append(node2);
        assertEquals("A_clone! ", node.toString());
    }

    @Test
    public void EpsilonRuleIsRemovedAndIssueMerge() throws Exception {
        LexerNode node = new LexerNode();
        node.append(new RuleEpsilon());
        LexerNode node2 = new LexerNode();
        node2.append(createRule("A"));
        node2.appendTokenName(token2_name);
        node.append(node2);
        node.add(new RuleEpsilon());
        node.append(node2);
        assertEquals(" ( A_clone! A_clone!  || A_clone!  ) ", node.toString());
  }
    
}
