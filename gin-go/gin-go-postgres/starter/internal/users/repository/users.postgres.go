package repository

import (
	"context"
	database "github.com/mrgsrylm/ShaBank/goshabank/database/postgres"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users"
)

type postgresRepository struct {
	DB database.IDatabase
}

func NewPostgresRepository(DB database.IDatabase) users.IRepository {
	return &postgresRepository{DB}
}

// Create implements users.IRepository.
func (r *postgresRepository) Create(ctx context.Context, user *users.User) error {
	return r.DB.Create(ctx, user)
}

// GetByID implements users.IRepository.
func (r *postgresRepository) GetByID(ctx context.Context, id string) (*users.User, error) {
	var user users.User
	if err := r.DB.FindById(ctx, id, &user); err != nil {
		return nil, err
	}

	return &user, nil
}

// GetByEmail implements users.IRepository.
func (r *postgresRepository) GetByEmail(ctx context.Context, email string) (*users.User, error) {
	var user users.User

	query := database.NewQuery("email = ?", email)
	if err := r.DB.FindOne(ctx, &user, database.WithQuery(query)); err != nil {
		return nil, err
	}

	return &user, nil
}

// Update implements users.IRepository.
func (r *postgresRepository) Update(ctx context.Context, user *users.User) error {
	return r.DB.Update(ctx, user)
}



