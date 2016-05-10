package me.itzg.kidsbank.repositories;

import me.itzg.kidsbank.domain.Account;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountsRepository extends PagingAndSortingRepository<Account, ObjectId> {
	Account findById(String id);
}
