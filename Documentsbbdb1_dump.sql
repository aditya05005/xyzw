--
-- PostgreSQL database dump
--

\restrict UaoblEDPgmtArTOqaGLBwM80eWdCmEFKMEtaTW0goRImtpku3a38vpMOc8hXdMg

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: all_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.all_requests (
    request_id integer NOT NULL,
    request_date date NOT NULL,
    blood_group character varying(5) NOT NULL,
    volume_requested integer NOT NULL,
    hospital_id integer NOT NULL,
    CONSTRAINT all_requests_blood_group_check CHECK (((blood_group)::text = ANY ((ARRAY['A+'::character varying, 'A-'::character varying, 'B+'::character varying, 'B-'::character varying, 'AB+'::character varying, 'AB-'::character varying, 'O+'::character varying, 'O-'::character varying])::text[]))),
    CONSTRAINT all_requests_volume_requested_check CHECK ((volume_requested > 0))
);


ALTER TABLE public.all_requests OWNER TO postgres;

--
-- Name: all_requests_request_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.all_requests_request_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.all_requests_request_id_seq OWNER TO postgres;

--
-- Name: all_requests_request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.all_requests_request_id_seq OWNED BY public.all_requests.request_id;


--
-- Name: blood; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.blood (
    blood_id integer NOT NULL,
    donation_date date NOT NULL,
    volume integer NOT NULL,
    status character varying(20) DEFAULT 'Available'::character varying,
    donor_id integer NOT NULL,
    blood_group character varying(5) NOT NULL,
    CONSTRAINT blood_blood_group_check CHECK (((blood_group)::text = ANY ((ARRAY['A+'::character varying, 'A-'::character varying, 'B+'::character varying, 'B-'::character varying, 'AB+'::character varying, 'AB-'::character varying, 'O+'::character varying, 'O-'::character varying])::text[]))),
    CONSTRAINT blood_status_check CHECK (((status)::text = ANY ((ARRAY['Available'::character varying, 'Used'::character varying, 'Expired'::character varying, 'Reserved'::character varying])::text[]))),
    CONSTRAINT blood_volume_check CHECK ((volume > 0))
);


ALTER TABLE public.blood OWNER TO postgres;

--
-- Name: blood_blood_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.blood_blood_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.blood_blood_id_seq OWNER TO postgres;

--
-- Name: blood_blood_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.blood_blood_id_seq OWNED BY public.blood.blood_id;


--
-- Name: donor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.donor (
    donor_id integer NOT NULL,
    name character varying(100) NOT NULL,
    gender character varying(10),
    dob date NOT NULL,
    phone_number character varying(15) NOT NULL,
    apartment_number character varying(20),
    street character varying(100),
    city character varying(50),
    blood_group character varying(5) NOT NULL,
    CONSTRAINT donor_blood_group_check CHECK (((blood_group)::text = ANY ((ARRAY['A+'::character varying, 'A-'::character varying, 'B+'::character varying, 'B-'::character varying, 'AB+'::character varying, 'AB-'::character varying, 'O+'::character varying, 'O-'::character varying])::text[]))),
    CONSTRAINT donor_gender_check CHECK (((gender)::text = ANY ((ARRAY['Male'::character varying, 'Female'::character varying, 'Other'::character varying])::text[])))
);


ALTER TABLE public.donor OWNER TO postgres;

--
-- Name: donor_donor_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.donor_donor_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.donor_donor_id_seq OWNER TO postgres;

--
-- Name: donor_donor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.donor_donor_id_seq OWNED BY public.donor.donor_id;


--
-- Name: hospital; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hospital (
    hospital_id integer NOT NULL,
    name character varying(100) NOT NULL,
    contact character varying(15) NOT NULL,
    location character varying(100) NOT NULL,
    billing_code character varying(20)
);


ALTER TABLE public.hospital OWNER TO postgres;

--
-- Name: hospital_hospital_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hospital_hospital_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.hospital_hospital_id_seq OWNER TO postgres;

--
-- Name: hospital_hospital_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.hospital_hospital_id_seq OWNED BY public.hospital.hospital_id;


--
-- Name: recipient; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recipient (
    recipient_id integer NOT NULL,
    name character varying(100) NOT NULL,
    blood_group character varying(5) NOT NULL,
    phone_number character varying(15) NOT NULL,
    dob date NOT NULL,
    address character varying(200) NOT NULL,
    hospital_id integer NOT NULL,
    CONSTRAINT recipient_blood_group_check CHECK (((blood_group)::text = ANY ((ARRAY['A+'::character varying, 'A-'::character varying, 'B+'::character varying, 'B-'::character varying, 'AB+'::character varying, 'AB-'::character varying, 'O+'::character varying, 'O-'::character varying])::text[])))
);


