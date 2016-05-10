package me.itzg.kidsbank.repositories;

import me.itzg.kidsbank.domain.KidLink;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface KidLinkRepository extends MongoRepository<KidLink, Integer>{

}
