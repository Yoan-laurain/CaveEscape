-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : lun. 07 nov. 2022 à 11:24
-- Version du serveur : 10.5.15-MariaDB-0+deb11u1
-- Version de PHP : 7.4.30

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
-- Structure de la table `history`
--

CREATE TABLE `history` (
  `idClient` varchar(50) NOT NULL,
  `dateInstall` date NOT NULL,
  `idHistory` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `map`
--

CREATE TABLE `map` (
  `idMap` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `nbRows` int(11) NOT NULL,
  `nbColumns` int(11) NOT NULL,
  `idClient` varchar(50) NOT NULL,
  `isTested` varchar(1) NOT NULL DEFAULT '0',
  `nbMoveMin` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `map`
--

INSERT INTO `map` (`idMap`, `nom`, `nbRows`, `nbColumns`, `idClient`, `isTested`, `nbMoveMin`) VALUES
(9, 'LEVEL 1', 6, 6, '1', '1', 4),
(10, 'LEVEL 2', 5, 6, '1', '1', 13),
(11, 'LEVEL 3', 6, 6, '1', '1', 38),
(12, 'LEVEL 4', 6, 7, '1', '1', 23),
(13, 'LEVEL 5', 7, 7, '1', '1', 35),
(14, 'LEVEL 6', 7, 7, '1', '1', 29),
(15, 'LEVEL 7', 6, 8, '1', '1', 49),
(16, 'LEVEL 8', 7, 8, '1', '1', 71),
(17, 'LEVEL 9', 6, 7, '1', '1', 34),
(18, 'LEVEL 10', 6, 8, '1', '1', 59),
(19, 'LEVEL 11', 6, 7, '1', '1', 26),
(20, 'LEVEL 12', 7, 7, '1', '1', 43),
(21, 'LEVEL 13', 7, 7, '1', '1', 31),
(22, 'LEVEL 14', 6, 7, '1', '1', 34),
(23, 'LEVEL 15', 6, 8, '1', '1', 38),
(24, 'LEVEL 16', 7, 7, '1', '1', 65),
(25, 'LEVEL 17', 6, 7, '1', '1', 22),
(26, 'LEVEL 18', 6, 8, '1', '1', 31),
(27, 'LEVEL 19', 7, 7, '1', '1', 33),
(28, 'LEVEL 20', 7, 7, '1', '1', 38);

-- --------------------------------------------------------

--
-- Structure de la table `map_ligne`
--

CREATE TABLE `map_ligne` (
  `id` int(11) NOT NULL,
  `indexRow` int(11) NOT NULL,
  `content` varchar(200) NOT NULL,
  `idMap` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `map_ligne`
--

INSERT INTO `map_ligne` (`id`, `indexRow`, `content`, `idMap`) VALUES
(89, 1, 'D..P.D', 9),
(90, 2, 'D.HEEB', 9),
(91, 3, 'DCD...', 9),
(92, 4, 'DXD...', 9),
(93, 5, 'AEB...', 9),
(94, 0, 'HEEEEI', 9),
(96, 0, 'HEEEEI', 10),
(97, 1, 'DP...D', 10),
(98, 4, 'AEEEEB', 10),
(99, 3, 'D.XC.D', 10),
(100, 2, 'D..CXD', 10),
(101, 0, 'HEEEI.', 11),
(102, 4, 'D....D', 11),
(103, 3, 'D..E.D', 11),
(104, 1, 'DP..AI', 11),
(105, 2, 'DXCW.D', 11),
(106, 5, 'AEEEEB', 11),
(107, 1, 'DP...AI', 12),
(108, 0, 'HEEEEI.', 12),
(109, 3, 'D.EX.XD', 12),
(110, 4, 'D.....D', 12),
(111, 2, 'D.CC..D', 12),
(112, 5, 'AEEEEEB', 12),
(113, 1, 'HEB..D.', 13),
(114, 0, '..HEEI.', 13),
(115, 2, 'DP.XCAI', 13),
(116, 3, 'D...C.D', 13),
(117, 4, 'D.EX..D', 13),
(118, 5, 'D.....D', 13),
(119, 6, 'AEEEEEB', 13),
(120, 0, '.HEEEI.', 14),
(121, 3, 'D.CWC.D', 14),
(122, 1, 'HBP..AI', 14),
(123, 5, 'AI.X.HB', 14),
(124, 2, 'D..E..D', 14),
(125, 4, 'D..X..D', 14),
(126, 6, '.AEEEB.', 14),
(127, 1, 'D....AEI', 15),
(128, 0, 'HEEEEI..', 15),
(129, 2, 'D...XX.D', 15),
(130, 3, 'D.CCCP.D', 15),
(131, 5, 'AEEUEEEB', 15),
(132, 4, 'D..J.X.D', 15),
(133, 0, '.HEEEI..', 16),
(134, 1, 'HBX..AEI', 16),
(135, 3, 'D..V.E.D', 16),
(136, 2, 'D..J.CPD', 16),
(137, 5, 'D....HEB', 16),
(138, 4, 'D.W..CXD', 16),
(139, 6, 'AEEEEB..', 16),
(140, 5, 'AEEEEB.', 17),
(141, 0, 'HEEEEI.', 17),
(142, 4, 'DX...HB', 17),
(143, 1, 'DPW..AI', 17),
(144, 3, 'D.C.X.D', 17),
(145, 2, 'D.CE..D', 17),
(146, 0, '.HEEEEI.', 18),
(147, 3, 'D.E.CCCD', 18),
(148, 1, 'HB....AI', 18),
(149, 2, 'D.XX...D', 18),
(150, 4, 'D...JPXD', 18),
(151, 5, 'AEEEUEEB', 18),
(152, 1, 'DXPW.AI', 19),
(153, 0, 'HEEEEI.', 19),
(154, 2, 'DC..C.D', 19),
(155, 3, 'D...E.D', 19),
(156, 4, 'D..X..D', 19),
(157, 5, 'AEEEEEB', 19),
(158, 2, 'D.JC.AI', 20),
(159, 1, 'DP.XXD.', 20),
(160, 0, 'HEEEEI.', 20),
(161, 3, 'D.V...D', 20),
(162, 5, 'D..W..D', 20),
(163, 4, 'D..CE.D', 20),
(164, 6, 'AEEEEEB', 20),
(165, 0, 'HEEEI..', 21),
(166, 1, 'D...D..', 21),
(167, 2, 'D.E.AEI', 21),
(168, 4, 'D..EC.D', 21),
(169, 3, 'DWXC..D', 21),
(170, 5, 'DP...XD', 21),
(171, 6, 'AEEEEEB', 21),
(172, 3, 'D...C.D', 22),
(173, 5, 'AEEEEB.', 22),
(174, 4, 'D.CX.HB', 22),
(175, 1, 'HEB..PD', 22),
(176, 0, '..HEEEI', 22),
(177, 2, 'DX.WE.D', 22),
(178, 0, 'HEEEEI..', 23),
(179, 1, 'DP...AI.', 23),
(180, 2, 'D.XCW.AI', 23),
(181, 3, 'D.ECX..D', 23),
(182, 4, 'D......D', 23),
(183, 5, 'AEEEEEEB', 23),
(184, 0, 'HEEEEI.', 24),
(185, 1, 'D..X.D.', 24),
(186, 3, 'D..X..D', 24),
(187, 2, 'D.EX.AI', 24),
(188, 4, 'D.CCC.D', 24),
(189, 5, 'D..J.PD', 24),
(190, 6, 'AEEUEEB', 24),
(191, 1, 'D..AEEI', 25),
(192, 0, 'HEEI...', 25),
(193, 2, 'D...WXD', 25),
(194, 3, 'D..CE.D', 25),
(195, 4, 'D..XCPD', 25),
(196, 5, 'AEEEEEB', 25),
(197, 4, 'D..XJ..D', 26),
(198, 5, 'AEEEUEEB', 26),
(199, 0, 'HEEEEEI.', 26),
(200, 3, 'D.EPC..D', 26),
(201, 1, 'D..X..AI', 26),
(202, 2, 'D.CCEX.D', 26),
(203, 0, 'HEEEEI.', 27),
(204, 6, 'AEEEEEB', 27),
(205, 1, 'DP...AI', 27),
(206, 3, '>G.XXXD', 27),
(207, 4, 'D...E.D', 27),
(208, 2, 'D.CCC.D', 27),
(209, 5, 'D.....D', 27),
(210, 1, 'D..AI..', 28),
(211, 5, 'D..X.XD', 28),
(212, 2, 'D...AI.', 28),
(213, 0, 'HEEI...', 28),
(214, 3, 'DC.CPAI', 28),
(215, 6, 'AEEEEEB', 28),
(216, 4, 'DX.EC.D', 28);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`idHistory`);

--
-- Index pour la table `map`
--
ALTER TABLE `map`
  ADD PRIMARY KEY (`idMap`);

--
-- Index pour la table `map_ligne`
--
ALTER TABLE `map_ligne`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Map_Ligne_Map_FK` (`idMap`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `history`
--
ALTER TABLE `history`
  MODIFY `idHistory` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT pour la table `map`
--
ALTER TABLE `map`
  MODIFY `idMap` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT pour la table `map_ligne`
--
ALTER TABLE `map_ligne`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=217;

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
