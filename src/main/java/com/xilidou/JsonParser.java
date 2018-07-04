package com.xilidou;

import com.xilidou.exception.JsonParseException;

import java.util.Stack;

public class JsonParser {

    private static final int BEGIN_OBJECT_TOKEN = 1;
    private static final int END_OBJECT_TOKEN = 2;
    private static final int BEGIN_ARRAY_TOKEN = 4;
    private static final int END_ARRAY_TOKEN = 8;
    private static final int NULL_TOKEN = 16;
    private static final int NUMBER_TOKEN = 32;
    private static final int STRING_TOKEN = 64;
    private static final int BOOLEAN_TOKEN = 128;
    private static final int SEP_COLON_TOKEN = 256;
    private static final int SEP_COMMA_TOKEN = 512;

    private TokenList tokenList;

    public JsonParser(TokenList tokenList) {
        this.tokenList = tokenList;
    }

    /**
     * object = {} | { members }
     * members = pair | pair , members
     * pair = string : value
     * array = [] | [ elements ]
     * elements = value  | value , elements
     * value = string | number | object | array | true | false | null
     * @return
     */

    public Object parser(){



        while (tokenList.hasMore()){

            Token token = tokenList.getNextToken();
            if(token.getTokenType() == TokenType.BEGIN_OBJECT){
                return parserJsonObject();
            }

            if(token.getTokenType() == TokenType.BEGIN_ARRAY){
                return parserJosnArray();
            }


        }
        return null;
    }

    private JsonObject parserJsonObject(){

        int expectToken = STRING_TOKEN | END_OBJECT_TOKEN;

        Object val = null;
        String key = "";
        while (tokenList.hasMore()){

            Token token = tokenList.getNextToken();


        }

        return null;

    }

    private JsonArray parserJosnArray(){
        return null;
    }

    private void checkExpectToken(TokenType tokenType, int expectToken) {
        if ((tokenType.getTokenCode() & expectToken) == 0) {
            throw new JsonParseException("Parse error, invalid Token.");
        }
    }
}
