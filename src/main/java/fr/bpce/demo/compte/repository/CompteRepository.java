package fr.bpce.demo.compte.repository;

import fr.bpce.demo.compte.domain.Compte;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Compte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteRepository extends MongoRepository<Compte, String> {

}
