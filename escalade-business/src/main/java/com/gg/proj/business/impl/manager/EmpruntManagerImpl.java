package com.gg.proj.business.impl.manager;

import com.gg.proj.business.contract.manager.EmpruntManager;
import com.gg.proj.consumer.contract.dao.EmpruntDao;
import com.gg.proj.consumer.contract.dao.TopoDao;
import com.gg.proj.model.bean.Emprunt;
import com.gg.proj.model.bean.Topo;
import com.gg.proj.model.bean.Utilisateur;
import com.gg.proj.technical.ConvertisseurDate;
import com.gg.proj.technical.exceptions.DateInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
public class EmpruntManagerImpl implements EmpruntManager {

    private static final Logger logger = LogManager.getLogger();

    @Inject
    EmpruntDao empruntDao;

    @Inject
    TopoDao topoDao;

    @Override
    @Transactional
    public void create(Emprunt emprunt) {
        logger.debug("Entrée dans la méthode create");
        if (emprunt.getUtilisateurId() != null) {
            LocalDate date = LocalDate.now();
            emprunt.setDateEmprunt(date);
            emprunt.setDateRetour(date.plusWeeks(3));
            empruntDao.create(emprunt);
        } else
            logger.warn("Un emprunt doit être lié a un utilisateur par utilisateur_id");
    }

    @Override
    @Transactional
    public void create(Emprunt emprunt, Date date) throws DateInputException {
        logger.debug("Entrée dans la méthode create avec Date en paramêtre");
        ConvertisseurDate cDate = new ConvertisseurDate();
        LocalDate dateRetour = cDate.convertToLocalDateViaInstant(date);
        LocalDate dateEmprunt = LocalDate.now();

        if (dateEmprunt.isAfter(dateRetour)) {
            throw new DateInputException("La date de retour doit être ultérieur à la date d'emprunt");
        } else {
            emprunt.setDateEmprunt(dateEmprunt);
            emprunt.setDateRetour(dateRetour);

            empruntDao.create(emprunt);
        }
    }

    @Override
    public Emprunt get(int id) {
        logger.debug("Entrée dans la méthode getByUserPseudo avec l'id " + id);
        return empruntDao.get(id);
    }

    @Override
    public List<Emprunt> list() {
        logger.debug("Entrée dans la méthode list");
        return empruntDao.list();
    }

    @Override
    @Transactional
    public void update(Emprunt model) {
        logger.debug("Entrée dans la méthode update");
        empruntDao.update(model);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        logger.debug("Entrée dans la méthode delete avec l'id " + id);
        empruntDao.delete(id);
    }

    /**
     * Cette méthode solicite la dao pour récupérer la liste des topos disponibles à l'emprunt
     *
     * @return la liste de topo disponible
     */
    @Override
    @Transactional
    public List<Topo> listAvailableTopo(Integer utilisateurId, boolean displayMyOwn) {
        logger.debug("Entrée dans la méthode listAvailableTopo");
        List<Topo> listTopo = topoDao.listAvailableTopo();
        List<Topo> listTopoReworked = new ArrayList<Topo>();
        for (Topo topo : listTopo) {
            // On raccourci la description à 50 caractère...
            topo.setDescription(shortenDescription(topo.getDescription()));
            // ... et on exclus les topos dont le propriétaire est identique à l'utilisateur qui fait la requête
            if (!topo.getProprietaireId().equals(utilisateurId) || displayMyOwn) {
                listTopoReworked.add(topo);
            }
        }
        return listTopoReworked;
    }

    /**
     * Cette méthode solicite la dao pour récupérer les topo qui n'ont pas encore été emprunter et qui appartiennent a l'
     * utilisateur d'id utilisateurId
     * @param utilisateurId l'id de l'utilisateur concerné
     * @return une liste de topos qui sont disponibles à l'emprunt
     */
    @Override
    @Transactional
    public List<Topo> listMyAvailableTopo(Integer utilisateurId){
        logger.debug("Entrée dans la méthode listMyAvailableTopo avec l'utilisateurId : " + utilisateurId);
        List<Topo> listTopo = topoDao.listAvailableTopoByUtilisateurId(utilisateurId);
        for (Topo topo : listTopo) {
            // On raccourci la description à 50 caractère...
            topo.setDescription(shortenDescription(topo.getDescription()));
        }
        return listTopo;
    }

    //todo javadoc
    @Override
    @Transactional
    public List<Topo> listBorrowedTopo(Integer borrowerId) {
        logger.debug("Entrée dans la méthode listBorrowedTopo avec le borrowerId" + borrowerId);
        List<Topo> listTopo = topoDao.listTopoByBorrowerId(borrowerId);
        for (Topo topo : listTopo) {
            // On raccourci la description à 50 caractère...
            topo.setDescription(shortenDescription(topo.getDescription()));
        }
        return listTopo;
    }

    // todo javadoc
    @Override
    @Transactional
    public List<Topo> listLoanedTopo(Integer loanerId) {
        logger.debug("Entrée dans la méthode listLoanedTopo avec le loanerId" + loanerId);
        List<Topo> listTopo = topoDao.listTopoByLoanerId(loanerId);
        for (Topo topo : listTopo) {
            // On raccourci la description à 50 caractère...
            topo.setDescription(shortenDescription(topo.getDescription()));
        }
        return listTopo;
    }

    // todo javadoc
    @Override
    @Transactional
    public boolean isReserved(Integer topoId) {
        logger.debug("Entrée dans la méthode isReserved avec le topoId" + topoId);
        List<Emprunt> listEmprunt = empruntDao.getEmpruntByTopoId(topoId);
        for (Emprunt e : listEmprunt) {
            if (e.getDateRetour().isAfter(LocalDate.now()))
                return true;
        }
        return false;
    }

    // todo javadoc
    @Override
    @Transactional
    public List<Emprunt> listEmpruntByTopoId(Integer topoId) {
        return empruntDao.getFullEmpruntByTopoId(topoId);
    }

    /**
     * Cette méthode retourne le bean emprunt, pour l'emprunt en cour concernant un topo
     *
     * @param topoId
     * @return
     */
    // todo meilleur avec un while
    @Override
    @Transactional
    public Emprunt getCurrentByTopoId(Integer topoId) {
        Emprunt emprunt = new Emprunt();
        List<Emprunt> listEmprunt = empruntDao.getEmpruntByTopoId(topoId);
        for (Emprunt e : listEmprunt) {
            if (e.getDateRetour().isAfter(LocalDate.now())) {
                emprunt = e;
            }
        }
        return emprunt;
    }

    // todo javadoc
    private String shortenDescription(String description) {
        if (description.length() > 50) {
            description = description.substring(0, 46) + "...";
        }
        return description;
    }

}
