-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 08 Nov 2024 pada 02.29
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bioskop`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `film_schedule`
--

CREATE TABLE `film_schedule` (
  `id` int(11) NOT NULL,
  `movie_id` int(11) NOT NULL,
  `theater_id` int(11) NOT NULL,
  `schedule_date` datetime NOT NULL,
  `price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `film_schedule`
--

INSERT INTO `film_schedule` (`id`, `movie_id`, `theater_id`, `schedule_date`, `price`) VALUES
(1, 1, 1, '2024-12-01 15:30:00', 40000.00),
(2, 3, 1, '2024-11-08 10:00:00', 35000.00),
(3, 2, 2, '2024-11-08 22:30:00', 35000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `movies`
--

CREATE TABLE `movies` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `genre` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `movies`
--

INSERT INTO `movies` (`id`, `title`, `description`, `duration`, `genre`) VALUES
(1, 'Avengers: Endgame', 'Sebuah film superhero Marvel yang melibatkan banyak karakter hero dalam pertempuran melawan Thanos.', 181, 'Action'),
(2, 'The Lion King', 'Cerita klasik Disney tentang perjalanan Simba untuk menjadi raja setelah kehilangan ayahnya.', 118, 'Animation'),
(3, 'Spider-Man: No Way Home', 'Peter Parker berjuang melawan berbagai tantangan saat berhadapan dengan multiverse.', 148, 'Action'),
(4, 'Frozen IIi', 'Anna dan Elsa kembali dalam petualangan baru untuk menemukan asal usul kekuatan Elsa.', 110, 'Animation'),
(5, 'Interstellar', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity\'s survival.', 169, 'Adventure, Drama, Sci-Fi');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sales`
--

CREATE TABLE `sales` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `sale_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) DEFAULT NULL,
  `schedule_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `sales`
--

INSERT INTO `sales` (`id`, `customer_id`, `sale_date`, `total_amount`, `schedule_id`) VALUES
(2, 9, '2024-11-07 22:54:11', 70000.00, 3),
(3, 7, '2024-11-08 00:04:57', 70000.00, 3),
(4, 10, '2024-11-08 00:46:51', 70000.00, 3);

-- --------------------------------------------------------

--
-- Struktur dari tabel `sales_detail`
--

CREATE TABLE `sales_detail` (
  `id` int(11) NOT NULL,
  `sale_id` int(11) DEFAULT NULL,
  `seat_id` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `sales_detail`
--

INSERT INTO `sales_detail` (`id`, `sale_id`, `seat_id`, `price`) VALUES
(3, 2, 82, 35000.00),
(4, 2, 83, 35000.00),
(5, 3, 100, 35000.00),
(6, 3, 99, 35000.00),
(7, 4, 92, 35000.00),
(8, 4, 93, 35000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `seat`
--

CREATE TABLE `seat` (
  `id` int(11) NOT NULL,
  `row` char(1) DEFAULT NULL,
  `col` int(11) DEFAULT NULL,
  `is_available` tinyint(1) DEFAULT NULL,
  `theater_id` int(11) DEFAULT NULL,
  `film_schedule_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `seat`
--

