ALTER TABLE product
DROP COLUMN price;

ALTER TABLE product
    ADD price BIGINT(20);