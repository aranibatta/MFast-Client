ALTER TABLE `mfast_test`.`measurementsession` 
CHANGE COLUMN `timestamp` `timestamp` TIMESTAMP NOT NULL ,
CHANGE COLUMN `level` `level` TINYINT(2) NULL DEFAULT NULL ,
ADD COLUMN `position` VARCHAR(45) NULL DEFAULT NULL AFTER `level`;
