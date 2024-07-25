-- tbl_code 1분류 초기화
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'mbti', 0, now());
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'category', 0, now());
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'paper', 0, now());
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'question', 0, now());
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'package', 0, now());

-- tbl_code 2분류 초기화
-- mbti
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ESTJ', 1, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ESTP', 2, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ESFJ', 3, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ESFP', 4, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ENTJ', 5, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ENTP', 6, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ENFJ', 7, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ENFP', 8, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ISTJ', 9, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ISTP', 10, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ISFJ', 11, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'ISFP', 12, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'INTJ', 13, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'INTP', 14, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'INFJ', 15, now() from tbl_code c where c.code_name = 'mbti';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'INFP', 16, now() from tbl_code c where c.code_name = 'mbti';
-- category
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '가족', 1, now() from tbl_code c where c.code_name = 'category';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '친구', 2, now() from tbl_code c where c.code_name = 'category';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '연인', 3, now() from tbl_code c where c.code_name = 'category';
-- paper
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '무료', 1, now() from tbl_code c where c.code_name = 'paper';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '유료', 2, now() from tbl_code c where c.code_name = 'paper';
-- question
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'comTarget', 1, now() from tbl_code c where c.code_name = 'question';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'comAll', 2, now() from tbl_code c where c.code_name = 'question';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'addTarget', 3, now() from tbl_code c where c.code_name = 'question';
-- package
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '확인', 1, now() from tbl_code c where c.code_name = 'package';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '미확인', 2, now() from tbl_code c where c.code_name = 'package';


-- admin user
insert into tbl_user (code_mbti, create_date, user_birth, user_id, user_img, user_name, user_phone, user_pwd)
    values ( 6, now(), '20000123', 'admin', 'imgfile.jpg', '관리자', '01011111111', 'admin1234');