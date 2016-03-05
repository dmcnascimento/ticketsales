CREATE TABLE tb_theatre (id_theatre INTEGER PRIMARY KEY, nm_theatre VARCHAR(100) NOT NULL);
CREATE TABLE tb_event (id_event INTEGER PRIMARY KEY, tp_rating VARCHAR(3) NOT NULL, nm_event VARCHAR(100) NOT NULL, 
	ds_event VARCHAR(255) NOT NULL,  qt_duration INTEGER NOT NULL, id_theatre INTEGER NOT NULL REFERENCES tb_theatre(id_theatre));
CREATE TABLE tb_session (id_session INTEGER PRIMARY KEY, dt_session TIMESTAMP NOT NULL,
	id_event INTEGER NOT NULL REFERENCES tb_event(id_event));
CREATE TABLE tb_chair (id_chair INTEGER PRIMARY KEY, nm_row CHAR(1) NOT NULL, nr_chair INTEGER NOT NULL, 
	id_theatre INTEGER NOT NULL REFERENCES tb_theatre(id_theatre));
CREATE TABLE tb_price (id_price INTEGER PRIMARY KEY, nm_weekday CHAR(3) NOT NULL, hr_start TIME NOT NULL,
	vl_price NUMERIC(10,2) NOT NULL, id_event INTEGER NOT NULL REFERENCES tb_event(id_event));
CREATE TABLE tb_ticket (id_ticket INTEGER PRIMARY KEY, id_session INTEGER NOT NULL REFERENCES tb_session(id_session),
	id_chair INTEGER NOT NULL REFERENCES tb_chair(id_chair));