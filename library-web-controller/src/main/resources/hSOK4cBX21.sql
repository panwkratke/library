-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Czas generowania: 10 Lut 2020, 00:43
-- Wersja serwera: 8.0.13-4
-- Wersja PHP: 7.2.24-0ubuntu0.18.04.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `hSOK4cBX21`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `book`
--

CREATE TABLE `book` (
  `id` bigint(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `author` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `genre` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pages` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `book`
--

INSERT INTO `book` (`id`, `creation_date`, `update_date`, `author`, `genre`, `pages`, `quantity`, `title`) VALUES
(62, '2020-01-25 16:24:44', '2020-01-26 12:35:24', 'Lisa Jewell', 'Drama', 334, 5, 'Then She Was Gone'),
(61, '2020-01-25 16:23:30', '2020-02-02 17:22:41', 'J. R. R. Tolkien', 'Fantasy', 452, 4, 'The Silmarillion'),
(60, '2020-01-25 16:22:49', '2020-01-31 17:47:50', 'J. R. R. Tolkien', 'Fantasy', 176, 5, 'The Lord of the Rings: The Return of the King'),
(59, '2020-01-25 16:22:17', '2020-01-31 17:45:01', 'J. R. R. Tolkien', 'Fantasy', 37, 5, 'The Lord of the Rings: The Two Towers'),
(58, '2020-01-25 16:21:31', NULL, 'J.R.R. Tolkien', 'Fantasy', 132, 5, 'The Lord of the Rings: The Fellowship of the Ring'),
(57, '2020-01-25 16:20:55', '2020-02-02 17:22:30', 'J.R.R. Tolkien', 'Fantasy', 78, 5, 'The Hobbit'),
(63, '2020-01-25 16:25:51', '2020-02-08 23:55:35', 'Lisa Wingate', 'Drama', 137, 5, 'Before We Were Yours'),
(64, '2020-01-25 16:26:20', NULL, 'Delia Owens', 'Drama', 336, 5, 'Where the Crawdads Sing'),
(65, '2020-01-25 16:27:42', NULL, 'Kerry Fisher', 'Drama', 305, 5, 'The Silent Wife'),
(66, '2020-01-25 16:29:21', NULL, 'Jodi Picoult', 'Drama', 475, 5, 'Small Great Things'),
(67, '2020-01-25 16:30:37', NULL, 'Susan Wise Bauer', 'Historical', 498, 5, 'The History of the Ancient World'),
(68, '2020-01-25 16:31:11', NULL, 'Jared Diamond', 'Historical', 467, 5, 'Guns, Germs, and Steel'),
(69, '2020-01-25 16:31:27', '2020-02-10 01:42:23', 'William Manchester', 'Historical', 421, 5, 'A World Lit Only by Fire'),
(70, '2020-01-25 16:31:44', NULL, 'Thomas Asbridge', 'Historical', 379, 5, 'The Crusades'),
(71, '2020-01-25 16:32:11', '2020-02-02 17:21:03', 'Laurence Bergreen', 'Historical', 197, 5, 'Over the Edge of the World'),
(72, '2020-01-25 16:33:24', NULL, 'Shirley Jackson', 'Horror', 149, 5, 'The Haunting of Hill House'),
(73, '2020-01-25 16:34:21', NULL, 'Stephen King', 'Horror', 425, 5, 'The Shining'),
(74, '2020-01-25 16:34:47', NULL, 'John Ajvide Lindqvist', 'Horror', 206, 5, 'Let the Right One In'),
(75, '2020-01-25 16:35:06', NULL, 'William Peter Blatty', 'Horror', 55, 5, 'The Exorcist'),
(76, '2020-01-25 16:35:25', NULL, 'Mary Shelley', 'Horror', 127, 5, 'Frankenstein or The Modern Prometheus'),
(77, '2020-01-25 16:37:17', NULL, 'Nancy Mitford', 'Humor', 374, 5, 'Love in A Cold Climate'),
(78, '2020-01-25 16:38:00', NULL, 'Bob Servant', 'Humor', 144, 5, 'Delete At Your Peril'),
(79, '2020-01-25 16:38:17', NULL, 'Robin Cooper', 'Humor', 123, 5, 'The Timewaster Letters'),
(80, '2020-01-25 16:38:34', NULL, 'Beryl Bainbridge', 'Humor', 130, 5, 'The Bottle Factory Outing'),
(81, '2020-01-25 16:38:53', '2020-02-05 16:27:47', 'Norm Macdonald', 'Humor', 386, 5, 'Based on a True Story'),
(82, '2020-01-25 17:06:20', NULL, 'Dashiell Hammett', 'Mystery', 182, 5, 'The Maltese Falcon'),
(83, '2020-01-25 17:06:45', NULL, 'Stieg Larsson', 'Mystery', 394, 5, 'The Girl with the Dragon Tattoo'),
(84, '2020-01-25 17:07:05', '2020-02-08 23:54:23', 'Agatha Christie', 'Mystery', 117, 5, 'And Then There Were None'),
(85, '2020-01-25 17:07:20', NULL, 'Mark Haddon', 'Mystery', 436, 5, 'The Curious Incident of the Dog in the Night-Time'),
(86, '2020-01-25 17:07:41', NULL, 'Daphne du Maurier', 'Mystery', 500, 5, 'Rebecca'),
(87, '2020-01-25 17:08:41', NULL, 'Tracey Garvis Graves', 'Romance', 415, 5, 'The Girl He Used to Know'),
(88, '2020-01-25 17:09:00', '2020-01-26 10:22:34', 'Colleen Hoover', 'Romance', 142, 5, 'It Ends With Us'),
(89, '2020-01-25 17:09:19', NULL, 'Nicholas Sparks', 'Romance', 387, 5, 'Every Breath'),
(90, '2020-01-25 17:09:36', '2020-02-08 23:52:01', 'Every Breath', 'Romance', 87, 5, 'After'),
(91, '2020-01-25 17:10:01', NULL, 'Amy Byler', 'Romance', 308, 5, 'The Overdue Life of Amy Byler'),
(92, '2020-01-25 17:11:01', NULL, 'Jeff VanderMeer', 'Science Fiction', 439, 5, 'Borne'),
(93, '2020-01-25 17:11:14', NULL, 'Isaac Asimov', 'Science Fiction', 242, 5, 'Foundation'),
(94, '2020-01-25 17:11:26', NULL, 'Alfred Bester', 'Science Fiction', 221, 5, 'The Stars My Destination'),
(95, '2020-01-25 17:11:40', NULL, 'Stanislaw Lem', 'Science Fiction', 200, 5, 'Solaris'),
(96, '2020-01-25 17:11:53', '2020-02-05 15:19:09', 'Frank Herbert', 'Science Fiction', 172, 5, 'Dune'),
(97, '2020-01-25 17:16:20', '2020-02-10 01:42:16', 'Darcey Bell', 'Thriller', 170, 4, 'A Simple Favor'),
(98, '2020-01-25 17:16:40', '2020-01-31 17:44:58', 'Zoje Stage', 'Thriller', 255, 5, 'Baby Teeth'),
(99, '2020-01-25 17:16:58', NULL, 'S.J. Watson', 'Thriller', 164, 5, 'Before I Go to Sleep'),
(100, '2020-01-25 17:17:13', NULL, 'Liane Moriarty', 'Thriller', 408, 5, 'Big Little Lies'),
(101, '2020-01-25 17:17:49', NULL, 'Josh Malerman', 'Thriller', 221, 5, 'Bird Box'),
(102, '2020-01-25 17:19:07', NULL, 'Jane Austen', 'Classic', 46, 5, 'Pride and Prejudice'),
(103, '2020-01-25 17:19:25', '2020-02-05 16:27:30', 'Harper Lee', 'Classic', 374, 5, 'To Kill a Mockingbird'),
(104, '2020-01-25 17:19:39', NULL, 'F. Scott Fitzgerald', 'Classic', 181, 5, 'The Great Gatsby'),
(105, '2020-01-25 17:19:58', NULL, 'Gabriel García Márquez', 'Classic', 411, 5, 'One Hundred Years of Solitude'),
(106, '2020-01-25 17:20:16', '2020-01-25 19:33:56', 'Truman Capote', 'Classic', 284, 5, 'In Cold Blood'),
(107, '2020-01-25 17:21:37', NULL, 'Robert Macfarlane', 'Nature', 352, 5, 'Underland'),
(108, '2020-01-25 17:22:24', NULL, 'Isabella Tree', 'Nature', 378, 5, 'Wilding: The Return of Nature to a British Farm'),
(109, '2020-01-25 17:23:04', NULL, 'Jonny Keeling', 'Nature', 198, 5, 'Seven Worlds One Planet'),
(110, '2020-01-25 17:23:20', NULL, 'Peter Wohlleben', 'Nature', 410, 5, 'The Hidden Life of Trees'),
(111, '2020-01-25 17:23:37', NULL, 'Tristan Gooley', 'Nature', 453, 5, 'The Walker\'s Guide to Outdoor Clues and Signs');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `borrow_order`
--

CREATE TABLE `borrow_order` (
  `id` bigint(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `order_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `book_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `dictionary`
--

CREATE TABLE `dictionary` (
  `id` bigint(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `dictionary_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `dictionary`
--

INSERT INTO `dictionary` (`id`, `creation_date`, `update_date`, `dictionary_name`) VALUES
(30, '2020-01-22 10:20:44', NULL, 'RoleDictionary'),
(35, '2020-01-24 00:33:52', NULL, 'BookGenreDictionary');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `dictionary_words`
--

CREATE TABLE `dictionary_words` (
  `dictionary_id` bigint(20) NOT NULL,
  `words` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `dictionary_words`
--

INSERT INTO `dictionary_words` (`dictionary_id`, `words`) VALUES
(30, 'CREATOR'),
(30, 'ADMIN'),
(30, 'USER'),
(35, 'Fantasy'),
(35, 'Drama'),
(35, 'Historical'),
(35, 'Horror'),
(35, 'Humor'),
(35, 'Mystery'),
(35, 'Romance'),
(35, 'Science Fiction'),
(35, 'Thriller'),
(35, 'Classic'),
(35, 'Nature');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(193),
(193),
(193),
(193);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `m2m_user2book`
--

CREATE TABLE `m2m_user2book` (
  `user_id` bigint(20) NOT NULL,
  `book_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `m2m_user2role`
--

CREATE TABLE `m2m_user2role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `m2m_user2role`
--

INSERT INTO `m2m_user2role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2),
(3, 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `role`
--

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `role`
--

INSERT INTO `role` (`id`, `creation_date`, `update_date`, `name`) VALUES
(1, '2020-01-09 10:44:15', NULL, 'CREATOR'),
(2, '2020-01-09 10:44:15', NULL, 'ADMIN'),
(3, '2020-01-16 19:57:28', NULL, 'USER');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `first_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password_hash` tinyblob,
  `token` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`id`, `creation_date`, `update_date`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`, `first_name`, `last_name`, `password_hash`, `token`, `username`) VALUES
(1, '2020-01-09 11:46:43', NULL, b'1', b'1', b'1', b'1', 'CREATOR', 'CREATOR', 0xfdd5f6745e3ae0acae613e1e69c3042a0bee192e81d812448dec52022cff2aa1, NULL, 'CREATOR'),
(2, '2020-01-09 11:48:51', NULL, b'1', b'1', b'1', b'1', 'ADMIN', 'ADMIN', 0x835d6dc88b708bc646d6db82c853ef4182fabbd4a8de59c213f2b5ab3ae7d9be, NULL, 'ADMIN'),
(3, '2020-01-24 15:52:22', NULL, b'1', b'1', b'1', b'1', 'USER', 'USER', 0x92b7b421992ef490f3b75898ec0e511f1a5c02422819d89719b20362b023ee4f, NULL, 'USER');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `borrow_order`
--
ALTER TABLE `borrow_order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKedl37mn2i8khg0hrr374epcm9` (`book_id`),
  ADD KEY `FKhq0jtjiv0dtjfbl7eoreyoykg` (`user_id`);

--
-- Indeksy dla tabeli `dictionary`
--
ALTER TABLE `dictionary`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `dictionary_words`
--
ALTER TABLE `dictionary_words`
  ADD KEY `FKsgb7raijb9c260xxvhm9684jp` (`dictionary_id`);

--
-- Indeksy dla tabeli `m2m_user2book`
--
ALTER TABLE `m2m_user2book`
  ADD PRIMARY KEY (`user_id`,`book_id`),
  ADD KEY `FKpwoa5982d6u28c4aliof8elu` (`book_id`);

--
-- Indeksy dla tabeli `m2m_user2role`
--
ALTER TABLE `m2m_user2role`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FK64vlr0055tn0v8yup8pxe3vjv` (`role_id`);

--
-- Indeksy dla tabeli `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
