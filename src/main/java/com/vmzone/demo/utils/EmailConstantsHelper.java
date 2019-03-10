package com.vmzone.demo.utils;

/**
 * Helper class for body of sent emails
 * 
 * @author Sabiha Djurina and Stefan Rangelov
 *
 */
public class EmailConstantsHelper {
	
	
	final static String SUBJECT_FORGOTTEN_PASSWORD = "Vmzona - Забравена парола";
	final static String SUBJECT_PROMOTIONS = "Vmzona - Промоция на седмицата!";
	final static String SUBJECT_CONTACT_US = "Запитване от потребител";
	final static String SUBJECT_WELCOME = "Добре дошли във vmzona!";
	
	final static String PRODUCT_URL = "http://localhost:8080/sales";
	

	public static String forgottenPassword(String newPass){
		
		StringBuffer message = new StringBuffer();
		message.append("Здравейте,");
		message.append(System.lineSeparator());
		message.append("по Ваше искане Ви изпращаме нова парола, която да замести старата Ви за сайта на Vmzona.");
		message.append(System.lineSeparator());
		message.append("Новата Ви парола е: ");
		message.append(newPass);
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
		message.append("Вие сте се абонирали да получавате бюлетин от Vmzona./n Изпращаме ви най-новите промоции в нашия сайт на следния адрес:");
		message.append(System.lineSeparator());
		message.append(PRODUCT_URL);
		message.append(System.lineSeparator());
		message.append("Пожелаваме ви лек ден,");
		message.append(System.lineSeparator());
		message.append("екипът на Vmzona!");
		return message.toString();
	}
	
	public static String registration(){
		StringBuffer message = new StringBuffer();
		message.append("Здравейте,");
		message.append(System.lineSeparator());
		message.append("Вашата регистрация във Vmzona е успешна!");
		message.append(System.lineSeparator());
		message.append("Пожелаваме Ви приятно пазаруване!");
		message.append(System.lineSeparator());
		message.append(System.lineSeparator());
		message.append("Приятен ден,");
		message.append(System.lineSeparator());
		message.append("екипът на Vmzona!");
		return message.toString();
	}
	
	
	
	

}
