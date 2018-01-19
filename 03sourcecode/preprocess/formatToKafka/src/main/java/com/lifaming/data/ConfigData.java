package com.lifaming.data;

import java.util.regex.Pattern;

public class ConfigData {
	private String introduction;
	private String matchRegexp;
	private String commands;
	
	private Pattern pattern;
	
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getMatchRegexp() {
		return matchRegexp;
	}
	public void setMatchRegexp(String matchRegexp) {
		this.matchRegexp = matchRegexp;
	}
	public String getCommands() {
		return commands;
	}
	public void setCommands(String commands) {
		this.commands = commands;
	}
	
	public void initPattern(){
		pattern = Pattern.compile(matchRegexp);
	}
	
}
