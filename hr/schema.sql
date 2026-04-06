-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema hr_platform
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hr_platform
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hr_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `hr_platform` ;

-- -----------------------------------------------------
-- Table `hr_platform`.`job_candidate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_platform`.`job_candidate` (
  `idjob_candidate` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `birthday` DATE NOT NULL,
  `contact_number` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idjob_candidate`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_platform`.`skill`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_platform`.`skill` (
  `idskill` INT NOT NULL,
  `skill_name` VARCHAR(45) NOT NULL,
  `idjob_candidate` INT NOT NULL,
  PRIMARY KEY (`idskill`),
  INDEX `fk_skill_job_candidate_idx` (`idjob_candidate` ASC) VISIBLE,
  CONSTRAINT `fk_skill_job_candidate`
    FOREIGN KEY (`idjob_candidate`)
    REFERENCES `hr_platform`.`job_candidate` (`idjob_candidate`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
