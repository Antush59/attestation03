START TRANSACTION;

INSERT INTO tires.options (name, price)
VALUES ('option2', 1298.40),
       ('option3', 348.10);

COMMIT;