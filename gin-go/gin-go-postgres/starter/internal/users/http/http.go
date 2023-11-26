package http

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users/dto"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/response"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/utils"
	"github.com/mrgsrylm/commonlibsingo/logger"
)

type Handler struct {
	service users.IService
}

func NewHandler(service users.IService) *Handler {
	return &Handler{service}
}

// Register godoc
//
//	@Summary	Register new user
//	@Tags		users
//	@Produce	json
//	@Param		_	body		dto.RegisterReq	true	"Body"
//	@Success	200	{object}	dto.RegisterRes
//	@Router		/api/v1/auth/register [post]
func (h *Handler) Register(c *gin.Context) {
	var req dto.RegisterReq
	if err := c.ShouldBindJSON(&req); c.Request.Body == nil || err != nil {
		logger.Error("Failed to get body", err)
		response.Error(c, http.StatusBadRequest, err, "Invalid parameters")
		return
	}

	user, err := h.service.Register(c, &req)
	if err != nil {
		logger.Error(err.Error())
		response.Error(c, http.StatusInternalServerError, err, "Something went wrong")
		return
	}

	var res dto.RegisterRes
	utils.CopyByJson(&res.User, &user)
	response.JSON(c, http.StatusOK, res)
}

// Login godoc
//
//	@Summary	Login
//	@Tags		users
//	@Produce	json
//	@Param		_	body		dto.LoginReq	true	"Body"
//	@Success	200	{object}	dto.LoginRes
//	@Router		/api/v1/auth/login [post]
func (h *Handler) Login(c *gin.Context) {
	var req dto.LoginReq
	if err := c.ShouldBindJSON(&req); c.Request.Body == nil || err != nil {
		logger.Error("Failed to get body ", err)
		response.Error(c, http.StatusBadRequest, err, "Invalid parameters")
		return
	}

	user, accessToken, refreshToken, err := h.service.Login(c, &req)
	if err != nil {
		logger.Error("Failed to login ", err)
		response.Error(c, http.StatusInternalServerError, err, "Something went wrong")
		return
	}

	var res dto.LoginRes
	utils.CopyByJson(&res.User, &user)
	res.AccessToken = accessToken
	res.RefreshToken = refreshToken

	response.JSON(c, http.StatusOK, res)
}
