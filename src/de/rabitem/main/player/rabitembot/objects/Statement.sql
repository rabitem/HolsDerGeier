SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema holsdergeier
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema holsdergeier
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `holsdergeier` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `holsdergeier` ;

-- -----------------------------------------------------
-- Table `holsdergeier`.`bot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `holsdergeier`.`bot` (
                                                    `idBot` INT NOT NULL AUTO_INCREMENT,
                                                    `Name` VARCHAR(45) NULL DEFAULT NULL,
    `LastPlayedAgainst` DATE NULL DEFAULT NULL,
    `Wins` INT NULL DEFAULT '0',
    `Loses` INT NULL DEFAULT '0',
    `Draws` INT NULL DEFAULT '0',
    PRIMARY KEY (`idBot`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 29
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `holsdergeier`.`playedcards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `holsdergeier`.`playedcards` (
                                                            `idPlayedCards` INT NOT NULL AUTO_INCREMENT,
                                                            `Bot` INT NOT NULL,
                                                            PRIMARY KEY (`idPlayedCards`),
    INDEX `fk_PlayedCards_Bot_idx` (`Bot` ASC) VISIBLE,
    CONSTRAINT `fk_PlayedCards_Bot`
    FOREIGN KEY (`Bot`)
    REFERENCES `holsdergeier`.`bot` (`idBot`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 29
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `holsdergeier`.`card`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `holsdergeier`.`card` (
                                                     `idCard` INT NOT NULL AUTO_INCREMENT,
                                                     `Value` INT NULL DEFAULT NULL,
                                                     `TimesUsedFor1` INT NULL DEFAULT '0',
                                                     `TimesUsedFor2` INT NULL DEFAULT '0',
                                                     `TimesUsedFor3` INT NULL DEFAULT '0',
                                                     `TimesUsedFor4` INT NULL DEFAULT '0',
                                                     `TimesUsedFor5` INT NULL DEFAULT '0',
                                                     `TimesUsedFor6` INT NULL DEFAULT '0',
                                                     `TimesUsedFor7` INT NULL DEFAULT '0',
                                                     `TimesUsedFor8` INT NULL DEFAULT '0',
                                                     `TimesUsedFor9` INT NULL DEFAULT '0',
                                                     `TimesUsedFor10` INT NULL DEFAULT '0',
                                                     `TimesUsedForMinus1` INT NULL DEFAULT '0',
                                                     `TimesUsedForMinus2` INT NULL DEFAULT '0',
                                                     `TimesUsedForMinus3` INT NULL DEFAULT '0',
                                                     `TimesUsedForMinus4` INT NULL DEFAULT '0',
                                                     `TimesUsedForMinus5` INT NULL DEFAULT '0',
                                                     `fk_PlayedCards` INT NOT NULL,
                                                     PRIMARY KEY (`idCard`),
    INDEX `fk_Card_PlayedCards1_idx` (`fk_PlayedCards` ASC) VISIBLE,
    CONSTRAINT `fk_Card_PlayedCards1`
    FOREIGN KEY (`fk_PlayedCards`)
    REFERENCES `holsdergeier`.`playedcards` (`idPlayedCards`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 406
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
