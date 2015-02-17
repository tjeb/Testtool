
CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 536 (class 1247 OID 24654)
-- Name: user_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE user_type AS ENUM (
  'lite',
  'full'
);


ALTER TYPE public.user_type OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 90113)
-- Name: accessPoints; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "accessPoints" (
  id bigint NOT NULL,
  url text NOT NULL,
  "userId" bigint NOT NULL,
  "addedTime" timestamp with time zone DEFAULT now() NOT NULL,
  name text DEFAULT '[no_description]'::text NOT NULL,
  "fileId" bigint,
  description text,
  "technicalUrl" text,
  "contactEmail" text,
  "usedForGalaxyGateway" boolean DEFAULT false NOT NULL
);


ALTER TABLE public."accessPoints" OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 90240)
-- Name: accessPointConfigs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "accessPointConfigs_id_seq"
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public."accessPointConfigs_id_seq" OWNER TO postgres;

--
-- TOC entry 2090 (class 0 OID 0)
-- Dependencies: 177
-- Name: accessPointConfigs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "accessPointConfigs_id_seq" OWNED BY "accessPoints".id;


--
-- TOC entry 171 (class 1259 OID 90123)
-- Name: certificateFiles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "certificateFiles" (
  id bigint NOT NULL,
  "userId" bigint NOT NULL,
  "accessPointId" bigint NOT NULL,
  "fileName" text NOT NULL,
  "addedTime" timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public."certificateFiles" OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 90242)
-- Name: certificateFiles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "certificateFiles_id_seq"
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public."certificateFiles_id_seq" OWNER TO postgres;

--
-- TOC entry 2091 (class 0 OID 0)
-- Dependencies: 178
-- Name: certificateFiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "certificateFiles_id_seq" OWNED BY "certificateFiles".id;


--
-- TOC entry 172 (class 1259 OID 90132)
-- Name: fileTypes; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "fileTypes" (
  id bigint NOT NULL,
  name text NOT NULL,
  "addedTime" timestamp with time zone DEFAULT now() NOT NULL,
  identifier text
);


ALTER TABLE public."fileTypes" OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 90244)
-- Name: fileTypes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "fileTypes_id_seq"
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public."fileTypes_id_seq" OWNER TO postgres;

--
-- TOC entry 2092 (class 0 OID 0)
-- Dependencies: 179
-- Name: fileTypes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "fileTypes_id_seq" OWNED BY "fileTypes".id;


--
-- TOC entry 185 (class 1259 OID 98339)
-- Name: gParticipantIdRef; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "gParticipantIdRef" (
  id bigint NOT NULL,
  "userId" bigint NOT NULL,
  "participantIdValue" text,
  "endpointId" integer
);


ALTER TABLE public."gParticipantIdRef" OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 98337)
-- Name: gEndpointRef_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "gEndpointRef_id_seq"
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public."gEndpointRef_id_seq" OWNER TO postgres;

--
-- TOC entry 2093 (class 0 OID 0)
-- Dependencies: 184
-- Name: gEndpointRef_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "gEndpointRef_id_seq" OWNED BY "gParticipantIdRef".id;


--
-- TOC entry 186 (class 1259 OID 98599)
-- Name: galaxyMetadataProfiles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "galaxyMetadataProfiles" (
  "profileId" bigint NOT NULL,
  base64 text NOT NULL,
  "fileTypeId" bigint NOT NULL
);


ALTER TABLE public."galaxyMetadataProfiles" OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 98644)
-- Name: receivedMetadata; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "receivedMetadata" (
  "timeStamp" character varying(255) NOT NULL,
  "messageFileName" text NOT NULL,
  "messageIdentifier" character varying(255) NOT NULL,
  "channelIdentifier" text NOT NULL,
  "recipientIdentifier" character varying(255) NOT NULL,
  "senderIdentifier" character varying(255) NOT NULL,
  "documentIdentifier" text NOT NULL,
  "processIdentifier" text NOT NULL,
  remote text NOT NULL,
  "apPrincipal" text NOT NULL,
  id bigint NOT NULL
);


ALTER TABLE public."receivedMetadata" OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 98664)
-- Name: receivedMetadata_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "receivedMetadata_id_seq"
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public."receivedMetadata_id_seq" OWNER TO postgres;

--
-- TOC entry 2094 (class 0 OID 0)
-- Dependencies: 188
-- Name: receivedMetadata_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "receivedMetadata_id_seq" OWNED BY "receivedMetadata".id;


--
-- TOC entry 173 (class 1259 OID 90141)
-- Name: schemaFiles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "schemaFiles" (
  id bigint NOT NULL,
  "addedTime" timestamp with time zone DEFAULT now() NOT NULL,
  "fileName" character varying(255) NOT NULL,
  extension character varying(10) NOT NULL,
  "typeId" bigint NOT NULL,
  "userId" bigint NOT NULL,
  name text,
  marked boolean DEFAULT false NOT NULL
);


