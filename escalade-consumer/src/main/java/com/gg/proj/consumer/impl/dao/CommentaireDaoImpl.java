package com.gg.proj.consumer.impl.dao;

import com.gg.proj.consumer.contract.dao.CommentaireDao;
import com.gg.proj.consumer.impl.rowmapper.CommentaireRM;
import com.gg.proj.model.bean.Commentaire;
import com.gg.proj.model.bean.Utilisateur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Named;
import java.util.List;

@Named
public class CommentaireDaoImpl extends AbstractDaoImpl implements CommentaireDao {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void create(Commentaire model) {
        logger.debug("Entrée dans la méthode create");
        JdbcTemplate jdbcTempplate = new JdbcTemplate(getDataSource());
        jdbcTempplate.update("INSERT INTO commentaire (date_de_creation, contenu_texte, utilisateur_id) VALUES(?, ?, ?);",
                model.getDateCreation(),
                model.getContenuTexte(),
                model.getUtilisateurId()
        );
    }

    @Override
    public Commentaire get(int id) {
        logger.debug("Entrée dans la méthode getByUserPseudo avec l'id " + id);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        CommentaireRM commentaireRM = new CommentaireRM();
        return jdbcTemplate.queryForObject("SELECT * FROM commentaire WHERE id = ?;", commentaireRM, id);
    }

    @Override
    public List<Commentaire> list() {
        logger.debug("Entrée dans la méthode list");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        CommentaireRM commentaireRM = new CommentaireRM();
        return jdbcTemplate.query("SELECT * FROM commentaire;", commentaireRM);
    }

    @Override
    public void update(Commentaire commentaire) {
        logger.debug("Entrée dans la méthode update");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTemplate.update("UPDATE commentaire SET (date_de_creation,contenu_texte,utilisateur_id) = (?,?,?) WHERE id = ?;",
                commentaire.getDateCreation(),
                commentaire.getContenuTexte(),
                commentaire.getUtilisateurId(),
                commentaire.getId());
    }

    @Override
    public void delete(Integer id) {
        logger.debug("Entrée dans la méthode create");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTemplate.update("DELETE FROM commentaire WHERE id = ?;", id);
    }

    /**
     * Cette méthode execute une requête SQL afin de récupérer les commentaires lié au topo.
     *
     * @param topoId l'id du topo lié au commentaire
     * @return La liste des commentaire lié au topo (cf requête SQL)
     */
    @Override
    public List<Commentaire> getCommentsByTopoId(Integer topoId) {
        logger.debug("Entrée dans la méthode getCommentsByTopoId avec le topoId : " + topoId);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        return jdbcTemplate.query("SELECT * FROM commentaire JOIN utilisateur ON commentaire.utilisateur_id = utilisateur.id WHERE commentaire.id IN " +
                        "( SELECT commentaire_sur_topo.commentaire_id FROM commentaire_sur_topo WHERE commentaire_sur_topo.topo_id = ?);"
                , (rs, rowNum) -> {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setId(rs.getInt("id"));
                    commentaire.setDateCreation(rs.getTimestamp("date_de_creation"));
                    commentaire.setContenuTexte(rs.getString("contenu_texte"));
                    commentaire.setUtilisateurId(rs.getInt("utilisateur_id"));
                    commentaire.setUtilisateur(new Utilisateur(rs.getString("pseudo")));
                    return commentaire;
                }, topoId);
    }

    /**
     * @param commentaire le commentaire pour lequel on veut connaitre l'Id
     * @return l'id du commentaire.
     */
    @Override
    public Integer getId(Commentaire commentaire) {
        logger.debug("Entrée dans la méthode getId");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        return jdbcTemplate.queryForObject("SELECT id FROM commentaire WHERE (date_de_creation,contenu_texte,utilisateur_id) = (?,?,?);",
                (rs, rowNum) -> {
                    return rs.getInt("id");
                },
                commentaire.getDateCreation(),
                commentaire.getContenuTexte(),
                commentaire.getUtilisateurId());
    }
}
