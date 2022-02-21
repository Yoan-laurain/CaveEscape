-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 16 fév. 2022 à 16:16
-- Version du serveur : 5.7.36
-- Version de PHP : 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `caveescape`
--

-- --------------------------------------------------------

--
-- Structure de la table `map`
--

DROP TABLE IF EXISTS `map`;
CREATE TABLE IF NOT EXISTS `map` (
  `idMap` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `nbRows` int(11) NOT NULL,
  `nbColumns` int(11) NOT NULL,
  `idClient` int(11) NOT NULL,
  PRIMARY KEY (`idMap`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `map`
--

INSERT INTO `map` (`idMap`, `nom`, `nbRows`, `nbColumns`, `idClient`) VALUES
(1, 'Tutoriel', 6, 6, 1);

-- --------------------------------------------------------

--
-- Structure de la table `map_ligne`
--

DROP TABLE IF EXISTS `map_ligne`;
CREATE TABLE IF NOT EXISTS `map_ligne` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `indexRow` int(11) NOT NULL,
  `content` varchar(200) NOT NULL,
  `idMap` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `Map_Ligne_Map_FK` (`idMap`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `map_ligne`
--

INSERT INTO `map_ligne` (`id`, `indexRow`, `content`, `idMap`) VALUES
(2, 0, '######', 1),
(3, 1, '#..P.#', 1),
(4, 2, '#C####', 1),
(5, 3, '#.#...', 1),
(6, 4, '#X#...', 1),
(7, 5, '###...', 1);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `map_ligne`
--
ALTER TABLE `map_ligne`
  ADD CONSTRAINT `Map_Ligne_Map_FK` FOREIGN KEY (`idMap`) REFERENCES `map` (`idMap`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
