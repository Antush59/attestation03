BEGIN TRANSACTION;

INSERT INTO tires.options (name, price)
VALUES ('option1', 1234.2);

COMMIT;