ALTER TABLE planter_settings DROP COLUMN settings_updated;
ALTER TABLE planter_settings ADD COLUMN settings_update_timestamp TIMESTAMP;

INSERT INTO planter_settings (settings_id, settings_key, settings_value, settings_update_timestamp, settings_planter_id) VALUES
    (UUID(), 'sleep_time', '60000', sysdate(), NULL),
    (UUID(), 'light_multiplier', '10', sysdate(), NULL),
    (UUID(), 'watering_threshold', '8', sysdate(), NULL),
    (UUID(), 'soil_moisture_multiplier', '4', sysdate(), NULL),
    (UUID(), 'pump_water_amount', '80', sysdate(), NULL),
    (UUID(), 'shortest_watering_interval', '500000', sysdate(), NULL),
    (UUID(), 'min_pump_voltage_level', '11.5', sysdate(), NULL)