ALTER TABLE public."schemaFiles" OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 90246)
-- Name: schemafiles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE schemafiles_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.schemafiles_id_seq OWNER TO postgres;

--
-- TOC entry 2095 (class 0 OID 0)
-- Dependencies: 180
-- Name: schemafiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE schemafiles_id_seq OWNED BY "schemaFiles".id;


--
-- TOC entry 189 (class 1259 OID 115032)
-- Name: tmpPassword; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "tmpPassword" (
  "userId" bigint NOT NULL,
  password character varying(255) NOT NULL
);


ALTER TABLE public."tmpPassword" OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 90150)
-- Name: userFiles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "userFiles" (
  id bigint NOT NULL,
  name text NOT NULL,
  "fileName" character varying(32) NOT NULL,
  size integer NOT NULL,
  "userId" bigint NOT NULL,
  "typeId" integer,
  extension character varying(10) NOT NULL,
  "addedTime" timestamp with time zone DEFAULT now() NOT NULL,
  validated boolean DEFAULT false NOT NULL,
  "validationInfo" text,
  "receivedMetadataId" integer
);


ALTER TABLE public."userFiles" OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 90248)
-- Name: userFiles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "userFiles_id_seq"
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public."userFiles_id_seq" OWNER TO postgres;

--
-- TOC entry 2096 (class 0 OID 0)
-- Dependencies: 181
-- Name: userFiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "userFiles_id_seq" OWNED BY "userFiles".id;


--
-- TOC entry 175 (class 1259 OID 90160)
-- Name: userRoles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "userRoles" (
  "userId" bigint,
  authority character varying(20),
  id bigint NOT NULL,
  "addedTime" timestamp with time zone
);


ALTER TABLE public."userRoles" OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 90250)
-- Name: userRoles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "userRoles_id_seq"
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public."userRoles_id_seq" OWNER TO postgres;

--
-- TOC entry 2097 (class 0 OID 0)
-- Dependencies: 182
-- Name: userRoles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "userRoles_id_seq" OWNED BY "userRoles".id;


--
-- TOC entry 176 (class 1259 OID 90165)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
  id bigint NOT NULL,
  username character varying(20) NOT NULL,
  password character varying(32),
  name text NOT NULL,
  "companyName" text NOT NULL,
  enabled boolean NOT NULL,
  type character varying(10) NOT NULL,
  salt character varying(32) NOT NULL,
  "addedTime" timestamp with time zone DEFAULT now() NOT NULL,
  editable boolean DEFAULT true NOT NULL,
  identifier text,
  email character varying(150)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 90252)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 2098 (class 0 OID 0)
-- Dependencies: 183
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- TOC entry 1891 (class 2604 OID 98689)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "accessPoints" ALTER COLUMN id SET DEFAULT nextval('"accessPointConfigs_id_seq"'::regclass);


--
-- TOC entry 1895 (class 2604 OID 98690)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "certificateFiles" ALTER COLUMN id SET DEFAULT nextval('"certificateFiles_id_seq"'::regclass);


--
-- TOC entry 1897 (class 2604 OID 98691)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "fileTypes" ALTER COLUMN id SET DEFAULT nextval('"fileTypes_id_seq"'::regclass);


--
-- TOC entry 1909 (class 2604 OID 98692)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "gParticipantIdRef" ALTER COLUMN id SET DEFAULT nextval('"gEndpointRef_id_seq"'::regclass);


--
-- TOC entry 1910 (class 2604 OID 98693)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "receivedMetadata" ALTER COLUMN id SET DEFAULT nextval('"receivedMetadata_id_seq"'::regclass);


--
-- TOC entry 1899 (class 2604 OID 98694)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "schemaFiles" ALTER COLUMN id SET DEFAULT nextval('schemafiles_id_seq'::regclass);


--
-- TOC entry 1902 (class 2604 OID 98695)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "userFiles" ALTER COLUMN id SET DEFAULT nextval('"userFiles_id_seq"'::regclass);


--
-- TOC entry 1905 (class 2604 OID 98696)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "userRoles" ALTER COLUMN id SET DEFAULT nextval('"userRoles_id_seq"'::regclass);


--
-- TOC entry 1906 (class 2604 OID 98697)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);

--
-- TOC entry 1912 (class 2606 OID 90183)
-- Name: accesPointConfigs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "accessPoints"
ADD CONSTRAINT "accesPointConfigs_pkey" PRIMARY KEY (id);

--
-- TOC entry 1916 (class 2606 OID 90185)
-- Name: certificatefiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "certificateFiles"
ADD CONSTRAINT certificatefiles_pkey PRIMARY KEY (id);


--
-- TOC entry 1918 (class 2606 OID 90187)
-- Name: fileTypes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "fileTypes"
ADD CONSTRAINT "fileTypes_pkey" PRIMARY KEY (id);


--
-- TOC entry 1924 (class 2606 OID 90189)
-- Name: files_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "userFiles"
ADD CONSTRAINT files_pkey PRIMARY KEY (id);


