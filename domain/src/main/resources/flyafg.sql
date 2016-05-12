-- phpMyAdmin SQL Dump
-- version 4.5.3.1
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Апр 28 2016 г., 15:57
-- Версия сервера: 5.7.11
-- Версия PHP: 5.6.19-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `flyafg`
--

-- --------------------------------------------------------

--
-- Структура таблицы `flights`
--

CREATE TABLE `flights` (
  `id`              INT(10) UNSIGNED        NOT NULL,
  `trip_id`         INT(11)                 NOT NULL,
  `position`        INT(11)                 NOT NULL,
  `parser`          CHAR(2)
                    COLLATE utf8_unicode_ci NOT NULL,
  `carrier_code`    VARCHAR(45)
                    COLLATE utf8_unicode_ci NOT NULL,
  `carrier_name`    VARCHAR(255)
                    COLLATE utf8_unicode_ci NOT NULL,
  `flight_duration` VARCHAR(45)
                    COLLATE utf8_unicode_ci NOT NULL,
  `cabin`           CHAR(1)
                    COLLATE utf8_unicode_ci NOT NULL,
  `depart_time`     VARCHAR(45)
                    COLLATE utf8_unicode_ci NOT NULL,
  `depart_date`     DATE                    NOT NULL,
  `depart_place`    VARCHAR(255)
                    COLLATE utf8_unicode_ci NOT NULL,
  `depart_code`     CHAR(3)
                    COLLATE utf8_unicode_ci NOT NULL,
  `arrive_time`     VARCHAR(45)
                    COLLATE utf8_unicode_ci NOT NULL,
  `arrive_date`     DATE                    NOT NULL,
  `arrive_place`    VARCHAR(255)
                    COLLATE utf8_unicode_ci NOT NULL,
  `arrive_code`     CHAR(3)
                    COLLATE utf8_unicode_ci NOT NULL,
  `flight_number`   VARCHAR(45)
                    COLLATE utf8_unicode_ci NOT NULL,
  `layover`         VARCHAR(45)
                    COLLATE utf8_unicode_ci NOT NULL,
  `aircraft`        VARCHAR(255)
                    COLLATE utf8_unicode_ci NOT NULL,
  `created_at`      TIMESTAMP               NULL DEFAULT NULL,
  `updated_at`      TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `migrations`
--

CREATE TABLE `migrations` (
  `migration` VARCHAR(255)
              COLLATE utf8_unicode_ci NOT NULL,
  `batch`     INT(11)                 NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `miles`
--

CREATE TABLE `miles` (
  `id`         INT(10) UNSIGNED        NOT NULL,
  `from`       CHAR(3)
               COLLATE utf8_unicode_ci NOT NULL,
  `to`         CHAR(3)
               COLLATE utf8_unicode_ci NOT NULL,
  `aircompany` CHAR(2)
               COLLATE utf8_unicode_ci NOT NULL,
  `class`      CHAR(1)
               COLLATE utf8_unicode_ci NOT NULL,
  `cost`       DOUBLE(8, 2)            NOT NULL,
  `tax`        DOUBLE(8, 2)            NOT NULL,
  `tax_out`    DOUBLE(8, 2)            NOT NULL,
  `created_at` TIMESTAMP               NULL DEFAULT NULL,
  `updated_at` TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `miles_and_taxes`
--

CREATE TABLE `miles_and_taxes` (
  `id`         INT(10) UNSIGNED        NOT NULL,
  `flight_numbers` JSON NOT NULL,
  `class`      VARCHAR(255)
               COLLATE utf8_unicode_ci NOT NULL,
  `miles`      DOUBLE(8, 2)            NOT NULL,
  `tax`        DOUBLE(8, 2)            NOT NULL,
  `created_at` TIMESTAMP               NULL DEFAULT NULL,
  `updated_at` TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `mile_costs`
--

CREATE TABLE `mile_costs` (
  `id`         INT(10) UNSIGNED        NOT NULL,
  `parser`     CHAR(2)
               COLLATE utf8_unicode_ci NOT NULL,
  `cost`       DOUBLE(8, 2)            NOT NULL,
  `created_at` TIMESTAMP               NULL DEFAULT NULL,
  `updated_at` TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `parser_answers`
--

CREATE TABLE `parser_answers` (
  `id`         INT(10) UNSIGNED NOT NULL,
  `query_id`   INT(11)          NOT NULL,
  `AA`         INT(11)          NOT NULL,
  `AC`         INT(11)          NOT NULL,
  `AF`         INT(11)          NOT NULL,
  `AS`         INT(11)          NOT NULL,
  `BA`         INT(11)          NOT NULL,
  `CX`         INT(11)          NOT NULL,
  `DL`         INT(11)          NOT NULL,
  `EK`         INT(11)          NOT NULL,
  `EY`         INT(11)          NOT NULL,
  `JL`         INT(11)          NOT NULL,
  `LH`         INT(11)          NOT NULL,
  `NH`         INT(11)          NOT NULL,
  `QF`         INT(11)          NOT NULL,
  `QR`         INT(11)          NOT NULL,
  `SQ`         INT(11)          NOT NULL,
  `VS`         INT(11)          NOT NULL,
  `UA`         INT(11)          NOT NULL,
  `created_at` TIMESTAMP        NULL DEFAULT NULL,
  `updated_at` TIMESTAMP        NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `parser_errors`
--

CREATE TABLE `parser_errors` (
  `id`         INT(10) UNSIGNED        NOT NULL,
  `query_id`   INT(11)                 NOT NULL,
  `parser`     CHAR(2)
               COLLATE utf8_unicode_ci NOT NULL,
  `error_text` VARCHAR(255)
               COLLATE utf8_unicode_ci NOT NULL,
  `created_at` TIMESTAMP               NULL DEFAULT NULL,
  `updated_at` TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `password_resets`
--

CREATE TABLE `password_resets` (
  `email`      VARCHAR(255)
               COLLATE utf8_unicode_ci NOT NULL,
  `token`      VARCHAR(255)
               COLLATE utf8_unicode_ci NOT NULL,
  `created_at` TIMESTAMP               NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `queries`
--

CREATE TABLE `queries` (
  `id`                  INT(10) UNSIGNED        NOT NULL,
  `alias`               VARCHAR(255)
                        COLLATE utf8_unicode_ci NOT NULL,
  `type`                CHAR(2)
                        COLLATE utf8_unicode_ci NOT NULL,
  `from`                CHAR(3)
                        COLLATE utf8_unicode_ci NOT NULL,
  `to`                  CHAR(3)
                        COLLATE utf8_unicode_ci NOT NULL,
  `include_from_nearby` TINYINT(1)              NOT NULL,
  `include_to_nearby`   TINYINT(1)              NOT NULL,
  `departure`           DATE                    NOT NULL,
  `flexible_departure`  TINYINT(1)              NOT NULL,
  `arrival`             DATE                    NOT NULL,
  `flexible_arrival`    TINYINT(1)              NOT NULL,
  `passengers`          INT(11)                 NOT NULL,
  `classes` JSON NOT NULL,
  `parsers` JSON NOT NULL,
  `status`              INT(11)                 NOT NULL,
  `error`               TINYINT(1)              NOT NULL,
  `created_at`          TIMESTAMP               NULL DEFAULT NULL,
  `updated_at`          TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `trips`
--

CREATE TABLE `trips` (
  `id`            INT(10) UNSIGNED        NOT NULL,
  `query_id`      INT(11)                 NOT NULL,
  `depart_code`   CHAR(3)
                  COLLATE utf8_unicode_ci NOT NULL,
  `arrive_code`   CHAR(3)
                  COLLATE utf8_unicode_ci NOT NULL,
  `depart_place`  VARCHAR(255)
                  COLLATE utf8_unicode_ci NOT NULL,
  `arrive_place`  VARCHAR(255)
                  COLLATE utf8_unicode_ci NOT NULL,
  `trip_date`     DATE                    NOT NULL,
  `trip_duration` VARCHAR(45)
                  COLLATE utf8_unicode_ci NOT NULL,
  `stops` JSON NOT NULL,
  `cabins` JSON NOT NULL,
  `carriers` JSON NOT NULL,
  `layovers` JSON NOT NULL,
  `flight_legs` JSON NOT NULL,
  `flight_numbers` JSON NOT NULL,
  `cost`          DOUBLE(8, 2)            NOT NULL,
  `created_at`    TIMESTAMP               NULL DEFAULT NULL,
  `updated_at`    TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `trip_costs`
--

CREATE TABLE `trip_costs` (
  `id`          INT(10) UNSIGNED NOT NULL,
  `trip_id`     INT(11)          NOT NULL,
  `miles`       INT(11)          NOT NULL,
  `tax`         DOUBLE(8, 2)     NOT NULL,
  `parser_cost` DOUBLE(8, 2)     NOT NULL,
  `aa_cost`     DOUBLE(8, 2)     NOT NULL,
  `sq_cost`     DOUBLE(8, 2)     NOT NULL,
  `nh_cost`     DOUBLE(8, 2)     NOT NULL,
  `ey_cost`     DOUBLE(8, 2)     NOT NULL,
  `lh_cost`     DOUBLE(8, 2)     NOT NULL,
  `cx_cost`     DOUBLE(8, 2)     NOT NULL,
  `created_at`  TIMESTAMP        NULL DEFAULT NULL,
  `updated_at`  TIMESTAMP        NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `id`             INT(10) UNSIGNED        NOT NULL,
  `name`           VARCHAR(255)
                   COLLATE utf8_unicode_ci NOT NULL,
  `email`          VARCHAR(255)
                   COLLATE utf8_unicode_ci NOT NULL,
  `password`       VARCHAR(255)
                   COLLATE utf8_unicode_ci NOT NULL,
  `remember_token` VARCHAR(100)
                   COLLATE utf8_unicode_ci      DEFAULT NULL,
  `created_at`     TIMESTAMP               NULL DEFAULT NULL,
  `updated_at`     TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `zones`
--

CREATE TABLE `zones` (
  `id`             INT(10) UNSIGNED        NOT NULL,
  `code`           CHAR(3)
                   COLLATE utf8_unicode_ci NOT NULL,
  `mileage_region` VARCHAR(255)
                   COLLATE utf8_unicode_ci NOT NULL,
  `region`         CHAR(5)
                   COLLATE utf8_unicode_ci NOT NULL,
  `aa_region`      CHAR(5)
                   COLLATE utf8_unicode_ci NOT NULL,
  `sq_region`      CHAR(5)
                   COLLATE utf8_unicode_ci NOT NULL,
  `nh_region`      CHAR(5)
                   COLLATE utf8_unicode_ci NOT NULL,
  `lh_region`      CHAR(5)
                   COLLATE utf8_unicode_ci NOT NULL,
  `created_at`     TIMESTAMP               NULL DEFAULT NULL,
  `updated_at`     TIMESTAMP               NULL DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `flights`
--
ALTER TABLE `flights`
ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `miles`
--
ALTER TABLE `miles`
ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `miles_and_taxes`
--
ALTER TABLE `miles_and_taxes`
ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `mile_costs`
--
ALTER TABLE `mile_costs`
ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `parser_answers`
--
ALTER TABLE `parser_answers`
ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `parser_errors`
--
ALTER TABLE `parser_errors`
ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `password_resets`
--
ALTER TABLE `password_resets`
ADD KEY `password_resets_email_index` (`email`),
ADD KEY `password_resets_token_index` (`token`);

--
-- Индексы таблицы `queries`
--
ALTER TABLE `queries`
ADD PRIMARY KEY (`id`),
ADD UNIQUE KEY `requests_alias_unique` (`alias`);

--
-- Индексы таблицы `trips`
--
ALTER TABLE `trips`
ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `trip_costs`
--
ALTER TABLE `trip_costs`
ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
ADD PRIMARY KEY (`id`),
ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- Индексы таблицы `zones`
--
ALTER TABLE `zones`
ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `flights`
--
ALTER TABLE `flights`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `miles`
--
ALTER TABLE `miles`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `miles_and_taxes`
--
ALTER TABLE `miles_and_taxes`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `mile_costs`
--
ALTER TABLE `mile_costs`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `parser_answers`
--
ALTER TABLE `parser_answers`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `parser_errors`
--
ALTER TABLE `parser_errors`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `queries`
--
ALTER TABLE `queries`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `trips`
--
ALTER TABLE `trips`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `trip_costs`
--
ALTER TABLE `trip_costs`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `zones`
--
ALTER TABLE `zones`
MODIFY `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
