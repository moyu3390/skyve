create database skyve
GO
ALTER DATABASE skyve SET ALLOW_SNAPSHOT_ISOLATION ON
GO
ALTER DATABASE skyve SET READ_COMMITTED_SNAPSHOT ON
GO
ALTER DATABASE skyve SET AUTO_CLOSE ON WITH NO_WAIT
GO