package me.itzg.kidsbank.shared;

import static org.junit.Assert.*;

import java.util.Locale;

import me.itzg.kidsbank.shared.LocaleSpec;

import org.junit.Test;

public class LocaleSpecTest {

	@Test
	public void testEqualsYes() {
		LocaleSpec lhs = new LocaleSpec();
		lhs.setCountry("c1");
		lhs.setLanguage("l1");
		LocaleSpec rhs = new LocaleSpec();
		rhs.setCountry("c1");
		rhs.setLanguage("l1");
		
		assertTrue(lhs.equals(rhs));
	}
	
	@Test
	public void testEqualsNo() {
		LocaleSpec lhs = new LocaleSpec();
		lhs.setCountry("c1");
		lhs.setLanguage("l1");
		LocaleSpec rhs = new LocaleSpec();
		rhs.setCountry("c2");
		rhs.setLanguage("l1");
		
		assertFalse(lhs.equals(rhs));
	}
	
	@Test
	public void testCreatedFromLocale() {
		LocaleSpec result = new LocaleSpec(new Locale("en", "US"));
		assertEquals("en", result.getLanguage());
		assertEquals("US", result.getCountry());
	}

}
