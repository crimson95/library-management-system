DROP DATABASE IF EXISTS lms;

CREATE DATABASE lms;

USE lms;

-- 1. 建立出版社
CREATE TABLE Publisher (
    publisherID 	INT 		PRIMARY KEY 	AUTO_INCREMENT,
    publisher_name 	VARCHAR(30) NOT NULL
);

INSERT INTO Publisher(publisher_name) VALUES ('Kadokawa'), ('Algonquin'), ('Louis Express'), ('TongLi'), ('Collins'), ('Canbridge');

-- 2. 建立作者
CREATE TABLE Author (
    authorID 	INT 		PRIMARY KEY 	AUTO_INCREMENT,
    first_name 	VARCHAR(15) NOT NULL,
    last_name 	VARCHAR(15) NOT NULL
);

INSERT INTO Author(first_name, last_name) VALUES ('Simon', 'Lu'), ('Lee', 'Cony'), ('Alyssa', 'Matelas'), ('Yoji', 'Sakamura'), ('Dale', 'White'), ('Giacomo', 'Stewart'),
												('Hunter', 'Faulkner'), ('Zephr', 'Green'), ('Nayda', 'Walker'), ('Winifred', 'Howard'), ('Nicolas', 'Holland');

-- 3. 建立使用者
CREATE TABLE User (
    username 	VARCHAR(20) PRIMARY KEY,
    password 	VARCHAR(20) NOT NULL,
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
																				('TIR27QDX6SD', 'TIR27QDX6SD', 'Celeste', 'Calderon', '(719)756-9566', 'celcal@outlook.ca');

INSERT INTO User(username, password, first_name, last_name, phone, email, is_admin)
VALUES ('admin01', 'admin123', 'System', 'Admin', '0000000000', 'admin@lms.com', 1);

-- 4. 建立書籍目錄 (連結作者與出版社)
CREATE TABLE Book (
    ISBN 					VARCHAR(15) PRIMARY KEY,
    title 					VARCHAR(50) NOT NULL,
    date_acquired 			DATE 		NOT NULL,
    description 			VARCHAR(500),
    Author_authorID 		INT 		NOT NULL,
    Publisher_publisherID 	INT 		NOT NULL,
    FOREIGN KEY (Author_authorID) REFERENCES Author(authorID),
    FOREIGN KEY (Publisher_publisherID) REFERENCES Publisher(publisherID)
);

INSERT INTO Book (ISBN, title, date_acquired, description, Author_authorID, Publisher_publisherID) VALUES 
-- Java 與程式開發經典
('9780134685991', 'Effective Java', '2014-01-10', 'Java 開發者的必讀聖經，介紹最佳實踐。', 1, 2),
('9780132350884', 'Clean Code', '2003-01-15', '教你如何寫出乾淨、可維護的程式碼。', 2, 2),
('9780596009205', 'Head First Java', '2022-01-20', '圖文並茂，最適合初學者的 Java 入門書。', 5, 1),
('9781617294945', 'Spring in Action', '2024-02-01', '深入淺出 Spring Framework 5 的實戰指南。', 5, 5),
('9780201633610', 'Design Patterns', '2021-12-05', '四人幫 (GoF) 的經典物件導向設計模式。', 1, 2),

-- 奇幻與小說類
('9780747532743', 'Harry Potter and the Philosopher''s Stone', '2011-11-10', '哈利波特系列第一集：神秘的魔法石。', 3, 3),
('9780747538493', 'Harry Potter and the Chamber of Secrets', '2013-11-12', '哈利波特系列第二集：消失的密室。', 3, 3),
('9780007525546', 'The Hobbit', '2008-10-30', '魔戒前傳，比爾博·巴金斯的冒險故事。', 4, 4),
('9780261102385', 'The Lord of the Rings', '2015-12-25', '魔戒三部曲全集，史詩般的奇幻旅程。', 4, 4),

-- 湊數用
('9780137081073', 'The Clean Coder', '2002-02-02', '專業程式設計師的生存之道與職業素養。', 2, 2);

-- 5. 建立實體書 (連結書籍目錄)
CREATE TABLE Book_Info (
    bookID 			INT 		PRIMARY KEY AUTO_INCREMENT,
    book_condition 	VARCHAR(50) NOT NULL,
    status 			TINYINT 	NOT NULL,
    Book_ISBN 		VARCHAR(15) NOT NULL,
    FOREIGN KEY (Book_ISBN) REFERENCES Book(ISBN)
);

INSERT INTO Book_Info (bookID, book_condition, status, Book_ISBN) VALUES 
-- 1. Effective Java
(1001, 'Good', 1, '9780134685991'),       -- 狀況良好，在庫
(1002, 'Worn', 0, '9780134685991'),       -- 有點舊了，被借走了

-- 2. Clean Code
(1003, 'New', 1, '9780132350884'),        -- 全新，在庫

-- 3. Head First Java
(1004, 'Good', 1, '9780596009205'),       -- 狀況良好，在庫

-- 4. Harry Potter 1
(1005, 'Damaged', 2, '9780747532743'),    -- 破損嚴重，送修中
(1006, 'Good', 0, '9780747532743'),       -- 狀況良好，被借走了
(1007, 'New', 1, '9780747532743'),        -- 剛補貨的，在庫

-- 5. Harry Potter 2
(1008, 'Good', 1, '9780747538493'),
(1009, 'Good', 0, '9780747538493'),

-- 6. Design Patterns
(1010, 'Worn', 1, '9780201633610'),       -- 翻書痕跡明顯，但在庫

-- 7. The Hobbit
(1011, 'New', 1, '9780007525546'),

-- 8. Lord of the Rings
(1012, 'Good', 0, '9780261102385'),
(1013, 'New', 1, '9780261102385'),

-- 9. Spring in Action
(1014, 'New', 0, '9781617294945'),

-- 10. The Clean Coder
(1015, 'Good', 1, '9780137081073');

-- 6. 建立借書紀錄 (連結使用者與實體書)
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

INSERT INTO Book_User(start_date, return_date, late_fee, User_username, Book_Info_bookID) VALUES ('2020-12-05', '2020-12-19', 0.00, 'NQJ67dMN2jb', 1006),
																								('2023-04-23', '2023-06-30', 10.50, 'LWT29krn7SH', 1001),
                                                                                                ('2019-02-23', '2019-03-03', 0.00, 'XEE06YPV8JF', 1008),
                                                                                                ('2022-10-12', '2023-02-28', 30.00, 'HTQ48WFD7DG', 1003),
                                                                                                ('2025-11-14', '2025-12-31', 8.50, 'QKJ56GRF5MT', 1012),
                                                                                                ('2025-01-05', '2025-01-13', 0.00, 'DEU52NRD8SC', 1015),
                                                                                                ('2024-07-16', '2024-08-18', 7.00, 'WNY88FWK4IR', 1004),
                                                                                                ('2026-01-31', null, 0.00, 'VDS15IHH4BS', 1010);

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
