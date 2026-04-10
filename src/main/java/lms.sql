DROP DATABASE IF EXISTS lms;

CREATE DATABASE lms;

USE lms;

-- 1. Create Publishers
CREATE TABLE Publisher (
    publisherID 	INT 		PRIMARY KEY 	AUTO_INCREMENT,
    publisher_name 	VARCHAR(30) NOT NULL
);

INSERT INTO Publisher(publisher_name) VALUES ('Kadokawa'),
                                             ('Algonquin'),
                                             ('Louis Express'),
                                             ('TongLi'),
                                             ('Collins'),
                                             ('Canbridge');

-- 2. Create Authors
CREATE TABLE Author (
    authorID 	INT 		PRIMARY KEY 	AUTO_INCREMENT,
    first_name 	VARCHAR(15) NOT NULL,
    last_name 	VARCHAR(15) NOT NULL
);

INSERT INTO Author(first_name, last_name) VALUES ('Simon', 'Lu'),
                                                 ('Lee', 'Cony'),
                                                 ('Alyssa', 'Matelas'),
                                                 ('Yoji', 'Sakamura'),
                                                 ('Dale', 'White'),
                                                 ('Giacomo', 'Stewart'),
                                                 ('Hunter', 'Faulkner'),
                                                 ('Zephr', 'Green'),
                                                 ('Nayda', 'Walker'),
                                                 ('Winifred', 'Howard'),
                                                 ('Nicolas', 'Holland');

-- 3. Create Users
CREATE TABLE User (
    username 	VARCHAR(20) PRIMARY KEY,
    password 	VARCHAR(255) NOT NULL,
    first_name 	VARCHAR(15) NOT NULL,
    last_name 	VARCHAR(15) NOT NULL,
    email 		VARCHAR(50) NOT NULL,
    phone 		VARCHAR(15) NOT NULL,
    is_admin	TINYINT 	NOT NULL DEFAULT 0
);

INSERT INTO User(username, password, first_name, last_name, phone, email) VALUES ('NQJ67dMN2jb', 'NQJ67dMN2jb', 'Cade', 'Kinney', '(171)779-8402', 'cade@email.com'),
																				('LWT29krn7SH', 'LWT29krn7SH', 'Astra', 'Boyer', '(634)298-7276', 'astra@email.com'),
																				('XEE06YPV8JF', 'XEE06YPV8JF', 'Quemby', 'Noel', '(621)330-3338', 'qnoel@protonmail.couk'),
																				('HTQ48WFD7DG', 'HTQ48WFD7DG', 'Drake', 'Marshall', '(561)668-7335', 'drake.mar@outlook.net'),
																				('QKJ56GRF5MT', 'QKJ56GRF5MT', 'Vladimir', 'Hanson', '(875)603-5512', 'hanson33@google.net'),
																				('DEU52NRD8SC', 'DEU52NRD8SC', 'Elaine', 'Vasquez', '(766)374-7865', 'ela12@protonmail.net'),
																				('WNY88FWK4IR', 'WNY88FWK4IR', 'Ira', 'Lindsey', '(868)521-3610', 'ira@aol.net'),
																				('VDS15IHH4BS', 'VDS15IHH4BS', 'Philip', 'Dawson', '(329)924-2625', 'philip45@aol.net'),
																				('OKI09RBD4VU', 'OKI09RBD4VU', 'Ivana', 'Fletcher', '(217)253-5670', 'ivana@yahoo.com'),
																				('HUQ48BCE8LF', 'HUQ48BCE8LF', 'Burton', 'Holt', '(886)462-4383', 'holtzz@protonmail.couk'),
																				('EOG28PRB4MJ', 'EOG28PRB4MJ', 'Kimberley', 'Blackburn', '(735)221-5362', 'kimblack7@hotmail.ca'),
																				('HYF64OHM4IB', 'HYF64OHM4IB', 'Luke', 'Deleon', '(115)343-1539', 'luked67@outlook.com'),
																				('MJT11QNV4PY', 'MJT11QNV4PY', 'Quinn', 'Sutton', '(527)912-8534', 'quinn@nmail.edu'),
																				('XCD16BGH1GJ', 'XCD16BGH1GJ', 'Coby', 'Frederick', '(685)647-1857', 'coby06@hotmail.com'),
																				('MMH32JTY5UA', 'MMH32JTY5UA', 'Geoffrey', 'Ramirez', '(227)453-2951', 'geooo@google.com'),
																				('RKH38NJL5MM', 'RKH38NJL5MM', 'Gretchen', 'Figueroa', '(461)563-2813', 'fornare@google.ca'),
																				('TIR27QDX6SD', 'TIR27QDX6SD', 'Celeste', 'Calderon', '(719)756-9566', 'celcal@outlook.ca'),
                                                                                ('test', '12345678', 'Test', 'Test', '(123)123-4567', 'test@123.com');

