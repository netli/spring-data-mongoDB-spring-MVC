package me.itzg.kidsbank;

import java.text.NumberFormat;

import me.itzg.kidsbank.shared.LocaleSpec;

public abstract class CurrencyUtil {

	public static String format(LocaleSpec locale, double amount) {
		return NumberFormat.getCurrencyInstance(locale.toLocale()).format(amount);
	}
	
	public static String getCurrencySymbol(LocaleSpec locale) {
		return NumberFormat.getCurrencyInstance(locale.toLocale()).getCurrency().getSymbol();
	}
	
	public static class ValidatedCurrencyAmount {
		public double amount;
		public int position;
		public ValidatedCurrencyAmount(double amount, int position) {
			this.amount = amount;
			this.position = position;
		}
	}
}
