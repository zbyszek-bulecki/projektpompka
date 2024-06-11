CREATE UNIQUE INDEX planter_unique_settings_key
ON planter_settings (settings_planter_id, settings_setting_key);
ALTER TABLE planter_settings CHANGE COLUMN settings_setting_key settings_key varchar(50);
ALTER TABLE planter_settings CHANGE COLUMN settings_setting_value settings_value varchar(200);