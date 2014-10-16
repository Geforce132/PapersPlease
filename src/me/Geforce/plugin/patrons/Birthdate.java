package me.Geforce.plugin.patrons;

import java.util.GregorianCalendar;

public class Birthdate {
	
	private int day, month, year;
	
	@SuppressWarnings("static-access")
	public Birthdate(){
		GregorianCalendar gc = new GregorianCalendar();
		
		int year = randBetween(1900, 2014);
		gc.set(gc.YEAR, year);
		
		int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
		gc.set(gc.DAY_OF_YEAR, dayOfYear);
		
		this.day = gc.get(gc.DAY_OF_MONTH);
		this.month = gc.get(gc.MONTH);
		this.year = gc.get(gc.YEAR);
		
	}
	
	private int randBetween(int start, int end){
		return start + (int)Math.round(Math.random() * (end - start));
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

}
