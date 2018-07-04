package com.xilidou;

import com.xilidou.exception.JsonParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Zhengxin
 */
public class TokenParser {

	private CharReader charReader;

	private TokenList tokenList;

	public TokenParser(CharReader charReader){
		this.charReader = charReader;
		this.tokenList = new TokenList();
	}

	public TokenList parser() throws IOException {

		while (charReader.hasMore()) {

		    char ch = charReader.next();

			switch (ch) {
				case '{':
                    tokenList.add(new Token(TokenType.BEGIN_OBJECT, "{"));
					break;
				case '}':
                    tokenList.add(new Token(TokenType.END_OBJECT, "}"));
					break;
				case '[':
                    tokenList.add(new Token(TokenType.BEGIN_ARRAY, "["));
					break;
				case ']':
                    tokenList.add(new Token(TokenType.END_ARRAY, "]"));
					break;
				case ',':
                    tokenList.add(new Token(TokenType.SEP_COMMA, ","));
					break;
				case ':':
                    tokenList.add(new Token(TokenType.SEP_COLON, ":"));
					break;
				case '"':
                    tokenList.add(readString());
					break;
				case 'f':
                case 't':
                    tokenList.add(readBool());
                    break;
				case 'n':
                    tokenList.add(readNull());
					break;
                case '-':
                    tokenList.add(readNumber());
                    default:
						break;

			}

			if(isDigit(ch)){
                tokenList.add(readNumber());
            }
		}

		return tokenList;
	}


	private Token readNull() throws IOException{
		if (charReader.peek() == 'n' && charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l'){
			return new Token(TokenType.NULL,"null");
		}

		throw new JsonParseException("parse null error");
	}

	private Token readString() throws IOException{

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

	private Token readBool() throws IOException{

		if( charReader.peek() == 't'){
			if(charReader.next() == 'r' && charReader.next() == 'u' && charReader.next() == 'e'){
				return new Token(TokenType.BOOLEAN,"true");
			}
		}

		if( charReader.peek() == 'f'){
			if(charReader.next() == 'a' && charReader.next() == 'l' && charReader.next() == 's' && charReader.next() == 'e' ){
				return new Token(TokenType.BOOLEAN,"false");
			}
		}

		throw new JsonParseException("parse boolean error");

	}

    private Token readNumber() throws IOException {
        char ch = charReader.peek();
        StringBuilder sb = new StringBuilder();
        //处理负数
        if (ch == '-') {
            sb.append(ch);
            ch = charReader.next();
            //处理小数
            if (ch == '0') {
                sb.append(ch);
                sb.append(readFracAndExp());
            } else if (isDigitOne2Nine(ch)) {
                do {
                    sb.append(ch);
                    ch = charReader.next();
                } while (isDigit(ch));
                if (ch != (char) -1) {
                    charReader.back();
                    sb.append(readFracAndExp());
                }
            } else {
                throw new JsonParseException("Invalid minus number");
            }

            //处理小数
        } else if (ch == '0') {
            sb.append(ch);
            sb.append(readFracAndExp());
        } else {
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));
            if (ch != (char) -1) {
                charReader.back();
                sb.append(readFracAndExp());
            }
        }

        return new Token(TokenType.NUMBER, sb.toString());
    }

    private String readFracAndExp() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch = charReader.next();
        if (ch ==  '.') {
            sb.append(ch);
            ch = charReader.next();
            if (!isDigit(ch)) {
                throw new JsonParseException("Invalid frac");
            }
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));
            // 处理科学计数法
            if (isExp(ch)) {
                sb.append(ch);
                sb.append(readExp());
            } else {
                if (ch != (char) -1) {
                    charReader.back();
                }
            }
        } else if (isExp(ch)) {
            sb.append(ch);
            sb.append(readExp());
        } else {
            charReader.back();
        }

        return sb.toString();
    }

    private String readExp() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch = charReader.next();
        if (ch == '+' || ch =='-') {
            sb.append(ch);
            ch = charReader.next();
            if (isDigit(ch)) {
                do {
                    sb.append(ch);
                    ch = charReader.next();
                } while (isDigit(ch));
                // 读取结束，不用回退
                if (ch != (char) -1) {
                    charReader.back();
                }
            } else {
                throw new JsonParseException("e or E");
            }
        } else {
            throw new JsonParseException("e or E");
        }

        return sb.toString();
    }

    private boolean isExp(char ch){
        return ch == 'e' || ch == 'E';
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isDigitOne2Nine(char ch) {
        return ch >= '0' && ch <= '9';
    }


}
