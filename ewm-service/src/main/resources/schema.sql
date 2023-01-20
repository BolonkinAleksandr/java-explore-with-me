DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat float                                   NOT NULL,
    lon float                                   NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);


CREATE TABLE IF NOT EXISTS events
(
    id                    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation            VARCHAR(2000)                           NOT NULL,
    category_id           BIGINT                                  NOT NULL,
    confirmed_requests    BIGINT,
    created_on            TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description           VARCHAR(7000)                           NOT NULL,
    event_date            TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id          BIGINT                                  NOT NULL,
    location_id           BIGINT                                  NOT NULL,
    is_paid               BOOLEAN                                 NOT NULL,
    participant_limit     BIGINT,
    published_on          TIMESTAMP WITHOUT TIME ZONE,
    is_request_moderation BOOLEAN                                 NOT NULL,
    state                 VARCHAR(50)                             NOT NULL,
    title                 VARCHAR(120)                            NOT NULL,
    views                 BIGINT                                  NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (initiator_id) REFERENCES users (id),
    FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    is_pinned BOOLEAN,
    title     varchar(500)                            NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id),
    CONSTRAINT UQ_COMPILATIONS_TITLE UNIQUE (title)
);


CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text     VARCHAR(1000)               NOT NULL,
    user_id  BIGINT                      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT                      NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    status   VARCHAR(20)                 NOT NULL
);