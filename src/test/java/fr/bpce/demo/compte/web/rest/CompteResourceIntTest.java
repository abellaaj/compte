package fr.bpce.demo.compte.web.rest;

import fr.bpce.demo.compte.CompteApp;

import fr.bpce.demo.compte.domain.Compte;
import fr.bpce.demo.compte.repository.CompteRepository;
import fr.bpce.demo.compte.repository.search.CompteSearchRepository;
import fr.bpce.demo.compte.service.CompteService;
import fr.bpce.demo.compte.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;


import static fr.bpce.demo.compte.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CompteResource REST controller.
 *
 * @see CompteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompteApp.class)
public class CompteResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_BANQUE = "AAAAAAAAAA";
    private static final String UPDATED_BANQUE = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_DEVISE = "AAAAAAAAAA";
    private static final String UPDATED_DEVISE = "BBBBBBBBBB";

    @Autowired
    private CompteRepository compteRepository;

    

    @Autowired
    private CompteService compteService;

    /**
     * This repository is mocked in the fr.bpce.demo.compte.repository.search test package.
     *
     * @see fr.bpce.demo.compte.repository.search.CompteSearchRepositoryMockConfiguration
     */
    @Autowired
    private CompteSearchRepository mockCompteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restCompteMockMvc;

    private Compte compte;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompteResource compteResource = new CompteResource(compteService);
        this.restCompteMockMvc = MockMvcBuilders.standaloneSetup(compteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createEntity() {
        Compte compte = new Compte()
            .libelle(DEFAULT_LIBELLE)
            .banque(DEFAULT_BANQUE)
            .iban(DEFAULT_IBAN)
            .devise(DEFAULT_DEVISE);
        return compte;
    }

    @Before
    public void initTest() {
        compteRepository.deleteAll();
        compte = createEntity();
    }

    @Test
    public void createCompte() throws Exception {
        int databaseSizeBeforeCreate = compteRepository.findAll().size();

        // Create the Compte
        restCompteMockMvc.perform(post("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compte)))
            .andExpect(status().isCreated());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeCreate + 1);
        Compte testCompte = compteList.get(compteList.size() - 1);
        assertThat(testCompte.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCompte.getBanque()).isEqualTo(DEFAULT_BANQUE);
        assertThat(testCompte.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testCompte.getDevise()).isEqualTo(DEFAULT_DEVISE);

        // Validate the Compte in Elasticsearch
        verify(mockCompteSearchRepository, times(1)).save(testCompte);
    }

    @Test
    public void createCompteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = compteRepository.findAll().size();

        // Create the Compte with an existing ID
        compte.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteMockMvc.perform(post("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compte)))
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Compte in Elasticsearch
        verify(mockCompteSearchRepository, times(0)).save(compte);
    }

    @Test
    public void getAllComptes() throws Exception {
        // Initialize the database
        compteRepository.save(compte);

        // Get all the compteList
        restCompteMockMvc.perform(get("/api/comptes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].banque").value(hasItem(DEFAULT_BANQUE.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN.toString())))
            .andExpect(jsonPath("$.[*].devise").value(hasItem(DEFAULT_DEVISE.toString())));
    }
    

    @Test
    public void getCompte() throws Exception {
        // Initialize the database
        compteRepository.save(compte);

        // Get the compte
        restCompteMockMvc.perform(get("/api/comptes/{id}", compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(compte.getId()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.banque").value(DEFAULT_BANQUE.toString()))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN.toString()))
            .andExpect(jsonPath("$.devise").value(DEFAULT_DEVISE.toString()));
    }
    @Test
    public void getNonExistingCompte() throws Exception {
        // Get the compte
        restCompteMockMvc.perform(get("/api/comptes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCompte() throws Exception {
        // Initialize the database
        compteService.save(compte);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCompteSearchRepository);

        int databaseSizeBeforeUpdate = compteRepository.findAll().size();

        // Update the compte
        Compte updatedCompte = compteRepository.findById(compte.getId()).get();
        updatedCompte
            .libelle(UPDATED_LIBELLE)
            .banque(UPDATED_BANQUE)
            .iban(UPDATED_IBAN)
            .devise(UPDATED_DEVISE);

        restCompteMockMvc.perform(put("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompte)))
            .andExpect(status().isOk());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
        Compte testCompte = compteList.get(compteList.size() - 1);
        assertThat(testCompte.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCompte.getBanque()).isEqualTo(UPDATED_BANQUE);
        assertThat(testCompte.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testCompte.getDevise()).isEqualTo(UPDATED_DEVISE);

        // Validate the Compte in Elasticsearch
        verify(mockCompteSearchRepository, times(1)).save(testCompte);
    }

    @Test
    public void updateNonExistingCompte() throws Exception {
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();

        // Create the Compte

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restCompteMockMvc.perform(put("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compte)))
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Compte in Elasticsearch
        verify(mockCompteSearchRepository, times(0)).save(compte);
    }

    @Test
    public void deleteCompte() throws Exception {
        // Initialize the database
        compteService.save(compte);

        int databaseSizeBeforeDelete = compteRepository.findAll().size();

        // Get the compte
        restCompteMockMvc.perform(delete("/api/comptes/{id}", compte.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Compte in Elasticsearch
        verify(mockCompteSearchRepository, times(1)).deleteById(compte.getId());
    }

    @Test
    public void searchCompte() throws Exception {
        // Initialize the database
        compteService.save(compte);
        when(mockCompteSearchRepository.search(queryStringQuery("id:" + compte.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(compte), PageRequest.of(0, 1), 1));
        // Search the compte
        restCompteMockMvc.perform(get("/api/_search/comptes?query=id:" + compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].banque").value(hasItem(DEFAULT_BANQUE.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN.toString())))
            .andExpect(jsonPath("$.[*].devise").value(hasItem(DEFAULT_DEVISE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compte.class);
        Compte compte1 = new Compte();
        compte1.setId("id1");
        Compte compte2 = new Compte();
        compte2.setId(compte1.getId());
        assertThat(compte1).isEqualTo(compte2);
        compte2.setId("id2");
        assertThat(compte1).isNotEqualTo(compte2);
        compte1.setId(null);
        assertThat(compte1).isNotEqualTo(compte2);
    }
}
