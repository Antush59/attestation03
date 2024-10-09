BEGIN TRANSACTION;

INSERT INTO tires.customers (login, name, phone_number, car_model, dimension_of_tires)
VALUES ('johny_bee', 'John Lennon', '5553535', 'Ford', '180/65/r15');

INSERT INTO tires.options (name, price)
VALUES ('option1', 1234.2);

INSERT INTO tires.orders (customer_login, creation_time)
VALUES ('johny_bee', NOW());

INSERT INTO tires.order_option (order_id, option_id)
VALUES (1, 1);

COMMIT;