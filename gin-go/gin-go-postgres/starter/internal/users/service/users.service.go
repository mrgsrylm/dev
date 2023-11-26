package service

import (
	"context"
	"errors"

	"github.com/mrgsrylm/ShaBank/goshabank/internal/users"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users/dto"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/tokens"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/utils"
	"github.com/mrgsrylm/commonlibsingo/logger"
	"github.com/mrgsrylm/commonlibsingo/validation"
	"golang.org/x/crypto/bcrypt"
)

type service struct {
	validator validation.Validation
	repo      users.IRepository
}

func NewService(validator validation.Validation, repo users.IRepository) users.IService {
	return &service{
		validator: validator,
		repo:      repo,
	}
}

// Register implements users.IService.
func (s *service) Register(ctx context.Context, req *dto.RegisterReq) (*users.User, error) {
	if err := s.validator.ValidateStruct(req); err != nil {
		return nil, err
	}

	var user users.User
	utils.CopyByJson(&user, &req)

	err := s.repo.Create(ctx, &user)
	if err != nil {
		logger.Errorf("Register.Create fail, email: %s, error: %s", req.Email, err)
		return nil, err
	}

	return &user, nil
}

// Login implements users.IService.
func (s *service) Login(ctx context.Context, req *dto.LoginReq) (*users.User, string, string, error) {
	if err := s.validator.ValidateStruct(req); err != nil {
		return nil, "", "", err
	}

	user, err := s.repo.GetByEmail(ctx, req.Email)
	if err != nil {
		logger.Errorf("Login.GetByEmail fail, email: %s, error: %s", req.Email, err)
		return nil, "", "", err
	}

	if err = bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(req.Password)); err != nil {
		return nil, "", "", errors.New("wrong password")
	}

	tokenData := map[string]interface{}{
		"id":    user.ID,
		"email": user.Email,
		"role":  user.Role,
	}
	accessToken := tokens.GenerateAccessToken(tokenData)
	refreshToken := tokens.GenerateRefreshToken(tokenData)

	return user, accessToken, refreshToken, nil
}

// RefreshToken implements users.IService.
func (s *service) RefreshToken(ctx context.Context, userID string) (string, error) {
	user, err := s.repo.GetByID(ctx, userID)
	if err != nil {
		logger.Errorf("RefreshToken.GetByID fail, id: %s, error: %s", userID, err)
		return "", err
	}

	tokenData := map[string]interface{}{
		"id":    user.ID,
		"email": user.Email,
		"role":  user.Role,
	}
	accessToken := tokens.GenerateAccessToken(tokenData)

	return accessToken, nil
}

// GetUserByID implements users.IService.
func (s *service) GetByID(ctx context.Context, id string) (*users.User, error) {
	user, err := s.repo.GetByID(ctx, id)
	if err != nil {
		logger.Errorf("GetByID fail, id: %s, error: %s", id, err)
		return nil, err
	}

	return user, nil
}

// ChangePassword implements users.IService.
func (s *service) ChangePassword(ctx context.Context, id string, req *dto.ChangePasswordReq) error {
	if err := s.validator.ValidateStruct(req); err != nil {
		return err
	}
	user, err := s.repo.GetByID(ctx, id)
	if err != nil {
		logger.Errorf("ChangePassword.GetByID fail, id: %s, error: %s", id, err)
		return err
	}

	if err = bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(req.Password)); err != nil {
		return errors.New("wrong password")
	}

	user.Password = utils.HashAndSalt([]byte(req.NewPassword))
	err = s.repo.Update(ctx, user)
	if err != nil {
		logger.Errorf("ChangePassword.Update fail, id: %s, error: %s", id, err)
		return err
	}

	return nil
}