INSERT INTO `seat` (`id`, `row`, `col`, `is_available`, `theater_id`, `film_schedule_id`) VALUES
(1, 'A', 1, 1, 1, 1),
(2, 'A', 2, 1, 1, 1),
(3, 'A', 3, 1, 1, 1),
(4, 'A', 4, 1, 1, 1),
(5, 'A', 5, 1, 1, 1),
(6, 'A', 6, 1, 1, 1),
(7, 'A', 7, 1, 1, 1),
(8, 'A', 8, 1, 1, 1),
(9, 'A', 9, 1, 1, 1),
(10, 'A', 10, 1, 1, 1),
(11, 'B', 1, 1, 1, 1),
(12, 'B', 2, 1, 1, 1),
(13, 'B', 3, 1, 1, 1),
(14, 'B', 4, 1, 1, 1),
(15, 'B', 5, 1, 1, 1),
(16, 'B', 6, 1, 1, 1),
(17, 'B', 7, 1, 1, 1),
(18, 'B', 8, 1, 1, 1),
(19, 'B', 9, 1, 1, 1),
(20, 'B', 10, 1, 1, 1),
(21, 'C', 1, 1, 1, 1),
(22, 'C', 2, 1, 1, 1),
(23, 'C', 3, 1, 1, 1),
(24, 'C', 4, 1, 1, 1),
(25, 'C', 5, 1, 1, 1),
(26, 'C', 6, 1, 1, 1),
(27, 'C', 7, 1, 1, 1),
(28, 'C', 8, 1, 1, 1),
(29, 'C', 9, 1, 1, 1),
(30, 'C', 10, 1, 1, 1),
(31, 'D', 1, 1, 1, 1),
(32, 'D', 2, 1, 1, 1),
(33, 'D', 3, 1, 1, 1),
(34, 'D', 4, 1, 1, 1),
(35, 'D', 5, 1, 1, 1),
(36, 'D', 6, 1, 1, 1),
(37, 'D', 7, 1, 1, 1),
(38, 'D', 8, 1, 1, 1),
(39, 'D', 9, 1, 1, 1),
(40, 'D', 10, 1, 1, 1),
(41, 'A', 1, 1, 1, 2),
(42, 'A', 2, 1, 1, 2),
(43, 'A', 3, 1, 1, 2),
(44, 'A', 4, 1, 1, 2),
(45, 'A', 5, 1, 1, 2),
(46, 'A', 6, 1, 1, 2),
(47, 'A', 7, 1, 1, 2),
(48, 'A', 8, 1, 1, 2),
(49, 'A', 9, 1, 1, 2),
(50, 'A', 10, 1, 1, 2),
(51, 'B', 1, 1, 1, 2),
(52, 'B', 2, 1, 1, 2),
(53, 'B', 3, 1, 1, 2),
(54, 'B', 4, 1, 1, 2),
(55, 'B', 5, 1, 1, 2),
(56, 'B', 6, 1, 1, 2),
(57, 'B', 7, 1, 1, 2),
(58, 'B', 8, 1, 1, 2),
(59, 'B', 9, 1, 1, 2),
(60, 'B', 10, 1, 1, 2),
(61, 'C', 1, 1, 1, 2),
(62, 'C', 2, 1, 1, 2),
(63, 'C', 3, 1, 1, 2),
(64, 'C', 4, 1, 1, 2),
(65, 'C', 5, 1, 1, 2),
(66, 'C', 6, 1, 1, 2),
(67, 'C', 7, 1, 1, 2),
(68, 'C', 8, 1, 1, 2),
(69, 'C', 9, 1, 1, 2),
(70, 'C', 10, 1, 1, 2),
(71, 'D', 1, 1, 1, 2),
(72, 'D', 2, 1, 1, 2),
(73, 'D', 3, 1, 1, 2),
(74, 'D', 4, 1, 1, 2),
(75, 'D', 5, 1, 1, 2),
(76, 'D', 6, 1, 1, 2),
(77, 'D', 7, 1, 1, 2),
(78, 'D', 8, 1, 1, 2),
(79, 'D', 9, 1, 1, 2),
(80, 'D', 10, 1, 1, 2),
(81, 'A', 1, 1, 2, 3),
(82, 'A', 2, 0, 2, 3),
(83, 'A', 3, 0, 2, 3),
(84, 'A', 4, 1, 2, 3),
(85, 'A', 5, 1, 2, 3),
(86, 'A', 6, 1, 2, 3),
(87, 'A', 7, 1, 2, 3),
(88, 'A', 8, 1, 2, 3),
(89, 'A', 9, 1, 2, 3),
(90, 'A', 10, 1, 2, 3),
(91, 'B', 1, 1, 2, 3),
(92, 'B', 2, 0, 2, 3),
(93, 'B', 3, 0, 2, 3),
(94, 'B', 4, 1, 2, 3),
(95, 'B', 5, 1, 2, 3),
(96, 'B', 6, 1, 2, 3),
(97, 'B', 7, 1, 2, 3),
(98, 'B', 8, 1, 2, 3),
(99, 'B', 9, 0, 2, 3),
(100, 'B', 10, 0, 2, 3);

