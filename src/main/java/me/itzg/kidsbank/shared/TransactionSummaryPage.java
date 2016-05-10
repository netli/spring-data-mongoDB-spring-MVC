package me.itzg.kidsbank.shared;

import java.util.List;

public class TransactionSummaryPage {
	private List<TransactionSummary> content;
	
	private int currentPage;
	
	private int totalPages;

	public List<TransactionSummary> getContent() {
		return content;
	}

	public void setContent(List<TransactionSummary> content) {
		this.content = content;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
