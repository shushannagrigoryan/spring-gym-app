-- init-multiple-databases-and-users.sql

\connect postgres

-- Create database gym_db if it does not exist
SELECT 'CREATE DATABASE gym_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'gym_db')
\gexec

-- Create database gym_local_db if it does not exist
SELECT 'CREATE DATABASE gym_local_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'gym_local_db')
\gexec

-- Create database gym_prod_db if it does not exist
SELECT 'CREATE DATABASE gym_prod_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'gym_prod_db')
\gexec

-- Create database gym_stg_db if it does not exist
SELECT 'CREATE DATABASE gym_stg_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'gym_stg_db')
\gexec

-- Create database gym_test_db if it does not exist
SELECT 'CREATE DATABASE gym_test_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'gym_test_db')
\gexec

-- Create users if they do not exist
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'postgres') THEN
            CREATE USER postgres WITH PASSWORD 'postgres';
        END IF;

        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'local_user') THEN
            CREATE USER local_user WITH PASSWORD 'local_pass';
        END IF;

        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'prod_user') THEN
            CREATE USER prod_user WITH PASSWORD 'prod_pass';
        END IF;

        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'stg_user') THEN
            CREATE USER stg_user WITH PASSWORD 'stg_pass';
        END IF;

        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'test_user') THEN
            CREATE USER test_user WITH PASSWORD 'test_pass';
        END IF;
    END
$$;

-- Grant usage on schema public to users
\connect gym_db
GRANT USAGE ON SCHEMA public TO postgres;
GRANT CREATE ON SCHEMA public TO postgres;

\connect gym_local_db
GRANT USAGE ON SCHEMA public TO local_user;
GRANT CREATE ON SCHEMA public TO local_user;

\connect gym_prod_db
GRANT USAGE ON SCHEMA public TO prod_user;
GRANT CREATE ON SCHEMA public TO prod_user;

\connect gym_stg_db
GRANT USAGE ON SCHEMA public TO stg_user;
GRANT CREATE ON SCHEMA public TO stg_user;

\connect gym_test_db
GRANT USAGE ON SCHEMA public TO test_user;
GRANT CREATE ON SCHEMA public TO test_user;