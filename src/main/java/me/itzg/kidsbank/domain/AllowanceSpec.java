package me.itzg.kidsbank.domain;

public class AllowanceSpec {
	private int dayOfWeek;
	
	private double amount;
	
	private String parentId;
	
	private String description;
	
	@Override
	public String toString() {
		return AllowanceSpec.class.getSimpleName()+":[dayOfWeek="+dayOfWeek
				+", amount="+amount
				+"]";
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
