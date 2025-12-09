-- 01-hmk.sql


-- Users table
CREATE TABLE IF NOT EXISTS hmk.users (
                                         id BIGSERIAL PRIMARY KEY,
                                         login VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

-- Games table (For future expansion, e.g., different game modes)
CREATE TABLE IF NOT EXISTS hmk.games (
                                         id BIGSERIAL PRIMARY KEY,
                                         name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

-- Records table (Linking Users and Games)
CREATE TABLE IF NOT EXISTS hmk.records (
                                           id BIGSERIAL PRIMARY KEY,
                                           user_id BIGINT NOT NULL,
                                           game_id BIGINT NOT NULL,
                                           score INTEGER NOT NULL,
                                           played_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                                           CONSTRAINT fk_records_user FOREIGN KEY (user_id)
    REFERENCES hmk.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_records_game FOREIGN KEY (game_id)
    REFERENCES hmk.games(id) ON DELETE CASCADE
    );

-- Indexes for performance
CREATE INDEX idx_users_login ON hmk.users(login);
CREATE INDEX idx_records_user ON hmk.records(user_id);
CREATE INDEX idx_records_score ON hmk.records(score);

-- Insert the default game
INSERT INTO hmk.games (name) VALUES ('How Many Krabby Patties?') ON CONFLICT DO NOTHING;