-- --------------------------------------------------------

--
-- Struktur dari tabel `theater`
--

CREATE TABLE `theater` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `theater`
--

INSERT INTO `theater` (`id`, `name`, `description`) VALUES
(1, 'Teater A', 'Teater dengan kapasitas 150 kursi dan teknologi Dolby Atmos.'),
(2, 'Teater B', 'Teater dengan kapasitas 200 kursi, dilengkapi dengan sistem suara surround 7.1.');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` enum('user','staff') DEFAULT 'user',
  `date_of_birth` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `date_of_birth`) VALUES
(5, 'Admin1', 'admin1@gmail.com', '$2a$10$/Z1np0N9K.hHIQjZLk49a.JxM8pT1KUdrc78R8X1D0wdyl/utsbo.', 'staff', NULL),
(6, 'Kamila', 'kamila@gmail.com', '$2a$10$aMOvxpb032YF04N58auA7enVi0UCNW6VQdak9HltyLaliOiRGfwqq', 'user', '2005-07-08'),
(7, 'Vanno', 'vanno@gmail.com', '$2a$10$oQC37IxX75eiRDNEE9hLT.AdEyWd54lRLHq8IILes8hHc9a0YlXk2', 'user', '2006-11-01'),
(9, 'Puput', 'puput@gmail.com', '$2a$10$Vvb0TnqTytKBCUJpAet9.On2/4ixPkQro7ycC9g4/XRJWaJ1hMIUG', 'user', '1995-12-12'),
(10, 'Laili', 'laili@gmail.com', '$2a$10$tLcbHveSDoIKRBODyX3O3u/OmjXkWNcYlRPu3.lDucHyXvCDsATxu', 'user', '2002-09-02');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `film_schedule`
--
ALTER TABLE `film_schedule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `movie_id` (`movie_id`),
  ADD KEY `theater_id` (`theater_id`);

--
-- Indeks untuk tabel `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `schedule_id` (`schedule_id`);

--
-- Indeks untuk tabel `sales_detail`
--
ALTER TABLE `sales_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sale_id` (`sale_id`),
  ADD KEY `seat_id` (`seat_id`);

--
-- Indeks untuk tabel `seat`
--
ALTER TABLE `seat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `theater_id` (`theater_id`),
  ADD KEY `film_schedule_id` (`film_schedule_id`);

--
-- Indeks untuk tabel `theater`
--
ALTER TABLE `theater`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `film_schedule`
--
ALTER TABLE `film_schedule`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `movies`
--
ALTER TABLE `movies`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `sales`
--
ALTER TABLE `sales`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `sales_detail`
--
ALTER TABLE `sales_detail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `seat`
--
ALTER TABLE `seat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=101;

--
-- AUTO_INCREMENT untuk tabel `theater`
--
ALTER TABLE `theater`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `film_schedule`
--
ALTER TABLE `film_schedule`
  ADD CONSTRAINT `film_schedule_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  ADD CONSTRAINT `film_schedule_ibfk_2` FOREIGN KEY (`theater_id`) REFERENCES `theater` (`id`);

--
-- Ketidakleluasaan untuk tabel `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `sales_ibfk_3` FOREIGN KEY (`schedule_id`) REFERENCES `film_schedule` (`id`);

--
-- Ketidakleluasaan untuk tabel `sales_detail`
--
ALTER TABLE `sales_detail`
  ADD CONSTRAINT `sales_detail_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`id`),
  ADD CONSTRAINT `sales_detail_ibfk_2` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`);

--
-- Ketidakleluasaan untuk tabel `seat`
--
ALTER TABLE `seat`
  ADD CONSTRAINT `seat_ibfk_1` FOREIGN KEY (`theater_id`) REFERENCES `theater` (`id`),
  ADD CONSTRAINT `seat_ibfk_2` FOREIGN KEY (`film_schedule_id`) REFERENCES `film_schedule` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
