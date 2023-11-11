--Amy Nguyen,Bianca Rossetti

DROP TABLE Employees;
DROP TABLE Orders_Products ;
DROP TABLE Stores_Products;
DROP TABLE Warehouses_Products;
DROP TABLE Warehouses_Stores;
DROP TABLE Reviews;
DROP TABLE Orders;
DROP TABLE Customers;
DROP TABLE Products;
DROP TABLE Warehouses;
DROP TABLE Stores;

CREATE TABLE Stores(
    storeID NUMBER(2) GENERATED BY DEFAULT AS IDENTITY CONSTRAINT store_pk PRIMARY KEY,
    name VARCHAR2(20) NOT NULL
);

CREATE TABLE Warehouses(
    warehouseID      NUMBER(2)   GENERATED BY DEFAULT AS IDENTITY CONSTRAINT warehouse_pk PRIMARY KEY,
    name             VARCHAR2(20) NOT NULL,
    streetAddress    VARCHAR2(40),
    city             VARCHAR2(40),
    province         VARCHAR2(40),
    country          VARCHAR2(40)
);

CREATE TABLE Warehouses_Stores(
    warehouseID NUMBER(2)   REFERENCES Warehouses(warehouseID) ON DELETE CASCADE NOT NULL ,
    storeID     NUMBER(2)   REFERENCES Stores(storeID) ON DELETE CASCADE NOT NULL
);

CREATE TABLE Products(
    productID   NUMBER(2)   GENERATED BY DEFAULT AS IDENTITY CONSTRAINT products_pk PRIMARY KEY,
    name        VARCHAR2(20) NOT NULL,
    category    VARCHAR2(20) NOT NULL
);

CREATE TABLE Warehouses_Products(
    warehouseID     NUMBER(2)   REFERENCES Warehouses(warehouseID) ON DELETE CASCADE NOT NULL,
    productID       NUMBER(2)   REFERENCES Products(productID) ON DELETE CASCADE NOT NULL,
    quantity        NUMBER(6)   NOT NULL
);

CREATE TABLE Stores_Products(
    storeID         NUMBER(2)   REFERENCES Stores(storeID) ON DELETE CASCADE NOT NULL,
    productID       NUMBER(2)   REFERENCES Products(productID) ON DELETE CASCADE NOT NULL,
    price           NUMBER(8,2) NOT NULL
);

CREATE TABLE Customers(
    customerID       NUMBER(2)   GENERATED BY DEFAULT AS IDENTITY CONSTRAINT customers_pk PRIMARY KEY,
    firstName        VARCHAR2(40) NOT NULL,
    lastName         VARCHAR2(20) NOT NULL,
    email            VARCHAR2(30) NOT NULL,
    streetAddress    VARCHAR2(40),
    city             VARCHAR2(40),
    province         VARCHAR2(40),
    country          VARCHAR2(40)
);

CREATE TABLE Employees(
    username    VARCHAR2(20) UNIQUE NOT NULL,
    password    VARCHAR2(60) NOT NULL
);

CREATE TABLE Orders(
    orderID         NUMBER(2)   GENERATED BY DEFAULT AS IDENTITY CONSTRAINT orders_pk PRIMARY KEY,
    customerID      NUMBER(2)   REFERENCES Customers(customerID) ON DELETE CASCADE NOT NULL,
    storeID         NUMBER(2)   REFERENCES Stores(storeID) ON DELETE CASCADE NOT NULL,
    orderDate       DATE
);

CREATE TABLE Orders_Products(
    orderID         NUMBER(2)   REFERENCES Orders(orderID) ON DELETE CASCADE NOT NULL,
    productID       NUMBER(2)   REFERENCES Products(productID) ON DELETE CASCADE NOT NULL,
    quantity        NUMBER(6)   NOT NULL
);

CREATE TABLE Reviews(
    reviewID        NUMBER(2)   GENERATED BY DEFAULT AS IDENTITY CONSTRAINT reviews_pk PRIMARY KEY,
    productID       NUMBER(2)   REFERENCES Products(productID) ON DELETE CASCADE NOT NULL,
	customerID  	NUMBER(2) 	REFERENCES Customers(customerID) ON DELETE CASCADE NOT NULL,
    star            NUMBER(1),
    flagNums        NUMBER(1),
    description     VARCHAR2(100)
);

