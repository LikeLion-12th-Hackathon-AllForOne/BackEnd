-- tbl_code 1분류 초기화
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'mbti', 0, now());
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'category', 0, now());
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'paper', 0, now());
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'questionType', 0, now());
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'questionClass', 0, now());
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
-- questionType
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'comAll', 1, now() from tbl_code c where c.code_name = 'questionType';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'comTarget', 2, now() from tbl_code c where c.code_name = 'questionType';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'addTarget', 3, now() from tbl_code c where c.code_name = 'questionType';
-- questionClass
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'family', (select c.code_seq from tbl_code c where c.code_unit = 2 and c.code_name = '가족') as code_val, now() from tbl_code c where c.code_name = 'questionClass';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'parents', (select c.code_seq from tbl_code c where c.code_unit = 2 and c.code_name = '가족') as code_val, now() from tbl_code c where c.code_name = 'questionClass';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'child', (select c.code_seq from tbl_code c where c.code_unit = 2 and c.code_name = '가족') as code_val, now() from tbl_code c where c.code_name = 'questionClass';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'friend', (select c.code_seq from tbl_code c where c.code_unit = 2 and c.code_name = '친구') as code_val, now() from tbl_code c where c.code_name = 'questionClass';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, 'couple', (select c.code_seq from tbl_code c where c.code_unit = 2 and c.code_name = '연인') as code_val, now() from tbl_code c where c.code_name = 'questionClass';
-- package
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '확인', 1, now() from tbl_code c where c.code_name = 'package';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '미확인', 2, now() from tbl_code c where c.code_name = 'package';

-- tbl_code 3분류 초기화
-- category
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 3, '아빠', 1, now() from tbl_code c where c.code_unit = 2 and c.code_name = '가족';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 3, '엄마', 2, now() from tbl_code c where c.code_unit = 2 and c.code_name = '가족';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 3, '자녀', 3, now() from tbl_code c where c.code_unit = 2 and c.code_name = '가족';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 3, '친구', 1, now() from tbl_code c where c.code_unit = 2 and c.code_name = '친구';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 3, '연인', 1, now() from tbl_code c where c.code_unit = 2 and c.code_name = '연인';

-- 추가 - questionState
insert into tbl_code (code_unit, code_name, code_val, create_date) values (1, 'questionState', 0, now());
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '오늘의 문제가 출제되었습니다.', 1, now() from tbl_code c where c.code_name = 'questionState';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '모든 문제가 출제되어 출제 가능한 문제가 없습니다.', 2, now() from tbl_code c where c.code_name = 'questionState';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '인원수가 채워지지 않아 문제를 출제할 수 없습니다.', 3, now() from tbl_code c where c.code_name = 'questionState';
insert into tbl_code (code_parent_seq, code_unit, code_name, code_val, create_date) select c.code_seq, 2, '문제출제를 기다리는 중입니다.', 4, now() from tbl_code c where c.code_name = 'questionState';


-- test data zip
-- user
insert into tbl_user (code_mbti, create_date, user_birth, user_id, user_img, user_name, user_phone, user_pwd)
    values ( 6, now(), '20000123', 'admin', 'imgfile.jpg', '관리자', '01011111111', 'admin1234');
insert into tbl_user ( code_mbti, create_date, user_birth, user_id, user_img, user_name, user_phone, user_pwd)
    values ( 7, now(), '20000124', 'family1', 'imgfile.jpg', '부모', '01022222222', 'family1');
insert into tbl_user ( code_mbti, create_date, user_birth, user_id, user_img, user_name, user_phone, user_pwd)
    values ( 7, now(), '20000125', 'family2', 'imgfile.jpg', '자식', '01033333333', 'family2');
insert into tbl_user (code_mbti, create_date, user_birth, user_id, user_img, user_name, user_phone, user_pwd)
    values ( 8, now(), '20000126', 'friend1', 'imgfile.jpg', '친구1', '01044444444', 'friend1');
insert into tbl_user (code_mbti, create_date, user_birth, user_id, user_img, user_name, user_phone, user_pwd)
    values ( 8, now(), '20000127', 'friend2', 'imgfile.jpg', '친구2', '01055555555', 'friend2');
insert into tbl_user (code_mbti, create_date, user_birth, user_id, user_img, user_name, user_phone, user_pwd)
    values ( 8, now(), '20000128', 'friend3', 'imgfile.jpg', '친구3', '01066666666', 'friend3');
insert into tbl_user ( code_mbti, create_date, user_birth, user_id, user_img, user_name, user_phone, user_pwd)
    values ( 9, now(), '20000129', 'couple', 'imgfile.jpg', '애인', '01077777777', 'couple');




