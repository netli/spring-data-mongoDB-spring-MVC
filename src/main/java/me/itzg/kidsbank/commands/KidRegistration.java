package me.itzg.kidsbank.commands;

public class KidRegistration {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasValidName() {
		return name != null && !name.isEmpty();
	}
}