INSERT INTO User(username, password, first_name, last_name, phone, email, is_admin)
VALUES ('admin01', 'admin123', 'System', 'Admin', '0000000000', 'admin@lms.com', 1);

-- 4. Create Book Catalog (linking authors and publishers)
CREATE TABLE Book (
    ISBN 					VARCHAR(15) PRIMARY KEY,
    title 					VARCHAR(100) NOT NULL,
    date_acquired 			DATE 		NOT NULL,
    description 			VARCHAR(500),
    Author_authorID 		INT 		NOT NULL,
    Publisher_publisherID 	INT 		NOT NULL,
    FOREIGN KEY (Author_authorID) REFERENCES Author(authorID),
    FOREIGN KEY (Publisher_publisherID) REFERENCES Publisher(publisherID)
);

INSERT INTO Book (ISBN, title, date_acquired, description, Author_authorID, Publisher_publisherID) VALUES
-- Java and Programming Classics
('9780134685991', 'Effective Java', '2014-01-10', 'A must-read bible for Java developers, introducing best practices.', 1, 4),
('9780132350884', 'Clean Code', '2003-01-15', 'Teaches you how to write clean, maintainable code.', 2, 2),
('9780596009205', 'Head First Java', '2022-01-20', 'Rich in illustrations, the most suitable Java introductory book for beginners.', 6, 6),
('9781617294945', 'Spring in Action', '2024-02-01', 'An in-depth practical guide to Spring Framework 5.', 5, 5),
('9780201633610', 'Design Patterns', '2021-12-05', 'Classic object-oriented design patterns by the Gang of Four (GoF).', 8, 4),

-- Fantasy and Fiction
('9780747532743', 'Harry Potter and the Philosopher''s Stone', '2011-11-10', 'Harry Potter series book 1: The Philosopher''s Stone.', 3, 3),
('9780747538493', 'Harry Potter and the Chamber of Secrets', '2013-11-12', 'Harry Potter series book 2: The Chamber of Secrets.', 3, 3),
('9780007525546', 'The Hobbit', '2008-10-30', 'The Lord of the Rings prequel, the adventure story of Bilbo Baggins.', 4, 1),
('9780261102385', 'The Lord of the Rings', '2015-12-25', 'The Lord of the Rings trilogy, an epic fantasy journey.', 4, 1),

-- Others / Miscellaneous
('9780137081073', 'The Clean Coder', '2002-02-02', 'The survival guide and professional ethics for software developers.', 10, 6);

-- 5. Create Physical Book Copies (linking to book catalog)
CREATE TABLE Book_Info (
    bookID 			INT 		PRIMARY KEY AUTO_INCREMENT,
    book_condition 	VARCHAR(50) NOT NULL,
    status 			TINYINT 	NOT NULL,
    Book_ISBN 		VARCHAR(15) NOT NULL,
    FOREIGN KEY (Book_ISBN) REFERENCES Book(ISBN)
);

