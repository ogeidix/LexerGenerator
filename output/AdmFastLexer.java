package com.ogeidix.lexer; 

import java.io.IOException;
import com.ogeidix.lexer.AdmFastLexerException;

public class AdmFastLexer {

    public static final int
        TOKEN_EOF = 0 , TOKEN_BOMB_LIT=1 , TOKEN_FALSE_LIT=2 , TOKEN_BOOLEAN_LIT=3 , TOKEN_BONSAI_LIT=4 , TOKEN_HELLO_LIT=5 ;

    private static final String[] tokenImage = {
            "<EOF>" , "<BOMB_LIT>" , "<FALSE_LIT>" , "<BOOLEAN_LIT>" , "<BONSAI_LIT>" , "<HELLO_LIT>" 
          };
    
    private static final char EOF_CHAR = 4;
    protected java.io.Reader inputStream;
    protected int column;
    protected int line;
    protected boolean prevCharIsCR;
    protected boolean prevCharIsLF;
    protected char[] buffer;
    protected int bufsize;
    protected int bufpos;
    protected int tokenBegin;
    protected int endOf_USED_Buffer;
    protected int endOf_UNUSED_Buffer;
    protected int maxUnusedBufferSize;
    
    public int next() throws AdmFastLexerException, IOException{
        char currentChar = buffer[bufpos];
        while (currentChar == ' ' || currentChar=='\t' || currentChar == '\n' || currentChar=='\r')
            currentChar = readNextChar(); 
        tokenBegin = bufpos;
        if (currentChar==EOF_CHAR) return TOKEN_EOF;

        switch(currentChar){case 'b':
currentChar = readNextChar();
if (currentChar=='o'){
currentChar = readNextChar();

switch(currentChar){case 'o':
currentChar = readNextChar();
if (currentChar=='l'){
currentChar = readNextChar();

if (currentChar=='e'){
currentChar = readNextChar();

if (currentChar=='a'){
currentChar = readNextChar();

if (currentChar=='n'){
currentChar = readNextChar();

return TOKEN_BOOLEAN_LIT;
}
return parseError(TOKEN_BOOLEAN_LIT);
}
return parseError(TOKEN_BOOLEAN_LIT);
}
return parseError(TOKEN_BOOLEAN_LIT);
}
return parseError(TOKEN_BOOLEAN_LIT);
case 'm':
currentChar = readNextChar();
if (currentChar=='b'){
currentChar = readNextChar();

return TOKEN_BOMB_LIT;
}
return parseError(TOKEN_BOMB_LIT);
case 'n':
currentChar = readNextChar();
if (currentChar=='s'){
currentChar = readNextChar();

if (currentChar=='a'){
currentChar = readNextChar();

if (currentChar=='i'){
currentChar = readNextChar();

return TOKEN_BONSAI_LIT;
}
return parseError(TOKEN_BONSAI_LIT);
}
return parseError(TOKEN_BONSAI_LIT);
}
return parseError(TOKEN_BONSAI_LIT);
}return parseError(TOKEN_BONSAI_LIT,TOKEN_BOOLEAN_LIT,TOKEN_BOMB_LIT);
}
return parseError(TOKEN_BONSAI_LIT,TOKEN_BOOLEAN_LIT,TOKEN_BOMB_LIT);
case 'f':
currentChar = readNextChar();
if (currentChar=='a'){
currentChar = readNextChar();

if (currentChar=='l'){
currentChar = readNextChar();

if (currentChar=='s'){
currentChar = readNextChar();

if (currentChar=='e'){
currentChar = readNextChar();

return TOKEN_FALSE_LIT;
}
return parseError(TOKEN_FALSE_LIT);
}
return parseError(TOKEN_FALSE_LIT);
}
return parseError(TOKEN_FALSE_LIT);
}
return parseError(TOKEN_FALSE_LIT);
case 'h':
currentChar = readNextChar();
if (currentChar=='e'){
currentChar = readNextChar();

if (currentChar=='l'){
currentChar = readNextChar();

if (currentChar=='l'){
currentChar = readNextChar();

if (currentChar=='o'){
currentChar = readNextChar();

return TOKEN_HELLO_LIT;
}
return parseError(TOKEN_HELLO_LIT);
}
return parseError(TOKEN_HELLO_LIT);
}
return parseError(TOKEN_HELLO_LIT);
}
return parseError(TOKEN_HELLO_LIT);
}return parseError(TOKEN_BONSAI_LIT,TOKEN_BOOLEAN_LIT,TOKEN_BOMB_LIT,TOKEN_HELLO_LIT,TOKEN_FALSE_LIT);

    }

// ================================================================================
//  Public interface
// ================================================================================
    
    public AdmFastLexer(java.io.Reader stream) throws IOException{
        reInit(stream);
    }

