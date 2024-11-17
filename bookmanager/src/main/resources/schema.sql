CREATE TABLE IF NOT EXISTS book(
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '書籍ID'
    , title VARCHAR (255) NOT NULL COMMENT 'タイトル'
    , price int NOT NULL COMMENT '価格'
    , publication_status ENUM('NOT_PUBLISHED', 'PUBLISHED') NOT NULL COMMENT '出版状況'
) COMMENT = '書籍テーブル';

CREATE TABLE IF NOT EXISTS author(
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '著者ID'
    , name VARCHAR (255) NOT NULL COMMENT '著者名'
    , birth_date DATE NOT NULL COMMENT '生年月日'
) COMMENT = '著者テーブル';

CREATE TABLE IF NOT EXISTS book_author (
    book_id INT NOT NULL COMMENT '書籍ID',
    author_id INT NOT NULL COMMENT '著者ID',
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE
) COMMENT = '書籍・著者関連テーブル';