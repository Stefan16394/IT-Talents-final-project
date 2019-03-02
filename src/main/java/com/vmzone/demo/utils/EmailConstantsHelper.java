package com.vmzone.demo.utils;

public class EmailConstantsHelper {
	
	private static final int LENGTH_FOR_FORGOTTEN_PASSWORD = 8;
	final static String SUBJECT_FORGOTTEN_PASSWORD = "Vmzona - Забравена парола";
	final static String SUBJECT_PROMOTIONS = "Vmzona - Промоция на седмицата!";
	

	public static String forgottenPassword(){
		StringBuffer message = new StringBuffer();
		message.append("Здравейте,");
		message.append(System.lineSeparator());
		message.append("по Ваше искане Ви изпращаме нова парола, която да замести старата Ви парола за сайта на Vmzona.");
		message.append("Новата Ви парола е: ");
		message.append(PasswordGenerator.makePassword(LENGTH_FOR_FORGOTTEN_PASSWORD));
		message.append(System.lineSeparator());
		message.append("Пожелаваме ви лек ден,");
		message.append(System.lineSeparator());
		message.append("екипът на Vmzona!");
		return message.toString();
	}
	

}
