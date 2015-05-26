# This script will fix the issue with the weight being float, which causes issues
# when trying to compare with = sign. The script will turn the float column into 
# decimal (10,2)

#SELECT SUM(weight), SUM(weightStaged) FROM measurementsession LIMIT 10000;
SELECT * FROM `measurementsession` LIMIT 10000;

# Fix for issue with weight value comparison. Change to DECIMAL
#ALTER TABLE `measurementsession` DROP COLUMN `WeightStaged`;

# Add extra column that will convert current float values for weight to 2 digit
SET SQL_SAFE_UPDATES = 0;
ALTER TABLE `measurementsession` ADD `WeightStaged` float(10,2) NOT NULL;
UPDATE `measurementsession` SET `WeightStaged` = `weight`;
ALTER TABLE `measurementsession` MODIFY `WeightStaged` decimal(10,2) NOT NULL;
SELECT * FROM `measurementsession` WHERE round(`weight`,2) <> `WeightStaged`;

# Copy column to decimal
ALTER TABLE `measurementsession` DROP `weight`;
ALTER TABLE `measurementsession` CHANGE `WeightStaged` `weight` decimal(10,2) NOT NULL;

# Drop the extra column
SET SQL_SAFE_UPDATES = 1;

# Fix the column order
alter table `measurementsession` 
change column weight weight decimal(10,2) after `isCompleted`;