ALTER TABLE public.recipient OWNER TO postgres;

--
-- Name: recipient_recipient_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.recipient_recipient_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.recipient_recipient_id_seq OWNER TO postgres;

--
-- Name: recipient_recipient_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.recipient_recipient_id_seq OWNED BY public.recipient.recipient_id;


--
-- Name: request_fulfillment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.request_fulfillment (
    request_id integer NOT NULL,
    blood_id integer NOT NULL,
    fulfillment_date date NOT NULL
);


ALTER TABLE public.request_fulfillment OWNER TO postgres;

--
-- Name: all_requests request_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.all_requests ALTER COLUMN request_id SET DEFAULT nextval('public.all_requests_request_id_seq'::regclass);


--
-- Name: blood blood_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blood ALTER COLUMN blood_id SET DEFAULT nextval('public.blood_blood_id_seq'::regclass);


--
-- Name: donor donor_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.donor ALTER COLUMN donor_id SET DEFAULT nextval('public.donor_donor_id_seq'::regclass);


--
-- Name: hospital hospital_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hospital ALTER COLUMN hospital_id SET DEFAULT nextval('public.hospital_hospital_id_seq'::regclass);


--
-- Name: recipient recipient_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recipient ALTER COLUMN recipient_id SET DEFAULT nextval('public.recipient_recipient_id_seq'::regclass);


--
-- Data for Name: all_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.all_requests (request_id, request_date, blood_group, volume_requested, hospital_id) FROM stdin;
3	2025-10-10	O+	500	1
4	2025-10-10	A+	500	1
2	2025-10-10	O+	500	1
5	2025-10-12	A+	500	1
6	2025-10-12	O+	800	1
\.


--
-- Data for Name: blood; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.blood (blood_id, donation_date, volume, status, donor_id, blood_group) FROM stdin;
945762	2025-10-12	100	Available	4	AB+
695381	2025-10-11	150	Available	4	AB+
332004	2025-10-09	700	Used	2	O+
233393	2025-10-09	450	Used	2	O+
462640	2024-10-10	400	Available	43	AB+
243386	2025-10-13	400	Available	44	O+
\.