INSERT INTO Book_Info (bookID, book_condition, status, Book_ISBN) VALUES
-- status: 1(Available), 0(Borrowed), 2(Maintenance)
(1001, 'Good', 1, '9780134685991'),       -- Available
(1002, 'Worn', 0, '9780134685991'),       -- [Borrowed]
(1003, 'New', 1, '9780132350884'),        -- Available
(1004, 'Good', 1, '9780596009205'),       -- Available
(1005, 'Damaged', 2, '9780747532743'),    -- Under maintenance
(1006, 'Good', 0, '9780747532743'),       -- [Borrowed]
(1007, 'New', 1, '9780747532743'),        -- Available
(1008, 'Good', 1, '9780747538493'),       -- Available
(1009, 'Good', 0, '9780747538493'),       -- [Borrowed]
(1010, 'Worn', 1, '9780201633610'),       -- Available
(1011, 'New', 1, '9780007525546'),        -- Available
(1012, 'Good', 0, '9780261102385'),       -- [Borrowed]
(1013, 'New', 1, '9780261102385'),        -- Available
(1014, 'New', 0, '9781617294945'),        -- [Borrowed]
(1015, 'Good', 1, '9780137081073');       -- Available

-- 6. Create Borrowing Records (linking users and physical copies)
CREATE TABLE Book_User (
    book_userID 		INT		 	PRIMARY KEY 	AUTO_INCREMENT,
    start_date 			DATE 		NOT NULL,
    return_date 		DATE,
    late_fee 			DECIMAL(8,2),
    User_username 		VARCHAR(20) NOT NULL,
    Book_Info_bookID 	INT 		NOT NULL,
    FOREIGN KEY (User_username) REFERENCES User(username),
    FOREIGN KEY (Book_Info_bookID) REFERENCES Book_Info(bookID)
);

INSERT INTO Book_User(start_date, return_date, late_fee, User_username, Book_Info_bookID) VALUES -- [Historical Records (Returned)] (Corresponding Book_Info must be 1 or 2, return_date is not null)
                                                                                                 ('2023-04-23', '2023-06-30', 10.50, 'LWT29krn7SH', 1001),
                                                                                                 ('2022-10-12', '2023-02-28', 30.00, 'HTQ48WFD7DG', 1003),
                                                                                                 ('2024-07-16', '2024-08-18', 7.00, 'WNY88FWK4IR', 1004),
                                                                                                 ('2025-01-05', '2025-01-13', 0.00, 'DEU52NRD8SC', 1015),
                                                                                                 ('2019-02-23', '2019-03-03', 0.00, 'XEE06YPV8JF', 1008),

                                                                                                 -- [Currently Borrowed (Not Returned)] (Corresponding Book_Info must be 0, return_date must be null)
                                                                                                 ('2026-04-02', null, 0.00, 'NQJ67dMN2jb', 1002), -- Recently borrowed (not overdue)
                                                                                                 ('2026-02-15', null, 0.00, 'LWT29krn7SH', 1006), -- Borrowed for a long time (overdue)
                                                                                                 ('2026-04-01', null, 0.00, 'XEE06YPV8JF', 1009), -- Recently borrowed (not overdue)
                                                                                                 ('2026-03-05', null, 0.00, 'HTQ48WFD7DG', 1012), -- Borrowed for over 14 days (overdue)
                                                                                                 ('2026-04-05', null, 0.00, 'QKJ56GRF5MT', 1014); -- Recently borrowed (not overdue)

SELECT DISTINCT
    Book.ISBN AS 'ISBN',
    Book.title AS 'Book name',
    Author.first_name AS 'Author first name',
    Author.last_name AS 'Author last name',
    User.username AS 'Borrower username',
    Book_Info.status AS 'Book status',
    Book_User.start_date AS 'Book borrowed date',
    IFNULL(return_date, '') AS 'Book return date',
    Book_User.late_fee AS 'Late fee'
FROM
    Book
        JOIN
    Book_Info ON (Book.ISBN = Book_Info.Book_ISBN)
        JOIN
    Book_User ON (Book_Info.bookID = Book_User.Book_Info_bookID)
        JOIN
    User ON (Book_User.User_username = User.username)
        JOIN
    Author ON (Book.Author_authorID = Author.authorID)
        JOIN
    Publisher ON (Book.Publisher_publisherID = Publisher.publisherID)
    order by BOOK.ISBN DESC;
