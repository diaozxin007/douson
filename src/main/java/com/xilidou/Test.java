package com.xilidou;

import java.io.*;

public class Test {

    public static void main(String[] args) throws IOException {

        String json = "{\"a\": 1, \"bbb\": \"b\", \"c\": {\"a\": 1,    \"b\": null, \"d\": [0.1, \"a\", 1,2, 123, 1.23e+10, true, false, null]}";
        byte[] bytes = json.getBytes();
        InputStream in = new ByteArrayInputStream(bytes);
        InputStreamReader reader = new InputStreamReader(in);

        CharReader charReader = new CharReader(reader);
        TokenParser tokenParser = new TokenParser(charReader);

        TokenList parser = tokenParser.parser();

        System.out.println(parser);
    }

}
