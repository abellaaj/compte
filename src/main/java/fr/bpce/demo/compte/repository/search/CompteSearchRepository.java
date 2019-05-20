package fr.bpce.demo.compte.repository.search;

import fr.bpce.demo.compte.domain.Compte;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Compte entity.
 */
public interface CompteSearchRepository extends ElasticsearchRepository<Compte, String> {
}
