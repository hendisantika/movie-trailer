-- Flyway Migration: Insert 20 Dummy Movies
-- Description: Seeds the database with 20 popular movies across various genres
-- Author: Generated Migration
-- Date: 03/11/2025

-- Ensure genres exist (idempotent)
INSERT INTO genre (id, title) VALUES (1, 'Action') ON CONFLICT (id) DO NOTHING;
INSERT INTO genre (id, title) VALUES (2, 'Drama') ON CONFLICT (id) DO NOTHING;
INSERT INTO genre (id, title) VALUES (3, 'Comedy') ON CONFLICT (id) DO NOTHING;
INSERT INTO genre (id, title) VALUES (4, 'Thriller') ON CONFLICT (id) DO NOTHING;
INSERT INTO genre (id, title) VALUES (5, 'Sci-Fi') ON CONFLICT (id) DO NOTHING;
INSERT INTO genre (id, title) VALUES (6, 'Romance') ON CONFLICT (id) DO NOTHING;
INSERT INTO genre (id, title) VALUES (7, 'Horror') ON CONFLICT (id) DO NOTHING;
INSERT INTO genre (id, title) VALUES (8, 'Adventure') ON CONFLICT (id) DO NOTHING;

-- Insert 20 Dummy Movies with Real Poster URLs and YouTube Trailer IDs
INSERT INTO movie (title, sinopsis, premiere_date, youtube_trailer_id, route_cover) VALUES
('The Matrix', 'A computer hacker learns about the true nature of his reality and his role in the war against its controllers.', '1999-03-31', 'm8e-FF8MkqU', 'https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg'),
('Inception', 'A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.', '2010-07-16', 'YoHD9XEInc0', 'https://image.tmdb.org/t/p/w500/ljsZTbVsrQSqZgWeep2B1QiDKuh.jpg'),
('The Dark Knight', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests.', '2008-07-18', 'EXeTwQWrcwY', 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg'),
('Interstellar', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity survival.', '2014-11-07', 'zSWdZVtXT7E', 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg'),
('The Shawshank Redemption', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', '1994-09-23', '6hB3S9bIaco', 'https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg'),
('Pulp Fiction', 'The lives of two mob hitmen, a boxer, a gangster and his wife intertwine in four tales of violence and redemption.', '1994-10-14', 's7EdQ4FqbhY', 'https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg'),
('Forrest Gump', 'A man with a low IQ has accomplished great things in his life and been present during significant historic events.', '1994-07-06', 'bLvqoHBptjg', 'https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg'),
('Avengers: Endgame', 'After the devastating events of Infinity War, the Avengers assemble once more to reverse Thanos actions and restore balance to the universe.', '2019-04-26', 'TcMBFSGVi1c', 'https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg'),
('Parasite', 'A poor family schemes to become employed by a wealthy family and infiltrate their household by posing as unrelated, highly qualified individuals.', '2019-05-30', '5xH0HfJHsaY', 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg'),
('Joker', 'During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City.', '2019-10-04', 'zAGVQLHvwOY', 'https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg'),
('The Godfather', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', '1972-03-24', 'sY1S34973zA', 'https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg'),
('Gladiator', 'A former Roman General sets out to exact vengeance against the corrupt emperor who murdered his family and sent him into slavery.', '2000-05-05', 'owK1qxDselE', 'https://image.tmdb.org/t/p/w500/ty8TGRuvJLPUmAR1H1nRIsgwvim.jpg'),
('Titanic', 'A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.', '1997-12-19', 'kVrqfYjkFsE', 'https://image.tmdb.org/t/p/w500/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg'),
('The Lion King', 'Lion prince Simba and his father are targeted by his bitter uncle, who wants to ascend the throne himself.', '1994-06-24', 'lFzVJEksoDY', 'https://image.tmdb.org/t/p/w500/sKCr78MXSLixwmZ8DyJLrpMsd15.jpg'),
('Jurassic Park', 'A pragmatic paleontologist touring an almost complete theme park on an island is tasked with protecting a couple of kids after a power failure.', '1993-06-11', 'lc0UehYemOA', 'https://image.tmdb.org/t/p/w500/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg'),
('The Silence of the Lambs', 'A young FBI cadet must receive the help of an incarcerated cannibal killer to catch another serial killer who skins his victims.', '1991-02-14', 'W6Mm8Sbe__o', 'https://image.tmdb.org/t/p/w500/uS9m8OBk1A8eM9I042bx8XXpqAq.jpg'),
('Spider-Man: No Way Home', 'Peter Parker seeks Doctor Strange help to make the world forget he is Spider-Man, but the spell goes wrong and brings villains from other universes.', '2021-12-17', 'JfVOs4VSpmA', 'https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg'),
('Dune', 'Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.', '2021-10-22', '8g18jFHCLXk', 'https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg'),
('Oppenheimer', 'The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during World War II.', '2023-07-21', 'uYPbbksJxIg', 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg'),
('The Grand Budapest Hotel', 'A writer encounters the owner of an aging high-class hotel, who tells him of his early years serving as a lobby boy in the hotel golden age.', '2014-03-28', '1Fg5iWmQjwk', 'https://image.tmdb.org/t/p/w500/eWdyYQreja6JGCzqHWXpWHDrrPo.jpg')
ON CONFLICT DO NOTHING;

-- Link Movies with Genres (genre_movie junction table)

-- The Matrix - Action, Sci-Fi
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 1 FROM movie m WHERE m.title = 'The Matrix' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 1
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 5 FROM movie m WHERE m.title = 'The Matrix' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 5
);

-- Inception - Action, Sci-Fi, Thriller
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 1 FROM movie m WHERE m.title = 'Inception' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 1
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 5 FROM movie m WHERE m.title = 'Inception' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 5
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 4 FROM movie m WHERE m.title = 'Inception' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 4
);

-- The Dark Knight - Action, Drama, Thriller
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 1 FROM movie m WHERE m.title = 'The Dark Knight' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 1
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'The Dark Knight' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 4 FROM movie m WHERE m.title = 'The Dark Knight' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 4
);

-- Interstellar - Sci-Fi, Drama, Adventure
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 5 FROM movie m WHERE m.title = 'Interstellar' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 5
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Interstellar' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 8 FROM movie m WHERE m.title = 'Interstellar' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 8
);

