package main

import (
	"github.com/mrgsrylm/ShaBank/goshabank/config"
	database "github.com/mrgsrylm/ShaBank/goshabank/database/postgres"
	httpServer "github.com/mrgsrylm/ShaBank/goshabank/internal/server/http"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users"
	"github.com/mrgsrylm/commonlibsingo/logger"
	"github.com/mrgsrylm/commonlibsingo/validation"
)

func main() {
	cfg := config.LoadConfig()
	logger.New(cfg.Environment)

	db, err := database.NewDatabase(cfg.DatabaseURI)
	if err != nil {
		logger.Fatal("cannot connect to database", err)
	}

	err = db.AutoMigrate(&users.User{})
	if err != nil {
		logger.Fatal("database migration fail", err)
	}

	validator := validation.New()

	httpserver := httpServer.NewServer(validator, db)
	if err = httpserver.Run(); err != nil {
		logger.Fatal(err)

	}
}