/** inserts **/
INSERT INTO Stores (storeID, name) 
VALUES (1, 'marche adonis');
INSERT INTO Stores (storeID, name) 
VALUES (2, 'marche atwater');
INSERT INTO Stores (storeID, name) 
VALUES (3, 'dawson store');
INSERT INTO Stores (storeID, name) 
VALUES (4, 'store magic');
INSERT INTO Stores (storeID, name) 
VALUES (5, 'movie store');
INSERT INTO Stores (storeID, name) 
VALUES (6, 'super rue champlain');
INSERT INTO Stores (storeID, name) 
VALUES (7, 'toy r us');
INSERT INTO Stores (storeID, name) 
VALUES (8, 'Dealer one');
INSERT INTO Stores (storeID, name) 
VALUES (9, 'dealer montreal');
INSERT INTO Stores (storeID, name) 
VALUES (10, 'movie start');
INSERT INTO Stores (storeID, name) 
VALUES (11, 'star store');

INSERT INTO Warehouses (warehouseID, name, streetAddress, city, province, country) 
VALUES (1, 'Warehouse A', '100 rue William', 'Saint Laurent', 'Quebec', 'Canada');
INSERT INTO Warehouses (warehouseID, name, streetAddress, city, province, country) 
VALUES (2, 'Warehouse B', '304 Rue Francois-Perrault', 'Villeray Saint-Michel', 'Quebec', 'Canada');
INSERT INTO Warehouses (warehouseID, name, streetAddress, city, province, country) 
VALUES (3, 'Warehouse C', '86700 Weston Rd', 'Toronto', 'Ontario', 'Canada');
INSERT INTO Warehouses (warehouseID, name, streetAddress, city, province, country) 
VALUES (4, 'Warehouse D', '170 Sideroad', 'Quebec City', 'Quebec', 'Canada');
INSERT INTO Warehouses (warehouseID, name, streetAddress, city, province, country) 
VALUES (5, 'Warehouse E', '1231 Trudea road', 'Ottawa', 'Ontario', 'Canada');
INSERT INTO Warehouses (warehouseID, name, streetAddress, city, province, country) 
VALUES (6, 'Warehouse F', '16 Whitlock Rd', NULL, 'Alberta', 'Canada');

/*warehouses_stores???*/

INSERT INTO Products (name, category, productID) VALUES ('laptop ASUS 104S', 'electronics', 1);
INSERT INTO Products (name, category, productID) VALUES ('apple', 'Grocery', 2);
INSERT INTO Products (name, category, productID) VALUES ('SIMS CD', 'Video Games', 3);
INSERT INTO Products (name, category, productID) VALUES ('orange', 'grocery', 4);
INSERT INTO Products (name, category, productID) VALUES ('Barbie Movie', 'DVD', 5);
INSERT INTO Products (name, category, productID) VALUES ('L''Oreal Normal Hair', 'Health', 6);
INSERT INTO Products (name, category, productID) VALUES ('BMW iX Lego', 'Toys', 7);
INSERT INTO Products (name, category, productID) VALUES ('BMW i6', 'Cars', 8);
INSERT INTO Products (name, category, productID) VALUES ('Truck 500c', 'Vehicle', 9);
INSERT INTO Products (name, category, productID) VALUES ('paper towel', 'Beauty', 10);
INSERT INTO Products (name, category, productID) VALUES ('plum', 'grocery', 11);
INSERT INTO Products (name, category, productID) VALUES ('Lamborghini Lego', 'Toys', 12);
INSERT INTO Products (name, category, productID) VALUES ('chicken', 'grocery', 13);
INSERT INTO Products (name, category, productID) VALUES ('PS5', 'electronics', 14);
INSERT INTO Products (name, category, productID) VALUES ('pasta', 'Grocery', 15);
INSERT INTO Products (name, category, productID) VALUES ('tomato', 'Grocery', 16);
INSERT INTO Products (name, category, productID) VALUES ('Train X745', 'Vehicle', 17);

INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (1, 1, 1000);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (2, 2, 24980);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (3, 3, 103);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (4, 4, 35405);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (5, 5, 40);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (6, 6, 450);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (1, 7, 10);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (1, 8, 6);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (5, 9, 1000);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (6, 10, 3532);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (3, 11, 43242);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (2, 10, 39484);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (4, 11, 6579);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (5, 12, 98765);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (6, 13, 43523);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (1, 15, 2132);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (4, 14, 123);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (1, 16, 352222);
INSERT INTO Warehouses_Products (warehouseID, productID, quantity) VALUES (5, 17, 4543);

