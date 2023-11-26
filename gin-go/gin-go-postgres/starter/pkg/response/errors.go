package response

import (
	"github.com/gin-gonic/gin"
	"github.com/mrgsrylm/ShaBank/goshabank/config"
)

func Error(c *gin.Context, status int, err error, message string) {
	cfg := config.GetConfig()
	errorRes := map[string]interface{}{
		"message": message,
	}

	if cfg.Environment != config.Production {
		errorRes["debug"] = err.Error()
	}

	c.JSON(status, Response{Error: errorRes})
}
