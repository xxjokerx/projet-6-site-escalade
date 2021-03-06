package com.gg.proj.model.bean;

public class Topo implements Model {

    // propriétés
    private Integer id;
    private Integer proprietaireId;
    private String auteur;
    private String titre;
    private String description;
    private Utilisateur emprunteur;
    private Emprunt emprunt;

    private boolean empreintable;

    // constructeurs
    public Topo() {
    }

    public Topo(Integer id) {
        this.id = id;
    }

    // setters & getters
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(Integer proprietaireId) {
        this.proprietaireId = proprietaireId;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmpreintable() {
        return empreintable;
    }

    public void setEmpreintable(boolean empreintable) {
        this.empreintable = empreintable;
    }

    public Utilisateur getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Utilisateur emprunteur) {
        this.emprunteur = emprunteur;
    }

    public Emprunt getEmprunt() {
        return emprunt;
    }

    public void setEmprunt(Emprunt emprunt) {
        this.emprunt = emprunt;
    }
}