INSERT INTO Stores_Products (storeID, productID, price) VALUES (1, 1, 970);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (1, 13, 9.5);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (2, 2, 10);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (3, 3, 50);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (4, 4, 2);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (5, 5, 30);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (6, 6, 10);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (7, 7, 40);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (8, 8, 50000);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (9, 9, 856600);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (10, 10, 50);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (2, 11, 10);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (6, 6, 30);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (7, 12, 80);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (5, 3, 16);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (7, 5, 45);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (1, 13, 9.5);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (2, 15, 13.5);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (11, 14, 200);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (7, 7, 38);
INSERT INTO Stores_Products (storeID, productID, price) VALUES (4, 15, 15);

INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (1, 'mahsa', 'sadeghi', 'msadeghi@dawsoncollege.qc.ca', 'Dawson College', 'Montreal', 'Quebec', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (2, 'alex', 'brown', 'alex@gmail.com', '090 boul saint laurent', 'Montreal', 'Quebec', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (3, 'martin', 'alexandre', 'marting@yahoo.com', NULL, 'Brossard', 'Quebec', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (4, 'daneil', 'hanne', 'daneil@yahoo.com', '100 Atwater Street', 'Toronto', 'Ontario', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (5, 'martin', 'alexandre', 'marting@yahoo.com', NULL, 'Brossard', 'Quebec', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (6, 'John', 'boura', 'bdoura@gmail.com', '100 Young Street', 'Toronto', 'Ontario', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (7, 'Ari', 'brown', 'b.a@gmail.com', NULL, NULL, NULL, NULL);
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (8, 'Amanda', 'Harry', 'am.harry@yahoo.com', '100 boul saint laurent', 'Montreal', 'Quebec', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (9, 'Jack', 'Johnson', 'johnson.a@gmail.com', NULL, 'Calgary', 'Alberta', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (10, 'John', 'belle', 'abcd@yahoo.com', '105 Young Street', 'Toronto', 'Ontario', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (11, 'martin', 'Li', 'm.li@gmail.com', '87 boul saint laurent', 'Montreal', 'Quebec', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (12, 'olivia', 'smith', 'smith@hotmail.com', '76 boul decalthon', 'Laval', 'Quebec', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (13, 'Noah', 'Garcia', 'g.noah@yahoo.com', '22222 Happy Street', 'Laval', 'Quebec', 'Canada');
INSERT INTO Customers (customerID, firstName, lastName, email, streetAddress, city, province, country) 
VALUES (14, 'mahsa', 'sadeghi', 'ms@gmail.com', '104 Gill Street', 'Toronto', 'Ontario', 'Canada');

INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (1, 1, 1, TO_DATE('4/21/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (2, 2, 2, TO_DATE('10/23/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (3, 3, 3, TO_DATE('10/1/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (4, 4, 4, TO_DATE('10/23/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (2, 5, 5, TO_DATE('10/23/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (3, 6, 6, TO_DATE('10/10/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (1, 7, 7, TO_DATE('10/11/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (6, 8, 8, NULL);
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (7, 9, 9, NULL);
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (8, 10, 10, NULL);
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (9, 11, 2, TO_DATE('5/6/2020', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (3, 12, 6, TO_DATE('9/12/2019', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (1, 13, 7, TO_DATE('10/11/2010', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (1, 14, 2, TO_DATE('5/6/2022', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (14, 15, 7, TO_DATE('10/7/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (10, 16, 8, TO_DATE('8/10/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (2, 17, 5, TO_DATE('10/23/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (2, 18, 7, TO_DATE('10/2/2023', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (11, 19, 1, TO_DATE('4/3/2019', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (12, 20, 2, TO_DATE('12/29/2021', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (13, 21, 11, TO_DATE('1/20/2020', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (1, 22, 7, TO_DATE('10/11/2022', 'MM/DD/YYYY'));
INSERT INTO Orders (customerID, orderID, storeID, orderDate) 
VALUES (12, 23, 4, TO_DATE('12/29/2021', 'MM/DD/YYYY'));

INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (1, 1, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (2, 2, 2);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (3, 3, 3);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (4, 4, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (5, 5, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (6, 6, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (7, 7, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (8, 8, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (9, 9, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (10, 10, 3);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (11, 11, 6);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (12, 6, 3);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (13, 12, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (14, 11, 7);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (15, 12, 2);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (16, 8, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (17, 3, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (18, 5, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (19, 13, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (20, 15, 3);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (21, 14, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (22, 7, 1);
INSERT INTO Orders_Products (orderID, productID, quantity) VALUES (23, 15, 3);

INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (4, 0, 'It was affordable.', 1, 1, 1);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (3, 0, 'Quality was not good', 2, 2, 2);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (2, 1, NULL, 3, 3, 3);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (5, 0, 'Highly recommend', 4, 4, 4);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (1, 0, NULL, 5, 5, 2);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (1, 0, 'Did not worth the price', 6, 6, 3);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (1, 0, 'Missing some parts', 7, 7, 1);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (5, 1, 'Trash', 8, 8, 6);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (2, NULL, NULL, 9, 9, 7);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (5, NULL, NULL, 10, 10, 8);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (4, NULL, NULL, 11, 11, 9);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (3, NULL, NULL, 12, 6, 3);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (1, 0, 'Missing some parts', 13, 12, 1);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (4, NULL, NULL, 14, 11, 1);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (1, 0, 'Great product', 15, 12, 14);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (5, 1, 'Bad quality', 16, 8, 10);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (1, 0, NULL, 17, 3, 2);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (4, 0, NULL, 18, 5, 2);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (4, NULL, NULL, 19, 13, 11);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (5, NULL, NULL, 20, 15, 12);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (1, 2, 'Worse car I have driven!', 21, 7, 13);
INSERT INTO Reviews (star, flagNums, description, reviewID, productID, customerID)
VALUES (4, NULL, NULL, 22, 15, 12);

INSERT INTO Employees (username, password) VALUES ('john_doe', 'password123');
INSERT INTO Employees (username, password) VALUES ('jane_smith', 'securepass');
INSERT INTO Employees (username, password) VALUES ('mark_jones', 'employee123');
INSERT INTO Employees (username, password) VALUES ('sara_williams', 'pass456');
INSERT INTO Employees (username, password) VALUES ('robert_adams', '1234abcd');
INSERT INTO Employees (username, password) VALUES ('emily_white', 'p@ssw0rd');
INSERT INTO Employees (username, password) VALUES ('david_miller', 'employeepass');
INSERT INTO Employees (username, password) VALUES ('lisa_jackson', 'qwerty123');
INSERT INTO Employees (username, password) VALUES ('michael_clark', 'letmein');
INSERT INTO Employees (username, password) VALUES ('amber_lee', 'employee789');


/**BIANCA**/

/* this function taks a productid as input and will calculate the total inventory for that product across all tables */
CREATE OR REPLACE FUNCTION totalInventory(productIDsearch Products.productID%TYPE)
RETURN NUMBER
AS
    totalnum NUMBER(10);
BEGIN
    SELECT
        SUM(quantity) INTO totalNum
    FROM
        Warehouses_Products
    WHERE
        productId = productIDsearch;
    RETURN(totalNum);
END;
/

CREATE OR REPLACE PACKAGE calculations AS
    FUNCTION flaggedCustomers RETURN cus_names;
END calculations;
/

CREATE OR REPLACE PACKAGE BODY calculations AS
    TYPE cus_names IS TABLE OF VARCHAR(100)
    INDEX BY PLS_INTEGER;
    
    FUNCTION flaggedCustomers
    RETURN cus_names
    AS 
        custs cus_names;
    BEGIN
        DBMS_OUTPUT.PUT_LINE('Before SELECT');
        SELECT
            firstName || ' ' || lastName BULK COLLECT INTO custs
        FROM
            Customers INNER JOIN Reviews
            USING(customerID)
        WHERE
            flagNums IS NOT NULL AND
            flagNums > 0;
        RETURN(custs);
        
    END;
END calculations;

/
DECLARE
    customers calculations.cus_names;
    id PLS_INTEGER;
BEGIN
    customers := calculations.flaggedCustomers();
    FOR id IN 1 .. customers.count LOOP
        dbms_output.put_line(customers(id));
    END LOOP;
END;
/







/****/

/**AMY**/




/****/