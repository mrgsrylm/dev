package middlewares

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/tokens"
)

func JWTAuth() gin.HandlerFunc {
	return JWT(tokens.AccessTokenType)
}

func JWTRefresh() gin.HandlerFunc {
	return JWT(tokens.RefreshTokenType)
}

func JWT(tokenType string) gin.HandlerFunc {
	return func(c *gin.Context) {
		token := c.GetHeader("Authorization")
		if token == "" {
			c.JSON(http.StatusUnauthorized, nil)
			c.Abort()
			return
		}

		payload, err := tokens.ValidateToken(token)
		if err != nil || payload == nil || payload["type"] != tokenType {
			c.JSON(http.StatusUnauthorized, nil)
			c.Abort()
			return
		}
		c.Set("userId", payload["id"])
		c.Set("role", payload["role"])
		c.Next()
	}
}
