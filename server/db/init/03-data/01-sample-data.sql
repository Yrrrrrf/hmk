-- File: sample-data.sql
-- Sample user data and scores for the number guessing game

\set ON_ERROR_STOP on
\set ECHO all

-- Insert sample users
INSERT INTO hmb.usuario (login, password, correo) VALUES 
    ('player1', 'pass1', 'player1@example.com'),
    ('player2', 'pass2', 'player2@example.com'),
    ('player3', 'pass3', 'player3@example.com'),
    ('player4', 'pass4', 'player4@example.com')
ON CONFLICT (login) DO NOTHING;

-- Insert sample scores for the default game (id=1)
-- These represent number of attempts (lower is better)
INSERT INTO hmb.records (usuario_id, juego_id, puntaje, fecha) VALUES 
    ((SELECT id FROM hmb.usuario WHERE login = 'player1'), 1, 5, CURRENT_TIMESTAMP - INTERVAL '1 day'),
    ((SELECT id FROM hmb.usuario WHERE login = 'player2'), 1, 8, CURRENT_TIMESTAMP - INTERVAL '2 days'),
    ((SELECT id FROM hmb.usuario WHERE login = 'player3'), 1, 12, CURRENT_TIMESTAMP - INTERVAL '3 days'),
    ((SELECT id FROM hmb.usuario WHERE login = 'player4'), 1, 3, CURRENT_TIMESTAMP - INTERVAL '1 day'),
    ((SELECT id FROM hmb.usuario WHERE login = 'player1'), 1, 7, CURRENT_TIMESTAMP - INTERVAL '4 days'),
    ((SELECT id FROM hmb.usuario WHERE login = 'player2'), 1, 4, CURRENT_TIMESTAMP - INTERVAL '2 days'),
    ((SELECT id FROM hmb.usuario WHERE login = 'player3'), 1, 10, CURRENT_TIMESTAMP - INTERVAL '3 days')
ON CONFLICT DO NOTHING;