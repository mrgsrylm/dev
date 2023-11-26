package config

import (
	"log"
	"path/filepath"
	"runtime"
	"time"

	"github.com/caarlos0/env"
	"github.com/joho/godotenv"
)

const (
	Production       = "production"
	DatabaseTiomeout = 5 * time.Second
	CachingTime      = 1 * time.Minute
)

type Schema struct {
	Environment string `env:"environment"`
	HttpPort    int    `env:"http_port"`
	AuthSecret  string `env:"auth_secret"`
	DatabaseURI string `env:"database_uri"`
}

var (
	cfg Schema
)

func LoadConfig() *Schema {
	_, filename, _, _ := runtime.Caller(0)
	currentDir := filepath.Dir(filename)

	err := godotenv.Load(filepath.Join(currentDir, "config.yaml"))
	if err != nil {
		log.Printf("error on load configuration file, error: %v", err)
	}

	if err := env.Parse(&cfg); err != nil {
		log.Fatalf("error on parsing configuration file, error: %v", err)
	}

	return &cfg
}

func GetConfig() *Schema {
	return &cfg
}
