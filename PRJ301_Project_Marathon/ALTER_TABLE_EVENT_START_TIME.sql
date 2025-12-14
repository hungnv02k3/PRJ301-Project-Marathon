-- SQL script to add event_start_time column to Events table
-- Run this script if you want to store event start time separately from event_date

USE MarathonDB
GO

-- Check if column exists, if not, add it
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Events]') AND name = 'event_start_time')
BEGIN
    ALTER TABLE [dbo].[Events]
    ADD [event_start_time] [datetime2](7) NULL
    PRINT 'Column event_start_time added successfully'
END
ELSE
BEGIN
    PRINT 'Column event_start_time already exists'
END
GO

-- Update existing records: set event_start_time = event_date at 00:00:00 if null
UPDATE [dbo].[Events]
SET [event_start_time] = CAST([event_date] AS DATETIME2)
WHERE [event_start_time] IS NULL
GO

