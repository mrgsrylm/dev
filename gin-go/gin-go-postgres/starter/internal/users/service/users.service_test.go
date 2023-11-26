package service

import (
	"context"
	"errors"
	"testing"

	"github.com/mrgsrylm/ShaBank/goshabank/config"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users/dto"
	mocks "github.com/mrgsrylm/ShaBank/goshabank/internal/users/mocks"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/utils"
	"github.com/mrgsrylm/commonlibsingo/logger"
	"github.com/mrgsrylm/commonlibsingo/validation"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/suite"
)

type ServiceTestSuite struct {
	suite.Suite
	mockRepo *mocks.IRepository
	service  users.IService
}

func (suite *ServiceTestSuite) SetupTest() {
	logger.New(config.Production)
	validator := validation.New()

	suite.mockRepo = mocks.NewIRepository(suite.T())
	suite.service = NewService(validator, suite.mockRepo)
}

func TestServiceTestSuite(t *testing.T) {
	suite.Run(t, new(ServiceTestSuite))
}

// Start:RegisterTest
func (suite *ServiceTestSuite) TestRegisterSuccess() {
	req := &dto.RegisterReq{
		Name:        "test",
		Email:       "test@test.com",
		Password:    "test123456",
		PhoneNumber: "081234567890",
		Image:       "server/image.png",
	}
	suite.mockRepo.On("Create", mock.Anything, mock.Anything).
		Return(nil).Times(1)

	user, err := suite.service.Register(context.Background(), req)
	suite.NotNil(user)
	suite.Nil(err)
}

func (suite *ServiceTestSuite) TestRegisterCreateUserFail() {
	req := &dto.RegisterReq{
		Name:        "test",
		Email:       "test@test.com",
		Password:    "test123456",
		PhoneNumber: "081234567890",
		Image:       "server/image.png",
	}
	suite.mockRepo.On("Create", mock.Anything, mock.Anything).
		Return(errors.New("error")).Times(1)

	user, err := suite.service.Register(context.Background(), req)
	suite.Nil(user)
	suite.NotNil(err)
}

func (suite *ServiceTestSuite) TestRegisterInvalidEmailFormat() {
	req := &dto.RegisterReq{
		Name:        "test",
		Email:       "test",
		Password:    "test123456",
		PhoneNumber: "081234567890",
		Image:       "server/image.png",
	}
	user, err := suite.service.Register(context.Background(), req)
	suite.Nil(user)
	suite.NotNil(err)
}
// End:RegisterTest


// Start:LoginTest
func (suite *ServiceTestSuite) TestLoginSuccess() {
	req := &dto.LoginReq{
		Email:    "test@test.com",
		Password: "test123456",
	}
	suite.mockRepo.On("GetByEmail", mock.Anything, req.Email).
		Return(
			&users.User{
				Email:    "test@test.com",
				Password: utils.HashAndSalt([]byte("test123456")),
			},
			nil,
		).Times(1)

	user, accessToken, refreshToken, err := suite.service.Login(context.Background(), req)
	suite.NotNil(user)
	suite.Equal(req.Email, user.Email)
	suite.NotEmpty(accessToken)
	suite.NotEmpty(refreshToken)
	suite.Nil(err)
}

func (suite *ServiceTestSuite) TestLoginGetUserByEmailFail() {
	req := &dto.LoginReq{
		Email:    "test@test.com",
		Password: "test123456",
	}
	suite.mockRepo.On("GetByEmail", mock.Anything, req.Email).
		Return(nil, errors.New("error")).Times(1)

	user, accessToken, refreshToken, err := suite.service.Login(context.Background(), req)
	suite.Nil(user)
	suite.Empty(accessToken)
	suite.Empty(refreshToken)
	suite.NotNil(err)
}

func (suite *ServiceTestSuite) TestLoginInvalidEmailFormat() {
	req := &dto.LoginReq{
		Email:    "email",
		Password: "test123456",
	}

	user, accessToken, refreshToken, err := suite.service.Login(context.Background(), req)
	suite.Nil(user)
	suite.Empty(accessToken)
	suite.Empty(refreshToken)
	suite.NotNil(err)
}

func (suite *ServiceTestSuite) TestLoginWrongPassword() {
	req := &dto.LoginReq{
		Email:    "test@test.com",
		Password: "test123456",
	}

	suite.mockRepo.On("GetByEmail", mock.Anything, req.Email).
		Return(&users.User{
			Email:    "test@test.com",
			Password: "password",
		}, nil).Times(1)

	user, accessToken, refreshToken, err := suite.service.Login(context.Background(), req)
	suite.Nil(user)
	suite.Empty(accessToken)
	suite.Empty(refreshToken)
	suite.NotNil(err)
}
// End:LoginTest

