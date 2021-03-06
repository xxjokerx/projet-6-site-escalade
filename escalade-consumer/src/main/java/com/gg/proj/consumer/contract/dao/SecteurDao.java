package com.gg.proj.consumer.contract.dao;

import com.gg.proj.consumer.contract.CrudDao;
import com.gg.proj.model.bean.Secteur;

import java.util.List;

public interface SecteurDao extends CrudDao<Secteur> {

    public List<Secteur> getBySiteId(Integer siteId);

    List<Secteur> search(String termeDeLaRecherche);

    Secteur getSecteurByVoieId(Integer voieId);

    List<Secteur> listSecteurByDifficulty(List<String> listDifficultes, List<Integer> listSecteurId);

    Integer getId(String nom, Integer siteId);
}
