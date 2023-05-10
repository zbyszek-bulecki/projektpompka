CREATE TABLE Planters (
    id BIGINT,
    mac_address VARCHAR(17),
    name VARCHAR(100),

    PRIMARY KEY (id)
);

CREATE TABLE Measurement (
    id BIGINT,
    soil_moisture DOUBLE,
    light_intensity DOUBLE,
    temperature DOUBLE,
    pressure DOUBLE,
    planter_id BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (planter_id) REFERENCES Planters(id)
);

CREATE TABLE Tasks (
    id BIGINT,
    task VARCHAR(50),
    parameters VARCHAR(200),
    planter_id BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (planter_id) REFERENCES Planters(id)
);

CREATE TABLE Planter_Settings (
    id BIGINT,
    setting_key VARCHAR(50),
    setting_value VARCHAR(200),
    updated BOOL,
    planter_id BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (planter_id) REFERENCES Planters(id)
);