--
-- TOC entry 1936 (class 2606 OID 98344)
-- Name: gEndpointRef_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "gParticipantIdRef"
ADD CONSTRAINT "gEndpointRef_pkey" PRIMARY KEY (id);


--
-- TOC entry 1940 (class 2606 OID 98606)
-- Name: galaxyMetadataProfiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "galaxyMetadataProfiles"
ADD CONSTRAINT "galaxyMetadataProfiles_pkey" PRIMARY KEY ("profileId");


--
-- TOC entry 1942 (class 2606 OID 98674)
-- Name: receivedMetadata_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "receivedMetadata"
ADD CONSTRAINT "receivedMetadata_pkey" PRIMARY KEY (id);


--
-- TOC entry 1922 (class 2606 OID 90191)
-- Name: schemafiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "schemaFiles"
ADD CONSTRAINT schemafiles_pkey PRIMARY KEY (id);


--
-- TOC entry 1928 (class 2606 OID 115038)
-- Name: unique_email; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
ADD CONSTRAINT unique_email UNIQUE (email);


--
-- TOC entry 1930 (class 2606 OID 90193)
-- Name: unique_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
ADD CONSTRAINT unique_id UNIQUE (id);


--
-- TOC entry 1920 (class 2606 OID 90195)
-- Name: unique_identifier; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "fileTypes"
ADD CONSTRAINT unique_identifier UNIQUE (identifier);


--
-- TOC entry 1914 (class 2606 OID 90197)
-- Name: unique_name; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "accessPoints"
ADD CONSTRAINT unique_name UNIQUE (name);


--
-- TOC entry 1938 (class 2606 OID 98420)
-- Name: unique_participantIdValue; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "gParticipantIdRef"
ADD CONSTRAINT "unique_participantIdValue" UNIQUE ("participantIdValue");


--
-- TOC entry 1944 (class 2606 OID 115036)
-- Name: unique_userId; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "tmpPassword"
ADD CONSTRAINT "unique_userId" UNIQUE ("userId");


--
-- TOC entry 1932 (class 2606 OID 90199)
-- Name: unique_username; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
ADD CONSTRAINT unique_username UNIQUE (username);


--
-- TOC entry 1926 (class 2606 OID 90201)
-- Name: userRoles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "userRoles"
ADD CONSTRAINT "userRoles_pkey" PRIMARY KEY (id);


--
-- TOC entry 1934 (class 2606 OID 90203)
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 1945 (class 2606 OID 90204)
-- Name: accessPointConfigs_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "accessPoints"
ADD CONSTRAINT "accessPointConfigs_userId_fkey" FOREIGN KEY ("userId") REFERENCES users(id);


--
-- TOC entry 1946 (class 2606 OID 90268)
-- Name: accessPoints_fileId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "accessPoints"
ADD CONSTRAINT "accessPoints_fileId_fkey" FOREIGN KEY ("fileId") REFERENCES "certificateFiles"(id);


--
-- TOC entry 1947 (class 2606 OID 90209)
-- Name: certificatefiles_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "certificateFiles"
ADD CONSTRAINT certificatefiles_userid_fkey FOREIGN KEY ("userId") REFERENCES users(id);


--
-- TOC entry 1950 (class 2606 OID 90214)
-- Name: files_typeId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "userFiles"
ADD CONSTRAINT "files_typeId_fkey" FOREIGN KEY ("typeId") REFERENCES "fileTypes"(id);


--
-- TOC entry 1951 (class 2606 OID 90219)
-- Name: files_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "userFiles"
ADD CONSTRAINT "files_userId_fkey" FOREIGN KEY ("userId") REFERENCES users(id);


--
-- TOC entry 1954 (class 2606 OID 98345)
-- Name: gEndpointRef_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "gParticipantIdRef"
ADD CONSTRAINT "gEndpointRef_userId_fkey" FOREIGN KEY ("userId") REFERENCES users(id);


--
-- TOC entry 1948 (class 2606 OID 90224)
-- Name: schemafiles_typeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "schemaFiles"
ADD CONSTRAINT schemafiles_typeid_fkey FOREIGN KEY ("typeId") REFERENCES "fileTypes"(id);


--
-- TOC entry 1949 (class 2606 OID 90229)
-- Name: schemafiles_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "schemaFiles"
ADD CONSTRAINT schemafiles_userid_fkey FOREIGN KEY ("userId") REFERENCES users(id);


--
-- TOC entry 1952 (class 2606 OID 98675)
-- Name: userFiles_receivedMetadataId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "userFiles"
ADD CONSTRAINT "userFiles_receivedMetadataId_fkey" FOREIGN KEY ("receivedMetadataId") REFERENCES "receivedMetadata"(id);


--
-- TOC entry 1953 (class 2606 OID 90234)
-- Name: userRoles_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "userRoles"
ADD CONSTRAINT "userRoles_userId_fkey" FOREIGN KEY ("userId") REFERENCES users(id);
