DROP TABLE Planter_Settings;

CREATE TABLE Planter_Settings (
    settings_id VARCHAR(36) default UUID(),
    settings_ssid VARCHAR(32),
    settings_password VARCHAR(63),
    settings_host VARCHAR(200),
    settings_sleep_time NUMERIC,
    settings_updated BOOL,
    settings_planter_id VARCHAR(96),

    PRIMARY KEY (settings_id),
    FOREIGN KEY (settings_planter_id) REFERENCES Planters(planter_id)
);