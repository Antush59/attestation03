BEGIN TRANSACTION;

INSERT INTO tires.customers (login, name, phone_number, car_model, dimension_of_tires)
VALUES ('johny_bee', 'John Lennon', '5553535', 'Ford', '180/65/r15');

COMMIT;

