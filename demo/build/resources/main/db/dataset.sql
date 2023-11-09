--SET REFERENTIAL_INTEGRITY FALSE;
--truncate table user_tb;
--SET REFERENTIAL_INTEGRITY TRUE;
--
--
--INSERT INTO user_tb (`id`,`email`,`password`,`userName`, `roles`) VALUES ('1', 'admin@green.com', '{bcrypt}$2a$10$IkVCSNVb.j.63fF19eISZe1mSvMUxs6KRg/ltThRhLexgJzk1CZMO', '홍길동', 'ROLE_ADMIN');
--INSERT INTO user_tb (`id`,`email`,`password`,`userName`, `roles`) VALUES ('2', 'user@green.com', '{bcrypt}$2a$10$IkVCSNVb.j.63fF19eISZe1mSvMUxs6KRg/ltThRhLexgJzk1CZMO', '임꺽정', 'ROLE_USER');
drop table kakao_table;
create table kakao_table (
	k_number bigint auto_increment,
    k_name varchar(20) not null,
    k_email varchar(50) not null,
    constraint primary key(k_number)
);
--
--SET REFERENTIAL_INTEGRITY FALSE;
--
---- 테이블명과 CASCADE 키워드를 이어서 작성
--DROP TABLE IF EXISTS user_tb;
--
--SET REFERENTIAL_INTEGRITY TRUE;
--CREATE TABLE user_tb (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    email VARCHAR(100) NOT NULL,
--    password VARCHAR(100) NOT NULL,
--    userName VARCHAR(45) NOT NULL,
--    roles VARCHAR(30) NOT NULL
--);
--
---- 초기 데이터 삽입
--INSERT INTO user_tb (email, password, userName, roles) VALUES
--('admin@green.com', '{bcrypt}$2a$10$IkVCSNVb.j.63fF19eISZe1mSvMUxs6KRg/ltThRhLexgJzk1CZMO', '홍길동', 'ROLE_ADMIN'),
--('user@green.com', '{bcrypt}$2a$10$IkVCSNVb.j.63fF19eISZe1mSvMUxs6KRg/ltThRhLexgJzk1CZMO', '임꺽정', 'ROLE_USER');