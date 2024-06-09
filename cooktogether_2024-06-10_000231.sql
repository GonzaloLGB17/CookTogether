--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: actualizar_puntuacion_media(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.actualizar_puntuacion_media() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Actualiza la puntuación media de la receta
    UPDATE recetas
    SET puntuacion_media = (
        SELECT AVG(puntuacion)
        FROM valoraciones
        WHERE receta_id = NEW.receta_id
    )
    WHERE id = NEW.receta_id;
    
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.actualizar_puntuacion_media() OWNER TO postgres;

--
-- Name: actualizar_puntuacion_media(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.actualizar_puntuacion_media(p_receta_id integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Actualiza la puntuación media de la receta
    UPDATE recetas
    SET puntuacion_media = (
        SELECT AVG(puntuacion)
        FROM valoraciones
        WHERE receta_id = p_receta_id
    )
    WHERE id = p_receta_id;
END;
$$;


ALTER FUNCTION public.actualizar_puntuacion_media(p_receta_id integer) OWNER TO postgres;

--
-- Name: agregar_usuario(character varying, character varying, character varying, character varying, character varying, bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.agregar_usuario(p_nombre character varying, p_apellidos character varying, p_correo character varying, p_username character varying, p_password character varying, p_foto_usuario bytea) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_hash text;
    v_key INTEGER;
BEGIN

    IF EXISTS (
        SELECT 1
        FROM usuarios
        WHERE username = p_username
    ) THEN
        RETURN 1;
    ELSIF EXISTS (
        SELECT 1
        FROM usuarios
        WHERE correo = p_correo
    ) THEN
        RETURN 2;
    ELSE
        INSERT INTO usuarios (nombre, apellidos, correo, username, password, foto_usuario)
        VALUES (p_nombre, p_apellidos, p_correo, p_username, p_password, p_foto_usuario)
        RETURNING id INTO v_key;

        SELECT encode(digest(p_password, 'sha512'), 'hex') INTO v_hash;
        UPDATE usuarios SET password = v_hash WHERE username = p_username;
        RETURN 0;
    END IF;
END;
$$;


ALTER FUNCTION public.agregar_usuario(p_nombre character varying, p_apellidos character varying, p_correo character varying, p_username character varying, p_password character varying, p_foto_usuario bytea) OWNER TO postgres;

--
-- Name: agregar_usuario(character varying, character varying, character varying, character varying, character varying, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.agregar_usuario(p_nombre character varying, p_apellidos character varying, p_correo character varying, p_username character varying, p_password character varying, p_puntuacion double precision) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_hash text;
    v_key INTEGER;
BEGIN

    IF EXISTS (
        SELECT 1
        FROM usuarios
        WHERE username = p_username
    ) THEN
        RETURN 1;
    ELSIF EXISTS (
        SELECT 1
        FROM usuarios
        WHERE correo = p_correo
    ) THEN
        RETURN 2;
    ELSE
        INSERT INTO usuarios (nombre, apellidos, correo, username, password, puntuacion)
        VALUES (p_nombre, p_apellidos, p_correo, p_username, p_password, p_puntuacion)
        RETURNING id INTO v_key;

        SELECT encode(digest(p_password, 'sha512'), 'hex') INTO v_hash;
        UPDATE usuarios SET password = v_hash WHERE username = p_username;
        RETURN 0;
    END IF;
END;
$$;


ALTER FUNCTION public.agregar_usuario(p_nombre character varying, p_apellidos character varying, p_correo character varying, p_username character varying, p_password character varying, p_puntuacion double precision) OWNER TO postgres;

--
-- Name: agregar_usuario(character varying, character varying, character varying, character varying, character varying, double precision, bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.agregar_usuario(p_nombre character varying, p_apellidos character varying, p_correo character varying, p_username character varying, p_password character varying, p_puntuacion double precision, p_foto_usuario bytea) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_hash text;
    v_key INTEGER;
BEGIN

    IF EXISTS (
        SELECT 1
        FROM usuarios
        WHERE username = p_username
    ) THEN
        RETURN 1;
    ELSIF EXISTS (
        SELECT 1
        FROM usuarios
        WHERE correo = p_correo
    ) THEN
        RETURN 2;
    ELSE
        INSERT INTO usuarios (nombre, apellidos, correo, username, password, puntuacion, foto_usuario)
        VALUES (p_nombre, p_apellidos, p_correo, p_username, p_password, p_puntuacion, p_foto_usuario)
        RETURNING id INTO v_key;

        SELECT encode(digest(p_password, 'sha512'), 'hex') INTO v_hash;
        UPDATE usuarios SET password = v_hash WHERE username = p_username;
        RETURN 0;
    END IF;
END;
$$;


ALTER FUNCTION public.agregar_usuario(p_nombre character varying, p_apellidos character varying, p_correo character varying, p_username character varying, p_password character varying, p_puntuacion double precision, p_foto_usuario bytea) OWNER TO postgres;

--
-- Name: iniciar_sesion(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.iniciar_sesion(p_usuario_email character varying, p_passwd character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_hash text;
    v_nombre_usuario varchar;
BEGIN
    SELECT
        u.password,
        u.nombre
    INTO
        v_hash,
        v_nombre_usuario
    FROM
        usuarios u
    WHERE
        u.username = p_usuario_email OR
        u.correo = p_usuario_email;

    IF NOT FOUND THEN
        RETURN 1;
    END IF;

    IF encode(digest(p_passwd, 'sha512'), 'hex') = v_hash THEN
        RETURN 0;
    ELSE
        RETURN 2;
    END IF;
END;
$$;


ALTER FUNCTION public.iniciar_sesion(p_usuario_email character varying, p_passwd character varying) OWNER TO postgres;

--
-- Name: insertar_usuario(character varying, character varying, character varying, character varying, character varying, bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.insertar_usuario(p_nombre character varying, p_apellidos character varying, p_correo character varying, p_username character varying, p_password character varying, p_foto_usuario bytea) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_hash text;
    v_key INTEGER;
BEGIN

    IF EXISTS (
        SELECT 1
        FROM usuarios
        WHERE username = p_username
    ) THEN
        RETURN 1;
    ELSIF EXISTS (
        SELECT 1
        FROM usuarios
        WHERE correo = p_correo
    ) THEN
        RETURN 2;
    ELSE
        INSERT INTO usuarios (nombre, apellidos, correo, username, password, foto_usuario)
        VALUES (p_nombre, p_apellidos, p_correo, p_username, p_password, p_foto_usuario)
        RETURNING id INTO v_key;

        SELECT encode(digest(p_password, 'sha512'), 'hex') INTO v_hash;
        UPDATE usuarios SET password = v_hash WHERE username = p_username;
        RETURN 0;
    END IF;
END;
$$;


ALTER FUNCTION public.insertar_usuario(p_nombre character varying, p_apellidos character varying, p_correo character varying, p_username character varying, p_password character varying, p_foto_usuario bytea) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: recetas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recetas (
    id integer NOT NULL,
    titulo character varying(255),
    descripcion text,
    ingredientes text,
    instrucciones text,
    usuario character varying(100),
    foto_receta bytea,
    categoria character varying(100),
    fecha_hora timestamp without time zone,
    puntuacion_media double precision
);


ALTER TABLE public.recetas OWNER TO postgres;

--
-- Name: recetas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.recetas_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.recetas_id_seq OWNER TO postgres;

--
-- Name: recetas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.recetas_id_seq OWNED BY public.recetas.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id integer NOT NULL,
    nombre character varying(100),
    apellidos character varying(100),
    correo character varying(100),
    username character varying(100),
    password character varying(128),
    foto_usuario bytea
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuarios_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_id_seq OWNER TO postgres;

--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- Name: valoraciones; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.valoraciones (
    usuario_id integer NOT NULL,
    receta_id integer NOT NULL,
    puntuacion double precision NOT NULL
);


ALTER TABLE public.valoraciones OWNER TO postgres;

--
-- Name: recetas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recetas ALTER COLUMN id SET DEFAULT nextval('public.recetas_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Name: recetas recetas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recetas
    ADD CONSTRAINT recetas_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_correo_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_correo_key UNIQUE (correo);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_username_key UNIQUE (username);


--
-- Name: valoraciones valoraciones_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.valoraciones
    ADD CONSTRAINT valoraciones_pkey PRIMARY KEY (usuario_id, receta_id);


--
-- Name: valoraciones_usuario_id_receta_id_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX valoraciones_usuario_id_receta_id_key ON public.valoraciones USING btree (usuario_id, receta_id);


--
-- Name: recetas recetas_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recetas
    ADD CONSTRAINT recetas_usuario_fkey FOREIGN KEY (usuario) REFERENCES public.usuarios(username) ON UPDATE CASCADE;


--
-- Name: recetas recetas_usuarios_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recetas
    ADD CONSTRAINT recetas_usuarios_fkey FOREIGN KEY (usuario) REFERENCES public.usuarios(username) ON UPDATE CASCADE;


--
-- Name: recetas usuarios_username_key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recetas
    ADD CONSTRAINT usuarios_username_key FOREIGN KEY (usuario) REFERENCES public.usuarios(username) ON UPDATE CASCADE;


--
-- Name: valoraciones valoraciones_receta_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.valoraciones
    ADD CONSTRAINT valoraciones_receta_id_fkey FOREIGN KEY (receta_id) REFERENCES public.recetas(id);


--
-- Name: valoraciones valoraciones_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.valoraciones
    ADD CONSTRAINT valoraciones_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- PostgreSQL database dump complete
--

