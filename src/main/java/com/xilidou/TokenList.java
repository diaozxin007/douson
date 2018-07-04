package com.xilidou;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

public class TokenList {

	private List<Token> list;

	private int pos;

	public TokenList() {
		this.list = new ArrayList<>();
		this.pos = 0;
	}

	public void add(Token token){
	    list.add(token);
    }

	public Token getNextToken(){
	    return list.get(pos++);
    }

    public boolean hasMore(){
	    return pos + 1 < list.size();
    }

    public Token getCurrent(){
	    return list.get(pos);
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "list=" + list +
                ", pos=" + pos +
                '}';
    }
}
