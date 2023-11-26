package users

import (
	"context"

	"github.com/google/uuid"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users/dto"
	"github.com/mrgsrylm/ShaBank/goshabank/pkg/utils"
	"gorm.io/gorm"
)

type RoleType string

const (
	RoleTypeAdmin    RoleType = "ADMIN"
	RoleTypeCustomer RoleType = "CUSTOMER"
	RoleTypeMerchant RoleType = "MERCHANT"
)

type User struct {
	ID          string   `json:"id" gorm:"unique;not null;index;primary_key"`
	Name        string   `json:"name" gorm:"not null"`
	Email       string   `json:"email" gorm:"unique;not null;index:idx_user_email"`
	Password    string   `json:"password" gorm:"not null"`
	PhoneNumber string   `json:"phoneNumber" gorm:"not null"`
	Image       string   `json:"image"`
	Role        RoleType `json:"role"`
}

func (user *User) BeforeCreate(DB *gorm.DB) error {
	user.ID = uuid.New().String()
	user.Password = utils.HashAndSalt([]byte(user.Password))
	if user.Role == "" {
		user.Role = RoleTypeCustomer
	}
	return nil
}

//go:generate mockery --name=IRepository
type IRepository interface {
	Create(ctx context.Context, user *User) error
	GetByID(ctx context.Context, id string) (*User, error)
	GetByEmail(ctx context.Context, email string) (*User, error)
	Update(ctx context.Context, user *User) error
}

//go:generate mockery --name=IService
type IService interface {
	Login(ctx context.Context, req *dto.LoginReq) (*User, string, string, error)
	Register(ctx context.Context, req *dto.RegisterReq) (*User, error)
	RefreshToken(ctx context.Context, userID string) (string, error)
	GetByID(ctx context.Context, id string) (*User, error)
	ChangePassword(ctx context.Context, id string, req *dto.ChangePasswordReq) error
}

