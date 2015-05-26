ALTER TABLE `mfast`.`measurementframe` 
ADD COLUMN `realTimeStampSeconds` INT(11) NULL AFTER `frameTime`,
ADD COLUMN `realTimeStampMicros` INT(11) NULL AFTER `realTimeStampSeconds`,
DROP COLUMN `timestamp`;


SELECT * FROM mfast.measurementframe;


