package com.gg.proj.business.impl.manager;

import com.gg.proj.business.contract.manager.VoieManager;
import com.gg.proj.consumer.contract.dao.CommentaireDao;
import com.gg.proj.consumer.contract.dao.CommentaireSurVoieDao;
import com.gg.proj.consumer.contract.dao.VoieDao;
import com.gg.proj.model.bean.Commentaire;
import com.gg.proj.model.bean.CommentaireSurVoie;
import com.gg.proj.model.bean.Voie;
import com.gg.proj.technical.GenerateurDeDifficulte;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.expression.ExpressionException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@Named
public class VoieManagerImpl implements VoieManager {

    private static final Logger logger = LogManager.getLogger();

    @Inject
    VoieDao voieDao;

    @Inject
    CommentaireDao commentaireDao;

    @Inject
    CommentaireSurVoieDao commentaireSurVoieDao;

    @Override
    @Transactional
    public void create(Voie model) throws ExpressionException {
        logger.debug("Entrée dans la méthode create");
        // On vérifie que le model contienne bien l'id d'un site
        if (model.getSecteurId() != null) {
            // puis on vérifie que getNom soit non null
            if (model.getNom() != null) {
                String expression = model.getCotation();
                if(!expression.matches("^[1-9][a-cA-C][-+]?$"))
                    throw new ExpressionException("La cotation doit respecter les standarts français, le ? et le a/b ne sont pas pris en compte");
                voieDao.create(model);
            } else
                logger.warn("Voie doit posséder un nom");
        } else
            logger.warn("Voie doit être lié à un secteur par secteurId");
    }

    @Override
    @Transactional
    public void create(Voie voie, Integer secteurId) throws ExpressionException {
        logger.debug("Entrée dans la méthode create surchargée par secteurId " + secteurId);
        voie.setSecteurId(secteurId);
        String expression = voie.getCotation();
        if(!expression.matches("^[1-9][a-cA-C][-+]?$"))
            throw new ExpressionException("La cotation doit respecter les standarts français, le ? et le a/b ne sont pas pris en compte");
        voieDao.create(voie);
    }
    @Override
    public Voie get(int id) {
        logger.debug("Entrée dans la méthode getByUserPseudo avec l'id " + id);
        return voieDao.get(id);
    }

    @Override
    public List<Voie> list() {
        logger.debug("Entrée dans la méthode list");
        return voieDao.list();
    }

    @Override
    @Transactional
    public void update(Voie model) throws ExpressionException {
        logger.debug("Entrée dans la méthode update");
        String expression = model.getCotation();
        if(!expression.matches("^[1-9][a-cA-C][-+]?$"))
            throw new ExpressionException("La cotation doit respecter les standarts français, le ? et le a/b ne sont pas pris en compte");
        voieDao.update(model);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        logger.debug("Entrée dans la méthode delete avec l'id " + id);
        voieDao.delete(id);
    }

    /**
     * Cette méthode permet l'ajout d'un commentaire lié à un voie en BDD. Cette méthode se charger d'ajouté le timestamp sur l'objet commentaire.
     * Elle ajoute un commentaire en bdd et ajoute également une ligne dans la table commentaire_sur_voie.
     *
     * @param commentaire un objet commentaire dont la propriété contenuTexte est non null
     * @param voieId      l'id du voie associé
     */
    @Override
    @Transactional
    public void addComment(Commentaire commentaire, Integer voieId) {
        logger.debug("Entrée dans la méthode addComment avec l'id " + voieId);
        // Ajout du timestamp de la création du commentaire
        commentaire.setDateCreation(Timestamp.from(Instant.now()));
        // Solocitation de la Dao pour création du commentaire
        commentaireDao.create(commentaire);
        // On récupère l'id
        Integer commentaireId = commentaireDao.getId(commentaire);
        // Il reste a créer une entrée dans la table de composition commentaire_sur_voie
        // On créé un bean CommentaireSurVoie pour lui attribuer les valeurs.
        CommentaireSurVoie commentaireSurVoie = new CommentaireSurVoie();
        commentaireSurVoie.setCommentaireId(commentaireId);
        commentaireSurVoie.setVoieId(voieId);
        // Création de l'entrée
        commentaireSurVoieDao.create(commentaireSurVoie);
    }


    /**
     * Commentaire qui renvoyé la list des commentaire associés avec une voie par son ID
     *
     * @param voieId
     * @return liste de commentaire
     */
    @Override
    @Transactional
    public List<Commentaire> listComments(Integer voieId) {
        logger.debug("Entrée dans la méthode listComments avec l'id " + voieId);
        return commentaireDao.getCommentsByVoieId(voieId);
    }

    /**
     * Solicite la Dao pour recherche de la liste des voies
     *
     * @param termeDeLaRecherche
     * @return une liste de voies correspondant.
     */
    @Override
    @Transactional
    public List<Voie> search(String termeDeLaRecherche) {
        logger.debug("Entrée dans la méthode search avec le terme de recherche :" +termeDeLaRecherche);
        return voieDao.search(termeDeLaRecherche);
    }

    /**
     * Solocite la Dao pour récupérer les voies associées à un secteur donné.
     *
     * @param secteurId
     * @return la liste des voies
     */
    @Override
    @Transactional
    public List<Voie> listLinkedVoie(Integer secteurId) {
        logger.debug("Entrée dans la méthode listLinkedVoie avec le secteurId " + secteurId);
        return voieDao.listBySecteurId(secteurId);
    }

    /**
     * Cette fonction reçoit deux difficultés de grimpe, elle génère une liste des difficultés intermédiaires et envoit
     * la liste à la dao voieDao pour traitement.
     *
     * @param minDiff la difficulté minimale à rechercher
     * @param maxDiff la difficulté maximale à rechercher
     * @return un liste des voies.
     */
    @Override
    @Transactional
    public List<Voie> advancedSearchByDifficulty(String minDiff, String maxDiff, String termeDeLaRecherche) throws InputMismatchException {
        logger.debug("Entrée dans la méthode advancedSearchByDifficulty avec minDiff : " + minDiff + " et maxDiff : " + maxDiff);

        if (GenerateurDeDifficulte.isOrdinate(minDiff, maxDiff)) {
            List<Voie> listRetrievedVoie = voieDao.search(termeDeLaRecherche);
            if (!listRetrievedVoie.isEmpty()) {
                List<Integer> listVoieId = new ArrayList<>();
                for (Voie t : listRetrievedVoie) {
                    listVoieId.add(t.getId());
                }
                listRetrievedVoie = voieDao.listVoieByDifficulty(GenerateurDeDifficulte.Generateur(minDiff, maxDiff), listVoieId);
            }
            return listRetrievedVoie;
        } else {
            throw new InputMismatchException("La cotation max doit être supérieur à la cotation min");
        }
    }

    /**
     * Cette méthode extrait le nom et le secteurId du bean voie reçu en paramètre et solicite la dao pour retourner l'id
     * associée.
     *
     * @param voie le bean de la voie dont on veut l'Id
     * @return l'id de la voie recherchée
     */
    @Override
    @Transactional
    public Integer getId(Voie voie) {
        return voieDao.getId(voie.getNom(), voie.getSecteurId());
    }
}
