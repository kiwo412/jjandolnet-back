
CREATE TABLE address
(
  id   bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
  name varchar(50) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

COMMENT ON TABLE address IS '거주지';

CREATE TABLE attachment
(
  id        bigint       NOT NULL GENERATED ALWAYS AS IDENTITY,
  file_name varchar(255) NOT NULL,
  file_path varchar(500) NOT NULL,
  file_size bigint       NOT NULL,
  post_id   bigint       NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE attachment IS '첨부파일';

CREATE TABLE comment
(
  id        bigint    NOT NULL GENERATED ALWAYS AS IDENTITY,
  content   text      NOT NULL,
  create_at timestamp NOT NULL,
  post_id   bigint    NOT NULL,
  user_id   bigint    NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE comment IS '댓글';

CREATE TABLE expense
(
  id           bigint    NOT NULL GENERATED ALWAYS AS IDENTITY,
  amount       bigint    NOT NULL,
  memo         text     ,
  expense_date date      NOT NULL,
  create_at    timestamp NOT NULL,
  user_id      bigint    NOT NULL,
  category_id  bigint    NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE expense IS '지출내역';

CREATE TABLE expense_category
(
  id    bigint       NOT NULL GENERATED ALWAYS AS IDENTITY,
  name  varchar(100) NOT NULL,
  icon  varchar(255),
  color varchar(10) ,
  PRIMARY KEY (id)
);

COMMENT ON TABLE expense_category IS '지출내역 카테고리';

CREATE TABLE job
(
  id   bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
  name varchar(50) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

COMMENT ON TABLE job IS '직업';

CREATE TABLE post
(
  id         bigint       NOT NULL GENERATED ALWAYS AS IDENTITY,
  category   varchar(50)  NOT NULL,
  title      varchar(255) NOT NULL,
  content    text         NOT NULL,
  view_count integer      NOT NULL DEFAULT 0,
  create_at  timestamp    NOT NULL,
  status     varchar(20) ,
  user_id    bigint       NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE post IS '게시판';

CREATE TABLE rank_history
(
  id           bigint       NOT NULL GENERATED ALWAYS AS IDENTITY,
  change_point integer      NOT NULL,
  reason       varchar(255),
  user_id      bigint       NOT NULL,
  post_id      bigint       NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

COMMENT ON TABLE rank_history IS '랭크 점수 이력';

CREATE TABLE roles
(
  id        bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
  role_name varchar(50) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

COMMENT ON TABLE roles IS '권한';

CREATE TABLE user_roles
(
  id      bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE user_roles IS '사용자권한매핑';

CREATE TABLE users
(
  id         bigint       NOT NULL GENERATED ALWAYS AS IDENTITY,
  uuid       varchar(255) NOT NULL UNIQUE,
  password   varchar(255) NOT NULL,
  email      varchar(255) NOT NULL UNIQUE,
  nickname   varchar(50)  NOT NULL UNIQUE,
  gender     varchar(1)   NOT NULL,
  create_at  timestamp    NOT NULL,
  rank_score integer      NOT NULL DEFAULT 0,
  address_id bigint       NOT NULL,
  job_id     bigint       NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE users IS '사용자';

ALTER TABLE post
  ADD CONSTRAINT FK_users_TO_post
    FOREIGN KEY (user_id)
    REFERENCES users (id);

ALTER TABLE expense
  ADD CONSTRAINT FK_users_TO_expense
    FOREIGN KEY (user_id)
    REFERENCES users (id);

ALTER TABLE comment
  ADD CONSTRAINT FK_post_TO_comment
    FOREIGN KEY (post_id)
    REFERENCES post (id);

ALTER TABLE comment
  ADD CONSTRAINT FK_users_TO_comment
    FOREIGN KEY (user_id)
    REFERENCES users (id);

ALTER TABLE expense
  ADD CONSTRAINT FK_expense_category_TO_expense
    FOREIGN KEY (category_id)
    REFERENCES expense_category (id);

ALTER TABLE attachment
  ADD CONSTRAINT FK_post_TO_attachment
    FOREIGN KEY (post_id)
    REFERENCES post (id);

ALTER TABLE rank_history
  ADD CONSTRAINT FK_users_TO_rank_history
    FOREIGN KEY (user_id)
    REFERENCES users (id);

ALTER TABLE rank_history
  ADD CONSTRAINT FK_post_TO_rank_history
    FOREIGN KEY (post_id)
    REFERENCES post (id);

ALTER TABLE user_roles
  ADD CONSTRAINT FK_users_TO_user_roles
    FOREIGN KEY (user_id)
    REFERENCES users (id);

ALTER TABLE user_roles
  ADD CONSTRAINT FK_roles_TO_user_roles
    FOREIGN KEY (role_id)
    REFERENCES roles (id);

ALTER TABLE users
  ADD CONSTRAINT FK_address_TO_users
    FOREIGN KEY (address_id)
    REFERENCES address (id);

ALTER TABLE users
  ADD CONSTRAINT FK_job_TO_users
    FOREIGN KEY (job_id)
    REFERENCES job (id);