-- The Shawshank Redemption - Drama
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'The Shawshank Redemption' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);

-- Pulp Fiction - Drama, Thriller, Comedy
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Pulp Fiction' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 4 FROM movie m WHERE m.title = 'Pulp Fiction' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 4
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 3 FROM movie m WHERE m.title = 'Pulp Fiction' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 3
);

-- Forrest Gump - Drama, Romance
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Forrest Gump' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 6 FROM movie m WHERE m.title = 'Forrest Gump' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 6
);

-- Avengers: Endgame - Action, Adventure, Sci-Fi
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 1 FROM movie m WHERE m.title = 'Avengers: Endgame' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 1
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 8 FROM movie m WHERE m.title = 'Avengers: Endgame' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 8
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 5 FROM movie m WHERE m.title = 'Avengers: Endgame' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 5
);

-- Parasite - Drama, Thriller
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Parasite' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 4 FROM movie m WHERE m.title = 'Parasite' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 4
);

-- Joker - Drama, Thriller
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Joker' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 4 FROM movie m WHERE m.title = 'Joker' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 4
);

-- The Godfather - Drama, Thriller
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'The Godfather' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 4 FROM movie m WHERE m.title = 'The Godfather' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 4
);

-- Gladiator - Action, Drama, Adventure
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 1 FROM movie m WHERE m.title = 'Gladiator' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 1
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Gladiator' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 8 FROM movie m WHERE m.title = 'Gladiator' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 8
);

-- Titanic - Drama, Romance
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Titanic' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 6 FROM movie m WHERE m.title = 'Titanic' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 6
);

-- The Lion King - Adventure, Drama
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 8 FROM movie m WHERE m.title = 'The Lion King' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 8
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'The Lion King' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);

-- Jurassic Park - Action, Adventure, Sci-Fi
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 1 FROM movie m WHERE m.title = 'Jurassic Park' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 1
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 8 FROM movie m WHERE m.title = 'Jurassic Park' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 8
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 5 FROM movie m WHERE m.title = 'Jurassic Park' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 5
);

-- The Silence of the Lambs - Thriller, Horror, Drama
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 4 FROM movie m WHERE m.title = 'The Silence of the Lambs' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 4
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 7 FROM movie m WHERE m.title = 'The Silence of the Lambs' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 7
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'The Silence of the Lambs' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);

-- Spider-Man: No Way Home - Action, Adventure, Sci-Fi
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 1 FROM movie m WHERE m.title = 'Spider-Man: No Way Home' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 1
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 8 FROM movie m WHERE m.title = 'Spider-Man: No Way Home' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 8
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 5 FROM movie m WHERE m.title = 'Spider-Man: No Way Home' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 5
);

-- Dune - Sci-Fi, Adventure, Drama
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 5 FROM movie m WHERE m.title = 'Dune' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 5
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 8 FROM movie m WHERE m.title = 'Dune' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 8
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Dune' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);

-- Oppenheimer - Drama, Thriller
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'Oppenheimer' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 4 FROM movie m WHERE m.title = 'Oppenheimer' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 4
);

-- The Grand Budapest Hotel - Comedy, Drama, Adventure
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 3 FROM movie m WHERE m.title = 'The Grand Budapest Hotel' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 3
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 2 FROM movie m WHERE m.title = 'The Grand Budapest Hotel' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 2
);
INSERT INTO genre_movie (movie_id, id_genre)
SELECT m.id, 8 FROM movie m WHERE m.title = 'The Grand Budapest Hotel' AND NOT EXISTS (
    SELECT 1 FROM genre_movie gm WHERE gm.movie_id = m.id AND gm.id_genre = 8
);
