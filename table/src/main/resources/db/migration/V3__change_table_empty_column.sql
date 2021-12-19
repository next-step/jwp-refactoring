ALTER TABLE order_table
    ADD status VARCHAR(10) NULL;

UPDATE order_table
SET status = CASE empty
                 WHEN true THEN 'EMPTY'
                 ELSE 'FULL' END;

ALTER TABLE order_table
    ALTER status VARCHAR(10) NOT NULL;

ALTER TABLE order_table drop empty;
