package repository

import (
	"context"
	"errors"
	"testing"

	"github.com/mrgsrylm/ShaBank/goshabank/config"
	"github.com/mrgsrylm/ShaBank/goshabank/database/postgres/mocks"
	"github.com/mrgsrylm/ShaBank/goshabank/internal/users"
	"github.com/mrgsrylm/commonlibsingo/logger"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/suite"
)

type PostgresRepositoryTestSuite struct {
	suite.Suite
	mockDB *mocks.IDatabase
	repo   users.IRepository
}

func (suite *PostgresRepositoryTestSuite) SetupTest() {
	logger.New(config.Production)

	suite.mockDB = mocks.NewIDatabase(suite.T())
	suite.repo = NewPostgresRepository(suite.mockDB)
}

func TestPostgresRepositoryTestSuite(t *testing.T) {
	suite.Run(t, new(PostgresRepositoryTestSuite))
}

// Start:CreateTest
func (suite *PostgresRepositoryTestSuite) TestCreateUserSuccessfully() {
	user := &users.User{
		Email:    "test@test.com",
		Password: "test123456",
	}
	suite.mockDB.On("Create", mock.Anything, user).
		Return(nil).Times(1)

	err := suite.repo.Create(context.Background(), user)
	suite.Nil(err)
}

func (suite *PostgresRepositoryTestSuite) TestCreateUserFail() {
	user := &users.User{
		Email:    "test@test.com",
		Password: "test123456",
	}
	suite.mockDB.On("Create", mock.Anything, user).
		Return(errors.New("error")).Times(1)

	err := suite.repo.Create(context.Background(), user)
	suite.NotNil(err)
}
// End:CreateTest

// Start:GetByIDTest
func (suite *PostgresRepositoryTestSuite) TestFindByIdSuccessfully() {
	suite.mockDB.On("FindById", mock.Anything, "userId1", &users.User{}).
		Return(nil).Times(1)

	user, err := suite.repo.GetByID(context.Background(), "userId1")
	suite.Nil(err)
	suite.NotNil(user)
}

func (suite *PostgresRepositoryTestSuite) TestFindByIdFail() {
	suite.mockDB.On("FindById", mock.Anything, "userId1", &users.User{}).
		Return(errors.New("error")).Times(1)

	user, err := suite.repo.GetByID(context.Background(), "userId1")
	suite.NotNil(err)
	suite.Nil(user)
}
// End:GetByIDTest

// Start:GetByEmailTest
func (suite *PostgresRepositoryTestSuite) TestGetUserByEmailSuccessfully() {
	suite.mockDB.On("FindOne", mock.Anything, &users.User{}, mock.AnythingOfType("database.optionFn")).
		Return(nil).Times(1)

	user, err := suite.repo.GetByEmail(context.Background(), "email@test.com")
	suite.Nil(err)
	suite.NotNil(user)
}

func (suite *PostgresRepositoryTestSuite) TestGetUserByEmailFail() {
	suite.mockDB.On("FindOne", mock.Anything, &users.User{}, mock.AnythingOfType("database.optionFn")).
		Return(errors.New("error")).Times(1)

	user, err := suite.repo.GetByEmail(context.Background(), "email@test.com")
	suite.NotNil(err)
	suite.Nil(user)
}
// End:GetByEmailTest


// Start:UpdateTest
func (suite *PostgresRepositoryTestSuite) TestUpdateUserSuccessfully() {
	user := &users.User{
		ID:       "userId1",
		Email:    "test@test.com",
		Password: "test123456",
	}
	suite.mockDB.On("Update", mock.Anything, user).
		Return(nil).Times(1)

	err := suite.repo.Update(context.Background(), user)
	suite.Nil(err)
}

func (suite *PostgresRepositoryTestSuite) TestUpdateUserFail() {
	user := &users.User{
		ID:       "userId1",
		Email:    "test@test.com",
		Password: "test123456",
	}
	suite.mockDB.On("Update", mock.Anything, user).
		Return(errors.New("error")).Times(1)

	err := suite.repo.Update(context.Background(), user)
	suite.NotNil(err)
}
// End:UpdateTest