-- 01-sample-data.sql

-- Insert Sample Users
INSERT INTO hmk.users (login, password, email) VALUES
    ('player1', 'pass1', 'player1@example.com'),
    ('player2', 'pass2', 'player2@example.com'),
    ('player3', 'pass3', 'player3@example.com')
ON CONFLICT (login) DO NOTHING;

-- Insert Sample Records
-- We sub-select IDs to ensure this works even if IDs change
INSERT INTO hmk.records (user_id, game_id, score, played_at) VALUES
                                                                 ((SELECT id FROM hmk.users WHERE login = 'player1'), 1, 5, CURRENT_TIMESTAMP - INTERVAL '1 day'),
                                                                 ((SELECT id FROM hmk.users WHERE login = 'player2'), 1, 12, CURRENT_TIMESTAMP - INTERVAL '2 days'),
                                                                 ((SELECT id FROM hmk.users WHERE login = 'player3'), 1, 8, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
                                                                 ((SELECT id FROM hmk.users WHERE login = 'player1'), 1, 3, CURRENT_TIMESTAMP) -- Player 1 got better!
;