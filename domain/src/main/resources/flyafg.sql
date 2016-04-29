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


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `flyafg`
--

-- --------------------------------------------------------

--
-- Структура таблицы `flights`
--

CREATE TABLE `flights` (
  `id` int(10) UNSIGNED NOT NULL,
  `trip_id` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `parser` char(2) COLLATE utf8_unicode_ci NOT NULL,
  `carrier_code` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `carrier_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `flight_duration` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `cabin` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `depart_time` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `depart_date` date NOT NULL,
  `depart_place` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `depart_code` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `arrive_time` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `arrive_date` date NOT NULL,
  `arrive_place` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `arrive_code` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `flight_number` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `layover` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `aircraft` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `migrations`
--

CREATE TABLE `migrations` (
  `migration` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `miles`
--

CREATE TABLE `miles` (
  `id` int(10) UNSIGNED NOT NULL,
  `from` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `to` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `aircompany` char(2) COLLATE utf8_unicode_ci NOT NULL,
  `class` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `cost` double(8,2) NOT NULL,
  `tax` double(8,2) NOT NULL,
  `tax_out` double(8,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `miles_and_taxes`
--

CREATE TABLE `miles_and_taxes` (
  `id` int(10) UNSIGNED NOT NULL,
  `flight_numbers` json NOT NULL,
  `class` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `miles` double(8,2) NOT NULL,
  `tax` double(8,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `mile_costs`
--

CREATE TABLE `mile_costs` (
  `id` int(10) UNSIGNED NOT NULL,
  `parser` char(2) COLLATE utf8_unicode_ci NOT NULL,
  `cost` double(8,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `parser_answers`
--

CREATE TABLE `parser_answers` (
  `id` int(10) UNSIGNED NOT NULL,
  `query_id` int(11) NOT NULL,
  `AA` int(11) NOT NULL,
  `AC` int(11) NOT NULL,
  `AF` int(11) NOT NULL,
  `AS` int(11) NOT NULL,
  `BA` int(11) NOT NULL,
  `CX` int(11) NOT NULL,
  `DL` int(11) NOT NULL,
  `EK` int(11) NOT NULL,
  `EY` int(11) NOT NULL,
  `JL` int(11) NOT NULL,
  `LH` int(11) NOT NULL,
  `NH` int(11) NOT NULL,
  `QF` int(11) NOT NULL,
  `QR` int(11) NOT NULL,
  `SQ` int(11) NOT NULL,
  `VS` int(11) NOT NULL,
  `UA` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `parser_errors`
--

CREATE TABLE `parser_errors` (
  `id` int(10) UNSIGNED NOT NULL,
  `query_id` int(11) NOT NULL,
  `parser` char(2) COLLATE utf8_unicode_ci NOT NULL,
  `error_text` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `queries`
--

CREATE TABLE `queries` (
  `id` int(10) UNSIGNED NOT NULL,
  `alias` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `type` char(2) COLLATE utf8_unicode_ci NOT NULL,
  `from` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `to` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `include_from_nearby` tinyint(1) NOT NULL,
  `include_to_nearby` tinyint(1) NOT NULL,
  `departure` date NOT NULL,
  `flexible_departure` tinyint(1) NOT NULL,
  `arrival` date NOT NULL,
  `flexible_arrival` tinyint(1) NOT NULL,
  `passengers` int(11) NOT NULL,
  `classes` json NOT NULL,
  `parsers` json NOT NULL,
  `status` int(11) NOT NULL,
  `error` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `trips`
--

CREATE TABLE `trips` (
  `id` int(10) UNSIGNED NOT NULL,
  `query_id` int(11) NOT NULL,
  `depart_code` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `arrive_code` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `depart_place` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `arrive_place` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `trip_date` date NOT NULL,
  `trip_duration` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `stops` json NOT NULL,
  `cabins` json NOT NULL,
  `carriers` json NOT NULL,
  `layovers` json NOT NULL,
  `flight_legs` json NOT NULL,
  `flight_numbers` json NOT NULL,
  `cost` double(8,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `trip_costs`
--

CREATE TABLE `trip_costs` (
  `id` int(10) UNSIGNED NOT NULL,
  `trip_id` int(11) NOT NULL,
  `miles` int(11) NOT NULL,
  `tax` double(8,2) NOT NULL,
  `parser_cost` double(8,2) NOT NULL,
  `aa_cost` double(8,2) NOT NULL,
  `sq_cost` double(8,2) NOT NULL,
  `nh_cost` double(8,2) NOT NULL,
  `ey_cost` double(8,2) NOT NULL,
  `lh_cost` double(8,2) NOT NULL,
  `cx_cost` double(8,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `remember_token` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `zones`
--

CREATE TABLE `zones` (
  `id` int(10) UNSIGNED NOT NULL,
  `code` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `mileage_region` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `region` char(5) COLLATE utf8_unicode_ci NOT NULL,
  `aa_region` char(5) COLLATE utf8_unicode_ci NOT NULL,
  `sq_region` char(5) COLLATE utf8_unicode_ci NOT NULL,
  `nh_region` char(5) COLLATE utf8_unicode_ci NOT NULL,
  `lh_region` char(5) COLLATE utf8_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `miles`
--
ALTER TABLE `miles`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `miles_and_taxes`
--
ALTER TABLE `miles_and_taxes`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `mile_costs`
--
ALTER TABLE `mile_costs`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `parser_answers`
--
ALTER TABLE `parser_answers`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `parser_errors`
--
ALTER TABLE `parser_errors`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `queries`
--
ALTER TABLE `queries`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `trips`
--
ALTER TABLE `trips`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `trip_costs`
--
ALTER TABLE `trip_costs`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `zones`
--
ALTER TABLE `zones`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
