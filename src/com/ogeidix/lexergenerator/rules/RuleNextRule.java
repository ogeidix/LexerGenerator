package com.ogeidix.lexergenerator.rules;


public class RuleNextRule implements Rule {

    public RuleNextRule clone(){
        return new RuleNextRule();
    }
    
    @Override
    public String toString(){
        return "";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof RuleNextRule){
                return true;
        }
        return false;
    }

    @Override
    public String javaAction() {
        return "";
    }

    @Override
    public String javaMatch(String action) {
        StringBuilder result = new StringBuilder();
        result.append("{").append(action).append("}");
        return result.toString();
    }

}
