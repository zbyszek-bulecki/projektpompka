
ALTER TABLE planter_tasks ADD COLUMN task_created_at TIMESTAMP;
ALTER TABLE planter_tasks ADD COLUMN task_created_by VARCHAR(30);

ALTER TABLE planter_tasks ADD COLUMN task_updated_at TIMESTAMP;
ALTER TABLE planter_tasks ADD COLUMN task_updated_by VARCHAR(30);