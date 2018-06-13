package com.xilidou;

import com.xilidou.exception.JsonParseException;

import java.io.IOException;
import java.util.Stack;

public class TokenParser {

	private CharReader charReader;

	private Stack<Token> tokenStack;

	public TokenParser(CharReader charReader){
		this.charReader = charReader;
		this.tokenStack = new Stack<Token>();
	}

	public Stack<Token> parser() throws IOException {

		tokenStack = new Stack<Token>();

		while (charReader.hasMore()) {

			char ch = charReader.next();

			switch (ch) {
				case '{':
					tokenStack.add(new Token(TokenType.BEGIN_OBJECT, "{"));
				case '}':
					tokenStack.add(new Token(TokenType.END_OBJECT, "}"));
				case '[':
					tokenStack.add(new Token(TokenType.BEGIN_ARRAY, "["));
				case ']':
					tokenStack.add(new Token(TokenType.END_ARRAY, "]"));
				case ',':
					tokenStack.add(new Token(TokenType.SEP_COMMA, ","));
				case ':':
					tokenStack.add(new Token(TokenType.SEP_COLON, ":"));
				case '"':
					tokenStack.add(readString(charReader));
				case 'f':
					tokenStack.add(readBool(charReader));

			}
		}

		return tokenStack;
	}


	private Token readNull(CharReader charReader) throws IOException{

		if (charReader.peek() == 'n' && charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l'){
			return new Token(TokenType.NULL,"null");
		}

		throw new JsonParseException("parse null error");
	}

	private Token readString(CharReader charReader) throws IOException{

		StringBuilder sb = new StringBuilder();

		for (;;) {
			char ch = charReader.next();
			if (ch == '\\') {
				if (!isEscape()) {
					throw new JsonParseException("Invalid escape character");
				}
				sb.append('\\');
				ch = charReader.peek();
				sb.append(ch);
				if (ch == 'u') {
					for (int i = 0; i < 4; i++) {
						ch = charReader.next();
						if (isHex(ch)) {
							sb.append(ch);
						} else {
							throw new JsonParseException("Invalid character");
						}
					}
				}
			} else if (ch == '"') {
				return new Token(TokenType.STRING, sb.toString());
			} else if (ch == '\r' || ch == '\n') {
				throw new JsonParseException("Invalid character");
			} else {
				sb.append(ch);
			}
		}
	}

	private boolean isEscape() throws IOException {
		char ch = charReader.next();
		return (ch == '"' || ch == '\\' || ch == 'u' || ch == 'r'
				|| ch == 'n' || ch == 'b' || ch == 't' || ch == 'f');

	}

	private boolean isHex(char ch) {
		return ((ch >= '0' && ch <= '9') || ('a' <= ch && ch <= 'f')
				|| ('A' <= ch && ch <= 'F'));
	}

	private Token readBool(CharReader charReader) throws IOException{

		if( charReader.peek() == 't'){
			if(charReader.next() == 'r' && charReader.next() == 'u' && charReader.next() == 'e'){
				return new Token(TokenType.BOOLEAN,"true");
			}
		}

		if( charReader.peek() == 'f'){
			if(charReader.next() == 'a' && charReader.next() == 'l' && charReader.next() == 'u' && charReader.next() == 'e' ){
				return new Token(TokenType.BOOLEAN,"false");
			}
		}

		throw new JsonParseException("parse boolean error");

	}

	private Token readNumber(){
		return null;
	}

	private boolean isDigit(char ch){
		return Character.isDigit(ch);
	}

}
