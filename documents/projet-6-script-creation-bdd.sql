
CREATE TABLE public.site (
                id INTEGER NOT NULL,
                nom VARCHAR NOT NULL,
                description VARCHAR,
                coordonnees_gps POINT NOT NULL,
                CONSTRAINT site_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN public.site.coordonnees_gps IS 'Type : Point';


CREATE TABLE public.secteur (
                id INTEGER NOT NULL,
                nom VARCHAR NOT NULL,
                decription VARCHAR,
                coordonnees_gps POINT NOT NULL,
                site_id INTEGER NOT NULL,
                CONSTRAINT secteur_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN public.secteur.coordonnees_gps IS 'type : point';


CREATE TABLE public.voie (
                id INTEGER NOT NULL,
                nom VARCHAR NOT NULL,
                description VARCHAR,
                coordonnees_gps POINT NOT NULL,
                nombre_points INTEGER NOT NULL,
                cotation VARCHAR(10) NOT NULL,
                hauteur VARCHAR NOT NULL,
                secteur_id INTEGER NOT NULL,
                CONSTRAINT voie_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN public.voie.coordonnees_gps IS 'type : point';


CREATE SEQUENCE public.topo_id_seq;

CREATE TABLE public.topo (
                id INTEGER NOT NULL DEFAULT nextval('public.topo_id_seq'),
                titre VARCHAR NOT NULL,
                auteur VARCHAR,
                description VARCHAR,
                CONSTRAINT topo_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.topo_id_seq OWNED BY public.topo.id;

CREATE TABLE public.composition_site_topo (
                topo_id INTEGER NOT NULL,
                site_id INTEGER NOT NULL,
                CONSTRAINT composition_site_topo_pk PRIMARY KEY (topo_id, site_id)
);


CREATE TABLE public.utilisateur (
                uuid VARCHAR(32) NOT NULL,
                nom VARCHAR NOT NULL,
                prenom VARCHAR NOT NULL,
                pseudo VARCHAR NOT NULL,
                date_inscription DATE NOT NULL,
                description VARCHAR,
                adresse_mail VARCHAR NOT NULL,
                hash_du_mot_de_passe VARCHAR(60) NOT NULL,
                CONSTRAINT utilisateur_id_pk PRIMARY KEY (uuid)
);


CREATE SEQUENCE public.empreint_id_seq;

CREATE TABLE public.empreint (
                id INTEGER NOT NULL DEFAULT nextval('public.empreint_id_seq'),
                date_empreint DATE NOT NULL,
                date_retour DATE NOT NULL,
                utilisateur_uuid VARCHAR(32) NOT NULL,
                CONSTRAINT empreint_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.empreint_id_seq OWNED BY public.empreint.id;

CREATE TABLE public.empreint_de_topos (
                topo_id INTEGER NOT NULL,
                empreint_id INTEGER NOT NULL,
                CONSTRAINT empreint_de_topos_pk PRIMARY KEY (topo_id, empreint_id)
);


CREATE SEQUENCE public.commentaire_id_seq;

CREATE TABLE public.commentaire (
                id INTEGER NOT NULL DEFAULT nextval('public.commentaire_id_seq'),
                date_de_creation TIMESTAMP NOT NULL,
                utilisateur_uuid VARCHAR(32) NOT NULL,
                contenu_texte VARCHAR NOT NULL,
                CONSTRAINT commentaire_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.commentaire_id_seq OWNED BY public.commentaire.id;

CREATE TABLE public.commentaire_sur_voie (
                voie_id INTEGER NOT NULL,
                commentaire_id INTEGER NOT NULL,
                CONSTRAINT commentaire_sur_voie_pk PRIMARY KEY (voie_id, commentaire_id)
);


CREATE TABLE public.commentaire_sur_site (
                site_id INTEGER NOT NULL,
                commentaire_id INTEGER NOT NULL,
                CONSTRAINT commentaire_sur_site_pk PRIMARY KEY (site_id, commentaire_id)
);


CREATE TABLE public.commentaire_sur_topo (
                topo_id INTEGER NOT NULL,
                commentaire_id INTEGER NOT NULL,
                CONSTRAINT commentaire_sur_topo_pk PRIMARY KEY (topo_id, commentaire_id)
);


ALTER TABLE public.composition_site_topo ADD CONSTRAINT site_site_associe_fk
FOREIGN KEY (site_id)
REFERENCES public.site (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.commentaire_sur_site ADD CONSTRAINT site_commentaire_sur_site_fk
FOREIGN KEY (site_id)
REFERENCES public.site (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.secteur ADD CONSTRAINT site_secteur_fk
FOREIGN KEY (site_id)
REFERENCES public.site (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.voie ADD CONSTRAINT secteur_voie_fk
FOREIGN KEY (secteur_id)
REFERENCES public.secteur (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.commentaire_sur_voie ADD CONSTRAINT voie_commentaire_sur_voie_fk
FOREIGN KEY (voie_id)
REFERENCES public.voie (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.composition_site_topo ADD CONSTRAINT topo_site_associe_fk
FOREIGN KEY (topo_id)
REFERENCES public.topo (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.commentaire_sur_topo ADD CONSTRAINT topo_commentaire_sur_topo_fk
FOREIGN KEY (topo_id)
REFERENCES public.topo (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.empreint_de_topos ADD CONSTRAINT topo_empreint_de_topos_fk
FOREIGN KEY (topo_id)
REFERENCES public.topo (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.commentaire ADD CONSTRAINT utilisateur_commentaire_fk
FOREIGN KEY (utilisateur_uuid)
REFERENCES public.utilisateur (uuid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.empreint ADD CONSTRAINT utilisateur_empreint_fk
FOREIGN KEY (utilisateur_uuid)
REFERENCES public.utilisateur (uuid)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.empreint_de_topos ADD CONSTRAINT empreint_empreint_de_topos_fk
FOREIGN KEY (empreint_id)
REFERENCES public.empreint (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.commentaire_sur_topo ADD CONSTRAINT commentaire_commentaire_sur_topo_fk
FOREIGN KEY (commentaire_id)
REFERENCES public.commentaire (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.commentaire_sur_site ADD CONSTRAINT commentaire_commentaire_sur_site_fk
FOREIGN KEY (commentaire_id)
REFERENCES public.commentaire (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.commentaire_sur_voie ADD CONSTRAINT commentaire_commentaire_sur_voie_fk
FOREIGN KEY (commentaire_id)
REFERENCES public.commentaire (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
