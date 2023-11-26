package http

import (
	"github.com/gin-gonic/gin"
	database "github.com/mrgsrylm/ShaBank/goshabank/database/postgres"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users/repository"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users/service"
	"github.com/mrgsrylm/commonlibsingo/validation"
)

func Routes(r *gin.RouterGroup, DB database.IDatabase, validator validation.Validation) {
	repo := repository.NewPostgresRepository(DB)
	service := service.NewService(validator, repo)
	handler := NewHandler(service)

	// authMiddleware := middlewares.JWTAuth()
	// refreshMiddleware := middlewares.JWTRefresh()

	authRoute := r.Group("/auth")
	{
		authRoute.POST("/register", handler.Register)
		authRoute.POST("/login", handler.Login)
	}
}