// Start:RefreshTokenTest
func (suite *ServiceTestSuite) TestRefreshTokenSuccess() {
	userID := "userID"
	suite.mockRepo.On("GetByID", mock.Anything, userID).
		Return(
			&users.User{
				ID:    userID,
				Email: "test@test.com",
			}, nil,
		).Times(1)

	refreshToken, err := suite.service.RefreshToken(context.Background(), userID)
	suite.NotEmpty(refreshToken)
	suite.Nil(err)
}

func (suite *ServiceTestSuite) TestRefreshTokenGetUserByIDFail() {
	userID := "userID"
	suite.mockRepo.On("GetByID", mock.Anything, userID).
		Return(nil, errors.New("error")).Times(1)

	refreshToken, err := suite.service.RefreshToken(context.Background(), userID)
	suite.Empty(refreshToken)
	suite.NotNil(err)
}
// End:RefreshTokenTest

// Start:GetByID
func (suite *ServiceTestSuite) TestGetUserByIDSuccess() {
	userID := "userID"

	suite.mockRepo.On("GetByID", mock.Anything, userID).
		Return(
			&users.User{
				ID:    userID,
				Email: "test@test.com",
			},
			nil,
		).Times(1)

	user, err := suite.service.GetByID(context.Background(), userID)
	suite.NotNil(user)
	suite.Equal(userID, user.ID)
	suite.Equal("test@test.com", user.Email)
	suite.Nil(err)
}

func (suite *ServiceTestSuite) TestGetUserByIDFail() {
	userID := "userID"
	suite.mockRepo.On("GetByID", mock.Anything, userID).
		Return(nil, errors.New("error")).Times(1)

	user, err := suite.service.GetByID(context.Background(), userID)
	suite.Nil(user)
	suite.NotNil(err)
}
// End:GetByID

// Start:ChagePasswordTest
func (suite *ServiceTestSuite) TestChangePasswordSuccess() {
	userID := "userID"
	req := &dto.ChangePasswordReq{
		Password:    "password",
		NewPassword: "newPassword",
	}

	suite.mockRepo.On("GetByID", mock.Anything, userID).
		Return(
			&users.User{
				ID:       userID,
				Email:    "test@test.com",
				Password: utils.HashAndSalt([]byte("password")),
			}, nil,
		).Times(1)
	suite.mockRepo.On("Update", mock.Anything, mock.Anything).
		Return(nil).Times(1)

	err := suite.service.ChangePassword(context.Background(), userID, req)
	suite.Nil(err)
}

func (suite *ServiceTestSuite) TestChangePasswordGetUserByIDFail() {
	userID := "userID"
	req := &dto.ChangePasswordReq{
		Password:    "password",
		NewPassword: "newPassword",
	}

	suite.mockRepo.On("GetByID", mock.Anything, userID).
		Return(nil, errors.New("error")).Times(1)

	err := suite.service.ChangePassword(context.Background(), userID, req)
	suite.NotNil(err)
}

func (suite *ServiceTestSuite) TestChangePasswordMissRequiredField() {
	userID := "userID"
	req := &dto.ChangePasswordReq{
		Password:    "password",
		NewPassword: "",
	}

	err := suite.service.ChangePassword(context.Background(), userID, req)
	suite.NotNil(err)
}

func (suite *ServiceTestSuite) TestChangePasswordWrongCurrentPassword() {
	userID := "userID"
	req := &dto.ChangePasswordReq{
		Password:    "password1",
		NewPassword: "newPassword",
	}

	suite.mockRepo.On("GetByID", mock.Anything, userID).
		Return(
			&users.User{
				ID:       userID,
				Email:    "test@test.com",
				Password: utils.HashAndSalt([]byte("password")),
			}, nil,
		).Times(1)

	err := suite.service.ChangePassword(context.Background(), userID, req)
	suite.NotNil(err)
}

func (suite *ServiceTestSuite) TestChangePasswordUpdateUserFail() {
	userID := "userID"
	req := &dto.ChangePasswordReq{
		Password:    "password",
		NewPassword: "newPassword",
	}

	suite.mockRepo.On("GetByID", mock.Anything, userID).
		Return(
			&users.User{
				ID:       userID,
				Email:    "test@test.com",
				Password: utils.HashAndSalt([]byte("password")),
			}, nil,
		).Times(1)
	suite.mockRepo.On("Update", mock.Anything, mock.Anything).
		Return(errors.New("error")).Times(1)

	err := suite.service.ChangePassword(context.Background(), userID, req)
	suite.NotNil(err)
}
// End: ChangePasswordTest