-- Création de la table User
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       mail VARCHAR(100) UNIQUE NOT NULL,
                       mot_de_passe VARCHAR(100) NOT NULL,
                       role VARCHAR(100) NOT NULL
);

-- Création de la table Voiture
CREATE TABLE voitures (
                          id SERIAL PRIMARY KEY,
                          marque VARCHAR(100),
                          modele VARCHAR(100),
                          annee INTEGER,
                          conso INTEGER
);

CREATE TABLE user_voiture (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER NOT NULL REFERENCES users(id),
                          voiture_id INTEGER NOT NULL REFERENCES voitures(id)
);

-- Création de la table Borne
CREATE TABLE bornes (
                        id SERIAL PRIMARY KEY,
                        type_borne_id INTEGER NOT NULL,
                        status VARCHAR(100),
                        coord_x INTEGER,
                        coord_y INTEGER
);

-- Création de la table Type_Borne
CREATE TABLE types_borne (
                             id SERIAL PRIMARY KEY,
                             nom VARCHAR(100) NOT NULL
);

-- Création de la table Borne_Carte
CREATE TABLE bornes_cartes (
                               id SERIAL PRIMARY KEY,
                               carte_id INTEGER NOT NULL,
                               borne_id INTEGER NOT NULL REFERENCES bornes(id),
                               CONSTRAINT fk_borne
                                   FOREIGN KEY(borne_id)
                                       REFERENCES bornes(id)
                                       ON DELETE CASCADE
);

-- Création de la table Carte
CREATE TABLE cartes (
                        id SERIAL PRIMARY KEY,
                        type VARCHAR(100) NOT NULL,
                        nom VARCHAR(100) NOT NULL
);

-- Création de la table Reserver
CREATE TABLE reservations (
                              id SERIAL PRIMARY KEY,
                              user_id INTEGER NOT NULL REFERENCES users(id),
                              borne_id INTEGER NOT NULL REFERENCES bornes(id),
                              date_debut DATE NOT NULL,
                              date_fin DATE NOT NULL
);

-- Création de la table Signalement
CREATE TABLE signalements (
                              id SERIAL PRIMARY KEY,
                              borne_id INTEGER NOT NULL REFERENCES bornes(id),
                              user_id INTEGER NOT NULL REFERENCES users(id),
                              date DATE NOT NULL,
                              etat VARCHAR(100),
                              motif VARCHAR(255)
);

-- Insertion de données d'exemple pour les tables nouvellement créées
INSERT INTO users (mail, mot_de_passe, role) VALUES ('admin@cats.com', 'securepass', 'admin');
INSERT INTO users (mail, mot_de_passe, role) VALUES ('user@cats.com', 'pass', 'user');
INSERT INTO voitures (marque, modele, annee, conso) VALUES ('Tesla', 'Model S', 2019, 20);
INSERT INTO user_voiture (user_id, voiture_id) VALUES (1, 1);
INSERT INTO types_borne (nom) VALUES ('Type1'), ('Type2');
INSERT INTO bornes (type_borne_id, status, coord_x, coord_y) VALUES (1, 'Actif', 123, 456);
INSERT INTO cartes (type, nom) VALUES ('TypeA', 'Carte1');
INSERT INTO bornes_cartes (carte_id, borne_id) VALUES (1, 1);
INSERT INTO reservations (user_id, borne_id, date_debut, date_fin) VALUES (1, 1, '2023-12-01', '2023-12-02');
INSERT INTO signalements (borne_id, user_id, date, etat, motif) VALUES (1, 1, '2023-12-01', 'En panne', 'Ne charge plus');