--
-- Data for Name: donor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.donor (donor_id, name, gender, dob, phone_number, apartment_number, street, city, blood_group) FROM stdin;
1	Anita Singh	Female	1990-04-21	8887776665	A-12	MG Road	Mumbai	O+
2	Rittam Mondal	Male	2006-04-25	1234567890	neeraj apt	M. G. road	Thane	O+
3	Aryaman Sinha	Male	2006-07-11	4455667788	13/2	g.g. road	Mumbai	AB+
4	Ameya Inamdar	Male	2006-10-01	1122334455	12/3	S.G. Barve 	Mumbai	AB+
6	Ravi Kumar	Male	1985-07-15	9876543210	B-203	 Linking Road	Delhi	A+
7	Priya Sharma	Female	1992-11-30	9123456789	\N	Anna Salai	Chennai	B+
8	Amit Patel	Male	1988-03-12	9001234567	C-45	Baner Road	Pune	AB+
9	Sneha Gupta	Female	1995-09-25	9112233445	D-101	Park Street	Kolkata	O-
10	Vikram Rao	Male	1978-01-08	9223344556	\N	Jubilee Hills	Hyderabad	A-
11	Neha Joshi	Female	1993-06-17	9334455667	E-56	Camp Road	Bangalore	B-
12	Suresh Menon	Male	1982-12-03	9445566778	F-78	Gandhi Nagar	Ahmedabad	AB-
13	Kavita Reddy	Female	1990-02-14	9556677889	\N	Moti Nagar	Jaipur	O+
14	Rahul Verma	Male	1987-05-22	9667788990	G-23	Hazratganj	Lucknow	A+
15	Meena Iyer	Female	1991-08-09	9778899001	H-12	Sector 17	Chandigarh	B+
16	Arjun Singh	Male	1980-10-27	9889900112	\N	DLF Phase 2	Gurgaon	O-
17	Pooja Desai	Female	1994-04-05	9990011223	I-34	Sector 62	Noida	AB+
18	Kiran Malhotra	Other	1989-07-19	9001122334	J-89	Kolar Road	Bhopal	A-
19	Sanjay Yadav	Male	1975-03-31	9112233446	\N	Ashok Nagar	Patna	B-
20	Lata Nair	Female	1996-11-11	9223344557	K-15	Adajan	Surat	O+
21	Rohit Jain	Male	1984-02-28	9334455668	L-67	Ram Nagar	Nagpur	AB-
22	Anjali Shah	Female	1997-06-04	9445566779	\N	Kalyan Nagar	Thane	A+
23	Manish Thakur	Male	1983-09-13	9556677890	M-22	Alkapuri	Vadodara	B+
24	Rekha Pillai	Female	1990-12-20	9667789001	N-45	Peelamedu	Coimbatore	O-
25	Vivek Dubey	Male	1986-01-15	9778890112	\N	Kolar Road	Indore	AB+
26	Shalini Roy	Female	1992-03-07	9889901223	O-78	Beach Road	Visakhapatnam	A-
27	Nikhil Bose	Male	1979-05-29	9990012334	P-12	Mall Road	Kanpur	B-
28	Tanya Mehra	Female	1995-08-16	9001123445	\N	Sector 15	Faridabad	O+
29	Deepak Saxena	Male	1981-10-02	9112234556	Q-34	Sector 18	Ghaziabad	A+
30	Sunita Rani	Female	1993-04-23	9223345667	R-56	GT Road	Ludhiana	B+
31	Ajay Mishra	Male	1987-07-09	9334456778	\N	Civil Lines	Agra	O-
32	Ritu Kapoor	Female	1990-11-18	9445567889	S-78	Mall Road	Amritsar	AB+
33	Vinod Sharma	Male	1977-02-25	9556679000	T-23	Connaught Place	Delhi	A-
34	Preeti Nair	Female	1994-06-12	9667780111	\N	MG Road	Bangalore	B-
35	Kunal Das	Male	1988-09-30	9778891222	U-45	Bandra West	Mumbai	O+
36	Shweta Rao	Female	1991-12-07	9889902333	V-67	T Nagar	Chennai	AB-
37	Manoj Sen	Male	1985-03-14	9990013444	\N	Park Street	Kolkata	A+
38	Divya Reddy	Female	1996-05-26	9001124555	W-12	Banjara Hills	Hyderabad	B+
39	Siddharth Jain	Male	1980-08-08	9112235666	X-34	Koregaon Park	Pune	O-
40	Nisha Patel	Female	1993-01-19	9223346777	\N	SG Highway	Ahmedabad	AB+
41	Rakesh Kumar	Male	1976-04-11	9334457888	Y-56	Tonk Road	Jaipur	A-
42	Anu Sharma	Female	1990-07-28	9445568999	Z-78	Gomti Nagar	Lucknow	B-
43	Tarun Gill	Male	1984-10-15	9556679111	\N	Sector 8	Chandigarh	O+
44	Mrinal Sirwani	Male	2017-10-01	4477889966	20/2	g.n road	Thane	O+
\.


--
-- Data for Name: hospital; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hospital (hospital_id, name, contact, location, billing_code) FROM stdin;
1	City Hospital	9876543210	Delhi	B001
2	KEM Hospital	5544332211	Mumbai	B002
4	Sunrise Medical Center	8765432109	Chennai	B003
5	Apollo Hospital	9001234567	Bangalore	B004
6	Fortis Healthcare	9112233445	Kolkata	B005
7	Medanta Hospital	9223344556	Hyderabad	B006
8	Star Hospital	9334455667	Pune	B007
9	Rainbow Medical	9445566778	Ahmedabad	B008
10	Global Health Clinic	9556677889	Jaipur	B009
11	Metro Hospital	9667788990	Lucknow	B010
12	Care Hospital	9778899001	Chandigarh	B011
13	Max Healthcare	9889900112	Gurgaon	B012
14	Lifeline Hospital	9990011223	Noida	B013
15	Unity Medical Center	9001122334	Bhopal	B014
16	Hope Hospital	9112233446	Patna	B015
17	Sai Medical Institute	9223344557	Surat	B016
18	Vijaya Hospital	9334455668	Nagpur	B017
19	Jupiter Hospital	9445566779	Thane	B018
20	Pearl Health Center	9556677890	Vadodara	B019
21	Narayana Health	9667789001	Coimbatore	B020
22	Aster Hospital	9778890112	Indore	B021
23	Shanti Medical	9889901223	Visakhapatnam	B022
24	Elite Hospital	9990012334	Kanpur	B023
25	Prime Care Hospital	9001123445	Faridabad	B024
26	Bliss Medical Center	9112234556	Ghaziabad	B025
27	Surya Hospital	9223345667	Ludhiana	B026
28	Harmony Health	9334456778	Agra	B027
29	Vivek Hospital	9445567889	Amritsar	B028
30	Safdarjung Hospital	9556679000	Delhi	B029
31	Manipal Hospital	9667780111	Bangalore	B030
32	Holy Cross Hospital	9778891222	Mumbai	B031
33	Lotus Medical	9889902333	Chennai	B032
34	Evergreen Hospital	9990013444	Kolkata	B033
35	Silverline Clinic	9001124555	Hyderabad	B034
36	Sunshine Hospital	9112235666	Pune	B035
37	City Care Hospital	9223346777	Ahmedabad	B036
38	Golden Health	9334457888	Jaipur	B037
39	Relief Hospital	9445568999	Lucknow	B038
40	Trinity Medical	9556679111	Chandigarh	B039
41	Omega Hospital	9667780222	Gurgaon	B040
\.


