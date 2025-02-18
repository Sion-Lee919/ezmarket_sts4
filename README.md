# ezmarket_sts4

## 리액트<br>
npm install react-router-dom@7.1.5<br>
npm install react-bootstrap<br>
npm install js-cookie<br>

## DB
create table member ( --회원 테이블<br>
    member_id number primary key, -- 맴버id pk<br>
    username varchar2(50) not null, -- 로그인 id (50글자 이하)<br>
    realname varchar2(30) not null, -- 실명 (10글자 이하)<br>
    nickname varchar2(36) not null, -- 닉네임 (12글자 이하)<br>
    password varchar2(100) not null, -- 비밀번호 저장(시큐어코딩할지선택)<br>
    email varchar2(100) not null, -- 이메일<br>
    phone varchar2(13) not null, -- 폰번호 (010-xxxx-xxxx 형식)<br>
    address varchar2(1000) , -- 주소<br>
    join_date date default sysdate, -- 가입일<br>
    update_date date default sysdate, -- 수정일<br>
    userauthor number(1) default 1 not null check(userauthor in (0, 1, 2)) , -- 권한 (0: 관리자, 1: 일반회원, 2: 판매자) 기본값 1 일반회원<br>
    points number default 0 CHECK (points >= 0), -- 포인트<br>
    ezpay number(10) default 0 CHECK (ezpay >= 0), --충전금액<br>
    member_status varchar2(20) default 'normal' check(member_status in ('normal', 'kick')), --맴버 상태 (일반,강퇴)<br>
    member_kick_comment varchar2(4000), -- 강퇴당한 맴버 사유<br>
    social number(1) default 0 not null,<br>
    <br>
    -- 중복 방지 제약 조건<br>
    constraint unq_username unique (username), -- 로그인 id 중복 방지<br>
    constraint unq_email unique (email), -- 이메일 중복 방지 /<br>
    constraint unq_nickname unique (nickname), -- 닉네임 중복 방지<br>
    /* constraint unq_phone unique (phone), -- 전화번호 중복 방지 */<br>
    <br>
    -- 가입 제약 조건<br>
    constraint chk_username_length check (length(username) <= 50), -- 로그인 id 50글자 이하<br>
    constraint chk_realname_length check (length(realname) <= 30), -- 실명 10글자 이하<br>
    constraint chk_nickname_length check (length(nickname) <= 36), -- 닉네임 12글자 이하<br>
    constraint chk_email_format check (regexp_like(email, '^[a-z0-9._%+-]+@[a-z0-9.-]+\.(com|kr|net)$')), -- 이메일 형식<br>
    constraint chk_phone_format check (regexp_like(phone, '^010-\d{4}-\d{4}$')) -- 폰번호 형식<br>
);<br>
<br>
create table brand(--판매자 정보<br>
    brand_id number primary key, --판매자 id pk<br>
    member_id number, --회원 id<br>
    brandname varchar2(100) not null, --브랜드명<br>
    brandlogo_url VARCHAR2(500), --브랜드로고<br>
    brand_number varchar2(100) not null, --사업자 등록번호<br>
    brandlicense_url VARCHAR2(500), --사업자 등록증 파일 이미지<br>
    brand_status varchar2(20) default '신청 중' check(brand_status in ('신청 중', '검토 중','거절')),--판매자신청상태<br>
    brand_refusal_comment varchar2(4000), --거절 사유<br>
    brand_join_date date default sysdate, -- 판매자신청일<br>
    brand_update_date date default sysdate, -- 판매자정보수정일<br>
    constraint unq_brand_number unique (brand_number)
    constraint fk_brand_member foreign key (member_id) references member(member_id)<br>
);<br>
<br>
create table product ( --상품 테이블<br>
    product_id number primary key, -- 상품id pk<br>
    seller_id number not null, -- 판매자 id<br>
    name varchar2(100) not null, -- 상품명<br>
    description varchar2(4000), -- 상품 설명<br>
    price number(10) not null, -- 상품 가격<br>
    stock_quantity number default 0, -- 재고 수량<br>
    created_at date default sysdate, -- 등록일<br>
    update_date date default sysdate, -- 수정일<br>
    image_url varchar2(500), -- 상품 이미지 url<br>
    constraint fk_product_seller foreign key (seller_id) references member(member_id)<br>
);<br>
<br>
create table category ( --카테고리 테이블<br>
    category_id number primary key, -- 카테고리 id pk<br>
    name varchar2(50) not null -- 카테고리이름<br>
);<br>
<br>
create table product_category ( -- 카테고리-상품 연결 테이블<br>
    product_id number not null, -- 상품 id<br>
    category_id number not null, -- 카테고리 id<br>
    primary key (product_id, category_id),<br>
    constraint fk_product_category_product foreign key (product_id) references product(product_id),<br>
    constraint fk_product_category_category foreign key (category_id) references category(category_id),<br>
    quantity number not null<br>
);<br>
<br>
create table orders ( --주문서 테이블<br>
    order_id number primary key, -- 주문 id pk<br>
    member_id number not null, -- 회원 id<br>
    product_id number not null, -- 상품 id<br>
    order_date date default sysdate, -- 주문일<br>
    quantity number not null, -- 주문 수량<br>
    total_amount number(10) not null, -- 총 주문 금액<br>
    status varchar2(20) default '처리 중', -- 주문 상태<br>
    shipping_address varchar2(500) not null, -- 배송 주소<br>
    constraint fk_order_member foreign key (member_id) references member(member_id),<br>
    constraint fk_order_product foreign key (product_id) references product(product_id)<br>
);<br>
<br>
create table review ( --후기 테이블<br>
    review_id number primary key, -- 후기 id pk<br>
    product_id number not null, -- 상품 id<br>
    member_id number not null, -- 회원 id<br>
    comments varchar2(1000) not null, -- 후기 내용<br>
    review_date date default sysdate, -- 후기 작성일<br>
    update_date date default sysdate, -- 후기 수정일<br>
    image_url varchar2(500), -- 후기 이미지 url (선택사항)<br>
    constraint fk_review_product foreign key (product_id) references product(product_id),<br>
    constraint fk_review_member foreign key (member_id) references member(member_id)<br>
);<br>
<br>
create table qna ( -- 문의 테이블<br>
    qna_id number primary key, -- 문의 id pk<br>
    member_id number not null, -- 회원 id<br>
    subject varchar2(100) not null, -- 문의 제목<br>
    content varchar2(4000) not null, -- 문의 내용<br>
    created_at date default sysdate, -- 작성일<br>
    update_date date default sysdate, -- 문의 수정일<br>
    response varchar2(4000), -- 관리자 답변<br>
    response_date date default sysdate, -- 답변일<br>
    notifications varchar2(4000), --관리자 공지사항<br>
    constraint fk_qna_member foreign key (member_id) references member(member_id)<br>
);<br>
<br>
create table cart( -- 장바구니 테이블<br>
    cart_id number primary key, -- 장바구니 id pk<br>
    member_id number not null, -- 회원 id<br>
    product_id number not null, -- 상품 id<br>
    constraint fk_cart_member foreign key (member_id) references member(member_id),<br>
    constraint fk_cart_product foreign key (product_id) references product(product_id)<br>
);<br>
<br>
