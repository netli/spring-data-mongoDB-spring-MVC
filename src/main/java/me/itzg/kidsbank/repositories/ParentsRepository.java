package me.itzg.kidsbank.repositories;

import me.itzg.kidsbank.domain.Parent;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ParentsRepository extends MongoRepository<Parent, String> {
	Parent findByOpenId(String openId);

	@Query(value="{_id : ?0 }", fields="{accounts:1}")
	Parent findJustAccounts(String openId);
}