--
-- Data for Name: recipient; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recipient (recipient_id, name, blood_group, phone_number, dob, address, hospital_id) FROM stdin;
1	Rahul Sharma	A+	9998887776	1995-06-12	123 Green Street, Delhi	1
\.


--
-- Data for Name: request_fulfillment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.request_fulfillment (request_id, blood_id, fulfillment_date) FROM stdin;
3	233393	2025-10-12
\.


--
-- Name: all_requests_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.all_requests_request_id_seq', 6, true);


--
-- Name: blood_blood_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.blood_blood_id_seq', 1, true);


--
-- Name: donor_donor_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.donor_donor_id_seq', 44, true);


--
-- Name: hospital_hospital_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hospital_hospital_id_seq', 41, true);


--
-- Name: recipient_recipient_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.recipient_recipient_id_seq', 1, true);


--
-- Name: all_requests all_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.all_requests
    ADD CONSTRAINT all_requests_pkey PRIMARY KEY (request_id);


--
-- Name: blood blood_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blood
    ADD CONSTRAINT blood_pkey PRIMARY KEY (blood_id);


--
-- Name: donor donor_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.donor
    ADD CONSTRAINT donor_phone_number_key UNIQUE (phone_number);


--
-- Name: donor donor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.donor
    ADD CONSTRAINT donor_pkey PRIMARY KEY (donor_id);


--
-- Name: hospital hospital_billing_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hospital
    ADD CONSTRAINT hospital_billing_code_key UNIQUE (billing_code);


--
-- Name: hospital hospital_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hospital
    ADD CONSTRAINT hospital_pkey PRIMARY KEY (hospital_id);


--
-- Name: recipient recipient_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recipient
    ADD CONSTRAINT recipient_phone_number_key UNIQUE (phone_number);


--
-- Name: recipient recipient_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recipient
    ADD CONSTRAINT recipient_pkey PRIMARY KEY (recipient_id);


--
-- Name: request_fulfillment request_fulfillment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_fulfillment
    ADD CONSTRAINT request_fulfillment_pkey PRIMARY KEY (request_id, blood_id);


--
-- Name: all_requests all_requests_hospital_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.all_requests
    ADD CONSTRAINT all_requests_hospital_id_fkey FOREIGN KEY (hospital_id) REFERENCES public.hospital(hospital_id) ON DELETE CASCADE;


--
-- Name: blood blood_donor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blood
    ADD CONSTRAINT blood_donor_id_fkey FOREIGN KEY (donor_id) REFERENCES public.donor(donor_id) ON DELETE CASCADE;


--
-- Name: recipient recipient_hospital_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recipient
    ADD CONSTRAINT recipient_hospital_id_fkey FOREIGN KEY (hospital_id) REFERENCES public.hospital(hospital_id) ON DELETE CASCADE;


--
-- Name: request_fulfillment request_fulfillment_blood_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_fulfillment
    ADD CONSTRAINT request_fulfillment_blood_id_fkey FOREIGN KEY (blood_id) REFERENCES public.blood(blood_id) ON DELETE CASCADE;


--
-- Name: request_fulfillment request_fulfillment_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_fulfillment
    ADD CONSTRAINT request_fulfillment_request_id_fkey FOREIGN KEY (request_id) REFERENCES public.all_requests(request_id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict UaoblEDPgmtArTOqaGLBwM80eWdCmEFKMEtaTW0goRImtpku3a38vpMOc8hXdMg

