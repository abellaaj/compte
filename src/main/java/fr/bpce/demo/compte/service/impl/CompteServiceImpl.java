package fr.bpce.demo.compte.service.impl;

import fr.bpce.demo.compte.service.CompteService;
import fr.bpce.demo.compte.domain.Compte;
import fr.bpce.demo.compte.repository.CompteRepository;
import fr.bpce.demo.compte.repository.search.CompteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Compte.
 */
@Service
public class CompteServiceImpl implements CompteService {

    private final Logger log = LoggerFactory.getLogger(CompteServiceImpl.class);

    private final CompteRepository compteRepository;

    private final CompteSearchRepository compteSearchRepository;

    public CompteServiceImpl(CompteRepository compteRepository, CompteSearchRepository compteSearchRepository) {
        this.compteRepository = compteRepository;
        this.compteSearchRepository = compteSearchRepository;
    }

    /**
     * Save a compte.
     *
     * @param compte the entity to save
     * @return the persisted entity
     */
    @Override
    public Compte save(Compte compte) {
        log.debug("Request to save Compte : {}", compte);        Compte result = compteRepository.save(compte);
        compteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the comptes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Compte> findAll(Pageable pageable) {
        log.debug("Request to get all Comptes");
        return compteRepository.findAll(pageable);
    }


    /**
     * Get one compte by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Compte> findOne(String id) {
        log.debug("Request to get Compte : {}", id);
        return compteRepository.findById(id);
    }

    /**
     * Delete the compte by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Compte : {}", id);
        compteRepository.deleteById(id);
        compteSearchRepository.deleteById(id);
    }

    /**
     * Search for the compte corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Compte> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Comptes for query {}", query);
        return compteSearchRepository.search(queryStringQuery(query), pageable);    }
}
