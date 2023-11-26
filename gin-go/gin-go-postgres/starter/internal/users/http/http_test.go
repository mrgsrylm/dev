package http

import (
	"bytes"
	"encoding/json"
	"errors"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/mrgsrylm/ShaBank/goshabank/config"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users/dto"
	mocks "github.com/mrgsrylm/ShaBank/goshabank/internal/users/mocks"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/response"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/utils"
	"github.com/mrgsrylm/commonlibsingo/logger"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/suite"
)

type HandlerTestSuite struct {
	suite.Suite
	mockService *mocks.IService
	handler     *Handler
}

func (suite *HandlerTestSuite) SetupTest() {
	logger.New(config.Production)

	suite.mockService = mocks.NewIService(suite.T())
	suite.handler = NewHandler(suite.mockService)
}

func TestHandlerTestSuite(t *testing.T) {
	suite.Run(t, new(HandlerTestSuite))
}

func (suite *HandlerTestSuite) prepareContext(body any) (*gin.Context, *httptest.ResponseRecorder) {
	requestBody, _ := json.Marshal(body)
	w := httptest.NewRecorder()
	r := httptest.NewRequest("", "/", bytes.NewBuffer(requestBody))
	c, _ := gin.CreateTestContext(w)
	c.Request = r

	return c, w
}

// Start:RegisterTest
func (suite *HandlerTestSuite) TestRegisterSuccess() {
	req := &dto.RegisterReq{
		Name: "Register",
		Email:    "register@test.com",
		Password: "test123456",
		PhoneNumber: "081234567890",
		Image: "server/image.png",
	}

	ctx, writer := suite.prepareContext(req)

	suite.mockService.On("Register", mock.Anything, req).
		Return(
			&users.User{
				Name: "Register",
				Email:    "register@test.com",
				Password: "test123456",
				PhoneNumber: "081234567890",
				Image: "server/image.png",
			},
			nil,
		).Times(1)

	suite.handler.Register(ctx)

	var res response.Response
	var registerRes dto.RegisterRes

	_ = json.Unmarshal(writer.Body.Bytes(), &res)
	utils.CopyByJson(&registerRes, &res.Result)
	suite.Equal(http.StatusOK, writer.Code)
	suite.Equal(req.Email, registerRes.User.Email)
}

func (suite *HandlerTestSuite) TestRegisterInvalidEmailType() {
	req := map[string]interface{}{
		"name": "johndow",
		"email":    1,
		"password": "test123456",
		"phoneNumber": "081234567890",
		"image": "server/iamge.png",
	}

	ctx, writer := suite.prepareContext(req)

	suite.handler.Register(ctx)

	var res map[string]map[string]string
	_ = json.Unmarshal(writer.Body.Bytes(), &res)
	suite.Equal(http.StatusBadRequest, writer.Code)
	suite.Equal("Invalid parameters", res["error"]["message"])
}

func (suite *HandlerTestSuite) TestRegisterInvalidPasswordType() {
	req := map[string]interface{}{
		"name": "johndow",
		"email":    "login@test.com",
		"password": 12345,
		"phoneNumber": "081234567890",
		"image": "server/iamge.png",
	}

	ctx, writer := suite.prepareContext(req)

	suite.handler.Register(ctx)

	var res map[string]map[string]string
	_ = json.Unmarshal(writer.Body.Bytes(), &res)
	suite.Equal(http.StatusBadRequest, writer.Code)
	suite.Equal("Invalid parameters", res["error"]["message"])
}

func (suite *HandlerTestSuite) TestRegisterFail() {
	req := &dto.RegisterReq{
		Email:    "register@test.com",
		Password: "test123456",
	}

	ctx, writer := suite.prepareContext(req)

	suite.mockService.On("Register", mock.Anything, req).
		Return(nil, errors.New("error")).Times(1)

	suite.handler.Register(ctx)

	var res map[string]map[string]string
	_ = json.Unmarshal(writer.Body.Bytes(), &res)
	suite.Equal(http.StatusInternalServerError, writer.Code)
	suite.Equal("Something went wrong", res["error"]["message"])
}
// End:RegisterTest

// Start: LoginTest
func (suite *HandlerTestSuite) TestLoginSuccess() {
	req := &dto.LoginReq{
		Email:    "login@test.com",
		Password: "test123456",
	}

	ctx, writer := suite.prepareContext(req)

	suite.mockService.On("Login", mock.Anything, req).
		Return(
			&users.User{
				Email:    "login@test.com",
				Password: "test123456",
			},
			"access-token",
			"refresh-token",
			nil,
		).Times(1)

	suite.handler.Login(ctx)

	var res response.Response
	var loginRes dto.LoginRes

	_ = json.Unmarshal(writer.Body.Bytes(), &res)
	utils.CopyByJson(&loginRes, &res.Result)
	suite.Equal(http.StatusOK, writer.Code)
	suite.Equal(req.Email, loginRes.User.Email)
	suite.Equal("access-token", loginRes.AccessToken)
	suite.Equal("refresh-token", loginRes.RefreshToken)
}

func (suite *HandlerTestSuite) TestLoginInvalidEmailType() {
	req := map[string]interface{}{
		"email":    1,
		"password": "test123456",
	}

	ctx, writer := suite.prepareContext(req)

	suite.handler.Login(ctx)

	var res map[string]map[string]string
	_ = json.Unmarshal(writer.Body.Bytes(), &res)
	suite.Equal(http.StatusBadRequest, writer.Code)
	suite.Equal("Invalid parameters", res["error"]["message"])
}

func (suite *HandlerTestSuite) TestLoginInvalidPasswordType() {
	req := map[string]interface{}{
		"email":    "login@test.com",
		"password": 12345,
	}

	ctx, writer := suite.prepareContext(req)

	suite.handler.Login(ctx)

	var res map[string]map[string]string
	_ = json.Unmarshal(writer.Body.Bytes(), &res)
	suite.Equal(http.StatusBadRequest, writer.Code)
	suite.Equal("Invalid parameters", res["error"]["message"])
}

func (suite *HandlerTestSuite) TestLoginFail() {
	req := &dto.LoginReq{
		Email:    "login@test.com",
		Password: "test123456",
	}

	ctx, writer := suite.prepareContext(req)

	suite.mockService.On("Login", mock.Anything, req).
		Return(nil, "", "", errors.New("error")).Times(1)

	suite.handler.Login(ctx)

	var res map[string]map[string]string
	_ = json.Unmarshal(writer.Body.Bytes(), &res)
	suite.Equal(http.StatusInternalServerError, writer.Code)
	suite.Equal("Something went wrong", res["error"]["message"])
}
// End: LoginTest