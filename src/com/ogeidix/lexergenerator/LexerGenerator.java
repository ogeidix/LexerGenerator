package com.ogeidix.lexergenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class LexerGenerator {
    private List<Token> tokens = new ArrayList<Token>();
    
    public boolean addToken(String rule) throws Exception{
        return tokens.add(new Token(rule));
    }

    public void generateLexer(HashMap<String,String> config) throws Exception{
        config.put("TOKENS_CONSTANTS", this.tokensConstants());
        config.put("TOKENS_IMAGES",    this.tokensImages());
        config.put("LEXER_LOGIC",      this.lexerLogic());
        String[] files   = {"Lexer.java", "LexerException.java"};
        String inputDir  = config.get("INPUT_DIR");
        String outputDir = config.get("OUTPUT_DIR");
        System.out.println("Input dir:\t" + inputDir);
        System.out.println("Output dir:\t" + outputDir);
        for(String file : files){
            System.out.print("Generating: " + file);
            String input  = readFile(inputDir + file);
            String output = replaceParams(input, config);
            file = file.replace("Lexer", config.get("LEXER_NAME"));
            System.out.print("\t>\t" + file);
            FileWriter out = new FileWriter(outputDir+file);
            out.write(output);
            out.close();
            System.out.print(" [done]\n");
        }
    }

    public String printParsedGrammar() {
        StringBuilder result = new StringBuilder();
        for(Token token : tokens){
            result.append(token.toString()).append("\n");
        }
        return result.toString();
    }

    private String tokensConstants() {
        StringBuilder   result = new StringBuilder();
        HashSet<String> uniqueTokens = uniqueTokens();
        int i=1;
        for(String token : uniqueTokens){
            result.append(", TOKEN_").append(token).append("=").append(i).append(" ");
            i++;
        }
        return result.toString();
    }

    private String tokensImages() {
        StringBuilder   result = new StringBuilder();
        HashSet<String> uniqueTokens = uniqueTokens();
        for(String token : uniqueTokens){
            result.append(", \"<").append(token).append(">\" ");
        }
        return result.toString();
    }
    
    private HashSet<String> uniqueTokens() {
        HashSet<String> uniqueTokens = new HashSet<String>();
        for(Token token : tokens){
            uniqueTokens.add(token.getName());
        }
        return uniqueTokens;
    }

    private String lexerLogic() throws Exception {
        LexerNode main = new LexerNode();
        for(Token token : tokens){
            main.merge(token.getNode());
        }
        return main.toJava();
    }

    private static String readFile(String fileName) throws FileNotFoundException, IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    private static String replaceParams(String input, HashMap<String, String> config) {
        for(Entry<String, String> param : config.entrySet()){
            String key   = "\\[" + param.getKey() + "\\]";
            String value = param.getValue();
            input = input.replaceAll(key, value);
        }
        return input;
    }
 
    
    public static void main(String args[]) throws Exception{        
        
        if (args.length == 0 || args[0] == "--help" || args[0] == "-h"){
            System.out.println("LexerGenerator\nusage: java LexerGenerator <configuration file>");
            return;
        }
        
        LexerGenerator lexer = new LexerGenerator();
        HashMap<String, String> config = new HashMap<String, String>(); 

        System.out.println("Config file:\t"+args[0]);
        String input = readFile(args[0]);
        boolean tokens = false;
        for(String line : input.split("\r?\n")){
            if (line.length() == 0 || line.charAt(0)=='#') continue;
            if(tokens == false && !line.equals("TOKENS:")){
                config.put(line.split("\\s*:\\s*")[0], line.split("\\s*:\\s*")[1]);
            } else if(line.equals("TOKENS:")) {
                tokens = true;
            } else {
                lexer.addToken(line);
            }
        }
        String parsedGrammar = lexer.printParsedGrammar();
        lexer.generateLexer(config);
        System.out.println("Generated grammar:");
        System.out.println(parsedGrammar);
    }
    
}
