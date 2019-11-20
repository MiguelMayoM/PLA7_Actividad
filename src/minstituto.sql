-- MySQL dump 10.16  Distrib 10.1.37-MariaDB, for Win32 (AMD64)
--
-- Host: 127.0.0.1    Database: instituto
-- ------------------------------------------------------
-- Server version	10.1.37-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--  Cambios respecto al original:
--  -----------------------------
-- + Reubicación al principio del script de la creación de aquellas tablas que
-- 	 aportarán claves extranjeras.
-- + Obviamente, statements en 1 línea para el import.sql de la opción hbm2dml.auto
--   del hibernate.cfg.xml
-- + Líneas añadidas para crear la BD o regenerar las tablas. En este sentido,
-- 	 primero se han de eliminar aquellas que estén utilizando claves extranjeras
-- 	 de otras tablas.

--DROP DATABASE IF EXISTS minstituto;
--CREATE DATABASE minstituto IF NOT EXISTS;

--  Como el script es para ser llamado desde Java, quito esta sentencia porque
-- 	me da un warning que es molesto y la pongo en la "propertie" del conector
-- 	createDatabaseIfNotExist (sin "s"). Por otra parte, la primera vez, cuando
-- 	se crea la BD, también saltarán los warnings siguientes relativos a los
-- 	DROP de las tablas porque no existirán. Pero también en las sucesivas veces
-- 	porque los IF (NOT) EXISTS dan warnings aunque se cumplan, está montado así.
-- 	No obstante, estos últimos warnings no se muestran si uso el dialecto
-- 	MySQL8Dialect
  
USE `minstituto`;
DROP TABLE IF EXISTS `mod_alu`;
DROP TABLE IF EXISTS `modulos`;
DROP TABLE IF EXISTS `alumnos`;
DROP TABLE IF EXISTS `profesores`;

-- Table structure for table `profesores`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profesores` (`idprofesor` int(11) NOT NULL AUTO_INCREMENT, `dni` varchar(45) DEFAULT NULL, `nombre` varchar(45) DEFAULT NULL, `email` varchar(45) DEFAULT NULL, PRIMARY KEY (`idprofesor`)) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `profesores`
--
LOCK TABLES `profesores` WRITE;
/*!40000 ALTER TABLE `profesores` DISABLE KEYS */;
INSERT INTO `profesores` VALUES (1,'12345678z','Ana Pi','ana@pi.com'),(4,'12345678z','Ana Pi','ana@pi.com'),(5,'12345678z','Ana Pi','ana@pi.com');
/*!40000 ALTER TABLE `profesores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alumnos`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alumnos` (`idalumno` int(11) NOT NULL AUTO_INCREMENT, `nombre` varchar(45) DEFAULT NULL, `email` varchar(45) DEFAULT NULL, PRIMARY KEY (`idalumno`)) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `alumnos`
--
LOCK TABLES `alumnos` WRITE;
/*!40000 ALTER TABLE `alumnos` DISABLE KEYS */;
INSERT INTO `alumnos` VALUES (1,'Eva pi','pi@pi.com'),(2,'Ot Buj','ot@ot.com'),(3,'Iu Ros','pi@ot.com');
/*!40000 ALTER TABLE `alumnos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modulos`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modulos` (`idmodulo` int(11) NOT NULL AUTO_INCREMENT, `idprofesor` int(11) DEFAULT NULL, `nombre` varchar(45) DEFAULT NULL, PRIMARY KEY (`idmodulo`), KEY `fk_profesor_idx` (`idprofesor`), CONSTRAINT `fk_profesor` FOREIGN KEY (`idprofesor`) REFERENCES `profesores` (`idprofesor`) ON UPDATE CASCADE) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `modulos`
--
LOCK TABLES `modulos` WRITE;
/*!40000 ALTER TABLE `modulos` DISABLE KEYS */;
INSERT INTO `modulos` VALUES (2,1,'dd'),(4,4,'Java'),(5,1,'c#'),(6,1,'PHP'),(7,1,'JS'),(8,1,'c#'),(9,1,'PHP'),(10,1,'JS'),(11,5,'Java'),(12,NULL,'Java'),(13,NULL,'Node');
/*!40000 ALTER TABLE `modulos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mod_alu`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_alu` (`idmod_alu` int(11) NOT NULL AUTO_INCREMENT, `idmodulo` int(11) DEFAULT NULL, `idalumno` int(11) DEFAULT NULL, PRIMARY KEY (`idmod_alu`), KEY `fk_modulo_idx` (`idmodulo`), KEY `fk_alumno_idx` (`idalumno`), CONSTRAINT `fk_alumno` FOREIGN KEY (`idalumno`) REFERENCES `alumnos` (`idalumno`) ON UPDATE CASCADE, CONSTRAINT `fk_modulo` FOREIGN KEY (`idmodulo`) REFERENCES `modulos` (`idmodulo`) ON UPDATE CASCADE) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `mod_alu`
--
LOCK TABLES `mod_alu` WRITE;
/*!40000 ALTER TABLE `mod_alu` DISABLE KEYS */;
INSERT INTO `mod_alu` VALUES (2,12,2),(3,12,3),(4,12,1),(5,13,1);
/*!40000 ALTER TABLE `mod_alu` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-28 10:50:16