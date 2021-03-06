package com.gg.proj.consumer.impl.dao;

import com.gg.proj.consumer.contract.dao.SecteurDao;
import com.gg.proj.consumer.impl.rowmapper.SecteurRM;
import com.gg.proj.model.bean.Secteur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Named;
import java.sql.Types;
import java.util.List;

@Named
public class SecteurDaoImpl extends AbstractDaoImpl implements SecteurDao {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void create(Secteur model) {
        logger.debug("Entrée dans la méthode create");
        JdbcTemplate jdbcTempplate = new JdbcTemplate(getDataSource());
        jdbcTempplate.update("INSERT INTO secteur (nom, description, coordonnee_x,coordonnee_y, site_id)  VALUES(?, ?, ?, ?, ?);",
                model.getNom(),
                model.getDescription(),
                model.getCoordonneeX(),
                model.getCoordonneeY(),
                model.getSiteId()
        );
    }

    @Override
    public Secteur get(int id) {
        logger.debug("Entrée dans la méthode get avec l'id " + id);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        SecteurRM secteurRM = new SecteurRM();
        return jdbcTemplate.queryForObject("SELECT * FROM secteur WHERE id = ?;", secteurRM, id);
    }

    @Override
    public List<Secteur> list() {
        logger.debug("Entrée dans la méthode list");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        SecteurRM secteurRM = new SecteurRM();
        return jdbcTemplate.query("SELECT * FROM secteur;", secteurRM);
    }

    @Override
    public void update(Secteur model) {
        logger.debug("Entrée dans la méthode update");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTemplate.update("UPDATE secteur SET (nom,description,coordonnee_x,coordonnee_y,site_id) = (?,?,?,?,?) WHERE id = ?;",
                model.getNom(),
                model.getDescription(),
                model.getCoordonneeX(),
                model.getCoordonneeY(),
                model.getSiteId(),
                model.getId());
    }

    @Override
    public void delete(Integer id) {
        logger.debug("Entrée dans la méthode create");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTemplate.update("DELETE FROM secteur WHERE id = ?;", id);
    }

    /**
     * Cette méthode renvoit la liste des secteurs associé au site d'id siteId
     *
     * @param siteId
     * @return la liste des secteurs
     */
    @Override
    public List<Secteur> getBySiteId(Integer siteId) {
        logger.debug("Entrée dans la méthode getBySiteId avec le siteId : " + siteId);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        SecteurRM secteurRM = new SecteurRM();
        return jdbcTemplate.query("SELECT * FROM secteur WHERE site_id = ?;", secteurRM, siteId);
    }

    /**
     * Fonction qui va accéder à la BDD avec une requête LIKE
     *
     * @param termeDeLaRecherche
     * @return les secteurs qui correspondent
     */
    @Override
    public List<Secteur> search(String termeDeLaRecherche) {
        logger.debug("Entrée dans la méthode search avec le terme de recherche :" + termeDeLaRecherche);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
        SecteurRM secteurRM = new SecteurRM();
        // Préparation des paramètres
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("terme", "%" + termeDeLaRecherche + "%", Types.VARCHAR);

        String SQL = "SELECT * FROM secteur s WHERE upper(s.nom) LIKE upper(:terme) OR upper(s.description) LIKE upper(:terme);";
        return jdbcTemplate.query(SQL, params, secteurRM);
    }

    /**
     * @param voieId
     * @return
     */
    @Override
    public Secteur getSecteurByVoieId(Integer voieId) {
        logger.debug("Entrée dans la méthode getSecteurByVoieId avec le voieId : " + voieId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
        SecteurRM secteurRM = new SecteurRM();
        // Péparation des paramètres
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("voieId", voieId, Types.INTEGER);

        String SQL = "SELECT * FROM secteur JOIN  voie ON secteur.id = voie.secteur_id WHERE voie.id = :voieId;";
        return jdbcTemplate.queryForObject(SQL, params, secteurRM);
    }

    @Override
    public List<Secteur> listSecteurByDifficulty(List<String> listDifficultes, List<Integer> listSecteurId) {
        logger.debug("Entrée dans la méthode listSecteurByDifficulty");
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
        // préparation des params
        SecteurRM seRM = new SecteurRM();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("listDifficultes", listDifficultes);
        params.addValue("listSecteurId", listSecteurId);

        String rSQL = "SELECT DISTINCT se.* FROM secteur se" +
                " INNER JOIN voie v ON se.id = v.secteur_id" +
                " WHERE v.cotation IN (:listDifficultes) " +
                " AND se.id IN (:listSecteurId);";
        return jdbcTemplate.query(rSQL, params, seRM);
    }

    /**
     * Cette méthode revoit l'id d'un secteur en cherchant par nom et siteId
     *
     * @param nom le nom du secteur recherché
     * @param siteId le siteId associé au secteur
     * @return l'Id du secteur recherché
     */
    @Override
    public Integer getId(String nom, Integer siteId) {
        logger.debug("Entrée dans la méthode getId avec nom : " + nom + " et siteId : " + siteId);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
        SecteurRM secteurRM = new SecteurRM();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("nom", nom);
        params.addValue("siteId", siteId);

        String rSQL = "SELECT id FROM secteur WHERE (nom, site_id) = (:nom, :siteId);";
        return jdbcTemplate.queryForObject(rSQL, params, (rs, rowNum) -> rs.getInt("id"));
    }
}
