package com.gg.proj.consumer.impl.dao;

import com.gg.proj.consumer.contract.dao.TopoDao;
import com.gg.proj.consumer.impl.rowmapper.TopoRM;
import com.gg.proj.model.bean.Topo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.inject.Named;
import java.util.List;

@Named
//@Singleton
public class TopoDaoImpl extends AbstractDaoImpl implements TopoDao {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void create(Topo model) {
        logger.debug("Entrée dans la méthode create");
        JdbcTemplate jdbcTempplate = new JdbcTemplate(getDataSource());
        jdbcTempplate.update("INSERT INTO topo (auteur, titre, description, empreintable) VALUES(?, ?, ?, ?);",
                model.getAuteur(),
                model.getTitre(),
                model.getDescription(),
                model.isEmpreintable()
        );
    }

    @Override
    public Topo get(int id) {
        logger.debug("Entrée dans la méthode getByUserPseudo avec l'id " + id);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        TopoRM topoRM = new TopoRM();
        // RowMapper<T> ecrit via lambda
//                (rs, rowNum) -> {
//                    Topo t = new Topo();
//                    t.setId(rs.getInt("id"));
//                    t.setTitre(rs.getString("titre"));
//                    t.setAuteur(rs.getString("auteur"));
//                    t.setDescription(rs.getString("description"));
//                    t.setEmpreintable(rs.getBoolean("empreintable"));
//                    return t;
//                },
        return jdbcTemplate.queryForObject("SELECT * FROM topo WHERE id = ?;", topoRM, id /* Paramètre '?' de la requête */);
    }

    @Override
    public List list() {
        logger.debug("Entrée dans la méthode list");
        List<Topo> topos;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        TopoRM topoRM = new TopoRM();
        topos = jdbcTemplate.query("SELECT * FROM topo;", topoRM);
        return topos;
    }

    @Override
    public void update(Topo model) {
        logger.debug("Entrée dans la méthode update");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTemplate.update("UPDATE topo SET (auteur, titre, description, empreintable) = (?,?,?,?) WHERE id = ? ;",
                model.getAuteur(),
                model.getTitre(),
                model.getDescription(),
                model.isEmpreintable(),
                model.getId()
        );

    }

    @Override
    public void delete(Integer id) {
        logger.debug("Entrée dans la méthode create");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        jdbcTemplate.update("DELETE FROM topo WHERE id = ?;", id);
    }

    /**
     *
     * @param topo
     * @return L'id du topo
     */
    @Override
    public Integer getId(Topo topo){
        logger.debug("Entrée dans la méthode getId avec le titre topo : " + topo.getTitre());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        return jdbcTemplate.queryForObject("SELECT id FROM topo WHERE (titre,description,auteur,empreintable) = (?,?,?,?);",
                /* RowMapper : */
                (rs, rowNum) -> {return rs.getInt("id");},
                /* Params '?' */
                topo.getTitre(),
                topo.getDescription(),
                topo.getAuteur(),
                topo.isEmpreintable()
        );
    }
}
