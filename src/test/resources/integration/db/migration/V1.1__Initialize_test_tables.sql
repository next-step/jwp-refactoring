ALTER TABLE menu
DROP COLUMN price;

ALTER TABLE menu
    ADD price BIGINT(20);

ALTER TABLE product
DROP COLUMN price;

ALTER TABLE product
    ADD price BIGINT(20);