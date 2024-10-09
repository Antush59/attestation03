START TRANSACTION;

INSERT INTO tires.customers (login, name, phone_number, car_model, dimension_of_tires)
VALUES ('johny_bee', 'John Lennon', '5553535', 'Ford', '180/65/r15'),
       ('antush', 'anton', '238573', 'volvo', '210/85/r22');

INSERT INTO tires.options (name, price)
VALUES ('option1', 1234.20),
       ('option2', 580.50);

INSERT INTO tires.orders (customer_login, creation_time)
VALUES ('johny_bee', NOW());

INSERT INTO tires.order_option (order_id, option_id)
VALUES (1, 1);

COMMIT;