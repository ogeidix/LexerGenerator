package com.ogeidix.lexergenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Token {
    private String userDescription;
    private String name;
    private LexerNode node;
    
    public Token(String str) throws Exception {
        userDescription = str;
        parse(userDescription);
    }
    
    public String getName() {
        return name;
    }
    
    public LexerNode getNode() {
        return node;
    }

    public String toString() {
        return this.name + " => " + getNode().toString();
    }
    
    private void parse(String str) throws Exception{
        Pattern p = Pattern.compile("^(\\w+)\\s*=\\s*(.+)");
        Matcher m = p.matcher(str);
        if (!m.find()) throw new Exception("Token definition not correct");
        this.name = m.group(1);
        String[] textRules = m.group(2).split(",\\s*");
        for (String textRule : textRules) {
            Pattern pRule = Pattern.compile("^(\\w+)(\\((\\w+)\\))?");
            Matcher mRule = pRule.matcher(textRule);
            mRule.find();
            node = NodeChainFactory.create(mRule.group(1), mRule.group(3));
            node.appendTokenName(name);
        }
    }

}
