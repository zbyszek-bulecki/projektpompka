CREATE TABLE Planters (
    planter_id VARCHAR(96),
    planter_name VARCHAR(100),
    planter_mac_address VARCHAR(17),
    planter_last_activity DATETIME,

    PRIMARY KEY (planter_id),
    CONSTRAINT Planters_Identifier UNIQUE (planter_name,planter_mac_address)
);

CREATE TABLE Planter_Measurement (
    measurement_id VARCHAR(96),
    measurement_soil_moisture DOUBLE,
    measurement_light_intensity DOUBLE,
    measurement_temperature DOUBLE,
    measurement_pressure DOUBLE,
    measurement_water_level DOUBLE,
    measurement_planter_id VARCHAR(96),
    measurement_created_at DATETIME,

    PRIMARY KEY (measurement_id),
    FOREIGN KEY (measurement_planter_id) REFERENCES Planters(planter_id)
);

CREATE TABLE Planter_Tasks (
    task_id VARCHAR(96),
    task_task VARCHAR(50),
    task_parameters VARCHAR(200),
    task_planter_id VARCHAR(96),
    task_finished BOOL,

    PRIMARY KEY (task_id),
    FOREIGN KEY (task_planter_id) REFERENCES Planters(planter_id)
);

CREATE TABLE Planter_Settings (
    settings_id VARCHAR(96),
    settings_setting_key VARCHAR(50),
    settings_setting_value VARCHAR(200),
    settings_updated BOOL,
    settings_planter_id VARCHAR(96),

    PRIMARY KEY (settings_id),
    FOREIGN KEY (settings_planter_id) REFERENCES Planters(planter_id)
);