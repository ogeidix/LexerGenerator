package com.ogeidix.lexergenerator.rules;


public class RuleChar implements Rule {
    
    private char expected;
    
    public RuleChar(char expected){
        this.expected = expected;
    }

    public String toString(){
        return String.valueOf(expected);
    }
    
    public char expectedChar(){
        return expected;
    }

    @Override
    public int hashCode() {
        return (int) expected;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof RuleChar){
            if (((RuleChar) o).expected == this.expected){
                return true;
            }
        }
        return false;
    }

    @Override
    public String javaAction() {
        return "currentChar = readNextChar();\n";
    }

    @Override
    public String javaMatch(String action) {
        StringBuilder result = new StringBuilder();
        result.append("if (currentChar=='");
        result.append(expected);
        result.append("'){\n");
        result.append(this.javaAction());
        result.append(action);
        result.append("}\n");
        return result.toString();
    }

}
