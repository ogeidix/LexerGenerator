package com.ogeidix.lexergenerator;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.ogeidix.lexergenerator.rules.*;

public class LexerNode {
    private static final String TOKEN_PREFIX = "TOKEN_";
    private LinkedHashMap<Rule, LexerNode> actions = new LinkedHashMap<Rule, LexerNode>();
    private String finalTokenName;
    private Set<String> ongoingParsing = new HashSet<String>();

    public void appendTokenName(String name) {
        if (actions.size() == 0) {
            this.finalTokenName = name;
        } else {
            ongoingParsing.add(TOKEN_PREFIX + name);
            for (Map.Entry<Rule, LexerNode> action : actions.entrySet()) {
                action.getValue().appendTokenName(name);
            }
        }
    }

    public void add(Rule newRule) {
        if (actions.get(newRule) == null) {
            actions.put(newRule, new LexerNode());
        }
    }

    public void append(Rule newRule) {
        if (actions.size() == 0) {
            add(newRule);
        } else {
            for (Map.Entry<Rule, LexerNode> action : actions.entrySet()) {
                action.getValue().append(newRule);
            }
        }
    }

    public void merge(LexerNode newNode) throws Exception {
        for (Map.Entry<Rule, LexerNode> action : newNode.actions.entrySet()) {
            if (this.actions.get(action.getKey()) == null) {
                this.actions.put(action.getKey(), action.getValue());
            } else {
                this.actions.get(action.getKey()).merge(action.getValue());
            }
        }
        if (newNode.finalTokenName != null){
            if (this.finalTokenName == null){
                this.finalTokenName = newNode.finalTokenName;
            } else {
                throw new Exception("Rule conflict");
            }
        }
        for(String ongoing : newNode.ongoingParsing){
            this.ongoingParsing.add(ongoing);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (finalTokenName!=null)
            result.append("! ");
        if (actions.size() == 1)
            result.append(actions.keySet().toArray()[0].toString() + actions.values().toArray()[0].toString());
        if (actions.size() > 1) {
            result.append(" ( ");
            for (Map.Entry<Rule, LexerNode> action : actions.entrySet()) {
                if (result.length() != 3) {
                    result.append(" || ");
                }
                result.append(action.getKey().toString());
                result.append(action.getValue().toString());
            }
        }
        return result.toString();
    }

    public String toJava() {
        StringBuffer result = new StringBuffer();
        int singleCharRules = 0;
        for (Map.Entry<Rule, LexerNode> action : actions.entrySet()) {
            if (action.getKey() instanceof RuleChar) singleCharRules++;
        }
        if (singleCharRules>2){
            result.append(toJavaSingleCharRules());
            result.append(toJavaComplexRules(false));            
        } else {
            result.append(toJavaComplexRules(true));
        }
        if (this.finalTokenName != null) {
            result.append("return " + TOKEN_PREFIX + finalTokenName + ";\n");
        } else {
            if (ongoingParsing != null){
                StringBuilder ongoingParsingArgs = new StringBuilder();
                for (String token : ongoingParsing){
                    ongoingParsingArgs.append(token);
                    ongoingParsingArgs.append(",");
                }
                ongoingParsingArgs.deleteCharAt(ongoingParsingArgs.length()-1);
                result.append("return parseError(" + ongoingParsingArgs + ");\n");
            }
        }
        return result.toString();
    }

    private String toJavaSingleCharRules(){
        StringBuffer result = new StringBuffer();
        result.append("switch(currentChar){");
        for (Map.Entry<Rule, LexerNode> action : actions.entrySet()) {
            RuleChar rule = (RuleChar) action.getKey();
            result.append("case '" + rule.expectedChar() + "':\n");
            result.append(rule.javaAction());
            result.append(action.getValue().toJava());
        }
        result.append("}");
        return result.toString();        
    }
    
    private String toJavaComplexRules(boolean all){
        StringBuffer result = new StringBuffer();
        for (Map.Entry<Rule, LexerNode> action : actions.entrySet()) {
            if (all || !(action.getKey() instanceof RuleChar)){
                result.append(action.getKey().javaMatch(action.getValue().toJava()));
            }
        }
        return result.toString();
    }
    
    
}
