-- File: 02-01-hmb.sql
-- Creates user-related tables for How Many Burgers! game

\set ON_ERROR_STOP on
\set ECHO all

-- Create the schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS hmb;

-- Basic user information for the game
CREATE TABLE IF NOT EXISTS hmb.usuario (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    correo VARCHAR(255)
);

-- Indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_usuario_login ON hmb.usuario(login);
CREATE INDEX IF NOT EXISTS idx_usuario_correo ON hmb.usuario(correo);

-- Game information
CREATE TABLE IF NOT EXISTS hmb.juego (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Game records/scores
CREATE TABLE IF NOT EXISTS hmb.records (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL REFERENCES hmb.usuario(id) ON DELETE CASCADE,
    juego_id INTEGER NOT NULL REFERENCES hmb.juego(id) ON DELETE CASCADE,
    puntaje INTEGER NOT NULL,
    fecha TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Insert default game
INSERT INTO hmb.juego (nombre) VALUES ('How Many Burgers') ON CONFLICT DO NOTHING;

-- Indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_records_usuario_id ON hmb.records(usuario_id);
CREATE INDEX IF NOT EXISTS idx_records_juego_id ON hmb.records(juego_id);
CREATE INDEX IF NOT EXISTS idx_records_puntaje ON hmb.records(puntaje ASC);
CREATE INDEX IF NOT EXISTS idx_records_fecha ON hmb.records(fecha);
