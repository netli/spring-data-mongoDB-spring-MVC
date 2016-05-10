package me.itzg.kidsbank.repositories;

import me.itzg.kidsbank.domain.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransactionsRepository extends PagingAndSortingRepository<Transaction, String> {

	Page<Transaction> findByAccountId(String accountId, Pageable pageable);
}
