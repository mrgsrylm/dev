package http

import (
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/mrgsrylm/ShaBank/goshabank/database/postgres/mocks"
	"github.com/mrgsrylm/commonlibsingo/validation"
)

func TestRoutes(t *testing.T) {
	mockDB := mocks.NewIDatabase(t)
	Routes(gin.New().Group("/"), mockDB, validation.New())
}
