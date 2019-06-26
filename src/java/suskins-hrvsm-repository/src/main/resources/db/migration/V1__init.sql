CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

CREATE TABLE public.tweets (
  id bigint PRIMARY KEY NOT NULL,
	tweet_id bigint NOT NULL,
    user_name text  NOT NULL,
	tweet text NOT NULL,
	processed_tweet text NOT NULL,
	probability decimal NOT NULL,
	country_code varchar(3) NOT NULL,
	created_at TIMESTAMP with time zone NOT NULL
);