    public void reInit(java.io.Reader stream) throws IOException{
        done();
        inputStream    = stream;
        bufsize        = 4096;
        line           = 1;
        column         = 0;
        bufpos         = -1;
        endOf_UNUSED_Buffer = bufsize;
        endOf_USED_Buffer = 0;
        prevCharIsCR   = false;
        prevCharIsLF   = false;
        buffer         = new char[bufsize];
        tokenBegin     = -1;
        maxUnusedBufferSize = 4096/2;
        readNextChar();
    }

    public String getLastTokenImage() {
        if (bufpos >= tokenBegin)
            return new String(buffer, tokenBegin, bufpos - tokenBegin);
          else
            return new String(buffer, tokenBegin, bufsize - tokenBegin) +
                                  new String(buffer, 0, bufpos);
    }
    
    public static String tokenKindToString(int token) {
        return tokenImage[token]; 
    }

    public void done(){
        buffer = null;
    }

// ================================================================================
//  Parse error management
// ================================================================================    
    
    protected int parseError(String reason) throws AdmFastLexerException {
        StringBuilder message = new StringBuilder();
        message.append(reason).append("\n");
        message.append("Line: ").append(line).append("\n");
        message.append("Row: ").append(column).append("\n");
        throw new AdmFastLexerException(message.toString());
    }

    protected int parseError(int ... tokens) throws AdmFastLexerException {
        StringBuilder message = new StringBuilder();
        message.append("Error while parsing. ");
        message.append(" Line: ").append(line);
        message.append(" Row: ").append(column);
        message.append(" Expecting:");
        for (int tokenId : tokens){
            message.append(" ").append(AdmFastLexer.tokenKindToString(tokenId));
        }
        throw new AdmFastLexerException(message.toString());
    }
    
    protected void updateLineColumn(char c){
        column++;
    
        if (prevCharIsLF)
        {
            prevCharIsLF = false;
            line += (column = 1);
        }
        else if (prevCharIsCR)
        {
            prevCharIsCR = false;
            if (c == '\n')
            {
                prevCharIsLF = true;
            }
            else
            {
                line += (column = 1);
            }
        }
        
        if (c=='\r') {
            prevCharIsCR = true;
        } else if(c == '\n') {
            prevCharIsLF = true;
        }
    }
    
// ================================================================================
//  Read data buffer management
// ================================================================================    

    protected char readNextChar() throws IOException {
        if (++bufpos >= endOf_USED_Buffer)
            fillBuff();
        char c = buffer[bufpos];
        updateLineColumn(c);
        return c;
    }

    protected boolean fillBuff() throws IOException {
        if (endOf_UNUSED_Buffer == endOf_USED_Buffer) // If no more unused buffer space 
        {
          if (endOf_UNUSED_Buffer == bufsize)         // -- If the previous unused space was
          {                                           // -- at the end of the buffer
            if (tokenBegin > maxUnusedBufferSize)     // -- -- If the first N bytes before
            {                                         //       the current token are enough
              bufpos = endOf_USED_Buffer = 0;         // -- -- -- setup buffer to use that fragment 
              endOf_UNUSED_Buffer = tokenBegin;
            }
            else if (tokenBegin < 0)                  // -- -- If no token yet
              bufpos = endOf_USED_Buffer = 0;         // -- -- -- reuse the whole buffer
            else
              ExpandBuff(false);                      // -- -- Otherwise expand buffer after its end
          }
          else if (endOf_UNUSED_Buffer > tokenBegin)  // If the endOf_UNUSED_Buffer is after the token
            endOf_UNUSED_Buffer = bufsize;            // -- set endOf_UNUSED_Buffer to the end of the buffer
          else if ((tokenBegin - endOf_UNUSED_Buffer) < maxUnusedBufferSize)
          {                                           // If between endOf_UNUSED_Buffer and the token
            ExpandBuff(true);                         // there is NOT enough space expand the buffer                          
          }                                           // reorganizing it
          else 
            endOf_UNUSED_Buffer = tokenBegin;         // Otherwise there is enough space at the start
        }                                             // so we set the buffer to use that fragment
        int i;
        if ((i = inputStream.read(buffer, endOf_USED_Buffer, endOf_UNUSED_Buffer - endOf_USED_Buffer)) == -1)
        {
            inputStream.close();
            buffer[endOf_USED_Buffer]=(char)EOF_CHAR;
            endOf_USED_Buffer++;
            return false;
        }
            else
                endOf_USED_Buffer += i;
        return true;
    }


    protected void ExpandBuff(boolean wrapAround)
    {
      char[] newbuffer = new char[bufsize + maxUnusedBufferSize];

      try {
        if (wrapAround) {
          System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
          System.arraycopy(buffer, 0, newbuffer, bufsize - tokenBegin, bufpos);
          buffer = newbuffer;
          endOf_USED_Buffer = (bufpos += (bufsize - tokenBegin));
        }
        else {
          System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
          buffer = newbuffer;
          endOf_USED_Buffer = (bufpos -= tokenBegin);
        }
      } catch (Throwable t) {
          throw new Error(t.getMessage());
      }

      bufsize += maxUnusedBufferSize;
      endOf_UNUSED_Buffer = bufsize;
      tokenBegin = 0;
    }    
}
