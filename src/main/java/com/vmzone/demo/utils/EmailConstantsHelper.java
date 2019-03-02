package com.vmzone.demo.utils;

public class EmailConstantsHelper {
	
	private static final int LENGTH_FOR_FORGOTTEN_PASSWORD = 8;
	final static String SUBJECT_FORGOTTEN_PASSWORD = "Vmzona - Забравена парола";
	final static String SUBJECT_PROMOTIONS = "Vmzona - Промоция на седмицата!";
	final static String SUBJECT_CONTACT_US = "Запитване от потребител";
	

	public static String forgottenPassword(){
		StringBuffer message = new StringBuffer();
		message.append("Здравейте,");
		message.append(System.lineSeparator());
		message.append("по Ваше искане Ви изпращаме нова парола, която да замести старата Ви за сайта на Vmzona.");
		message.append(System.lineSeparator());
		message.append("Новата Ви парола е: ");
		message.append(PasswordGenerator.makePassword(LENGTH_FOR_FORGOTTEN_PASSWORD));
		message.append(System.lineSeparator());
		message.append("Препоръваме Ви да я смените след като успешно влезете в профила си!");
		message.append(System.lineSeparator());
		message.append(System.lineSeparator());
		message.append("Пожелаваме ви лек ден,");
		message.append(System.lineSeparator());
		message.append("екипът на Vmzona!");
		return message.toString();
	}
	
	public static String subscribedPromotions(){
		StringBuffer message = new StringBuffer();
		message.append("Здравейте,");
		message.append(System.lineSeparator());
		message.append("Вие сте се абонирали да получавате бюлетин от Vmzona./n Изпращаме ви най-новите промоции в нашия сайт.");
		message.append(System.lineSeparator());
		//TODO get all products with promotions
		message.append(System.lineSeparator());
		message.append("Пожелаваме ви лек ден,");
		message.append(System.lineSeparator());
		message.append("екипът на Vmzona!");
		return message.toString();
	}
	
	
	
	

}
