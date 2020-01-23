package com.example.library.core.service;

import com.example.library.core.exception.ActiveBorrowOrdersException;
import com.example.library.core.exception.UserExistException;
import com.example.library.core.exception.UserNotFoundException;
import com.example.library.core.repository.BorrowOrderRepository;
import com.example.library.core.repository.UserRepository;
import com.example.library.model.BorrowOrder;
import com.example.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * User service supports all operations regarding users. (CRUD)
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BorrowOrderRepository borrowOrderRepository;

    @Autowired
    public UserService(UserRepository userRepository, BorrowOrderRepository borrowOrderRepository) {
        this.userRepository = userRepository;
        this.borrowOrderRepository = borrowOrderRepository;
    }

    /**
     * Registers a new user.
     *
     * @param user New user to be saved in the user repository.
     * @throws UserExistException User with the given username already exist in the repository.
     */
    public void registerUser(User user) throws UserExistException {
        Optional<User> userOptional = this.userRepository.findByUsername(user.getUsername());
        if (userOptional.isEmpty()) {
            this.userRepository.save(user);
        } else {
            throw UserExistException.createWithUsername(user.getUsername());
        }
    }

    /**
     * Retrieves the user from the repository by id.
     *
     * @param id User id to be searched in the repository.
     * @return User from the user repository with the given id.
     * @throws UserNotFoundException The user with the given id does not exist in the repository.
     */
    public User getUserById(Long id) throws UserNotFoundException {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw UserNotFoundException.createWithId(id);
        }
        return userOptional.get();
    }

    /**
     * Retrieves the user from the repository by username.
     *
     * @param username User username to be searched in the repository.
     * @return User from the user repository with the given username.
     * @throws UserNotFoundException The user with the given username does not exist in the repository.
     */
    public User getUserByUsername(String username) throws UserNotFoundException {
        Optional<User> userOptional = this.userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw UserNotFoundException.createWithUsername(username);
        }
        return userOptional.get();
    }

    /**
     * Retrieves the list of all users from the repository.
     *
     * @return User list from the repository.
     */
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Deletes the user from the repository.
     *
     * @param id Id of the user to be deleted from the repository.
     * @throws UserNotFoundException       The user with the given id does not exist in the repository.
     * @throws ActiveBorrowOrdersException The user with the given id has active borrow orders.
     */
    public void deleteUserById(Long id) throws UserNotFoundException, ActiveBorrowOrdersException {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            List<BorrowOrder> borrowOrders = this.borrowOrderRepository.findAllByUserId(id);
            if (!borrowOrders.isEmpty()) {
                throw ActiveBorrowOrdersException.createWithUserId(id);
            }
            this.userRepository.deleteById(id);
        } else {
            throw UserNotFoundException.createWithId(id);
        }
    }

    /**
     * Updates the user.
     *
     * @param user Updated user to be saved in the repository.
     * @throws UserNotFoundException The user with the given id does not exist in the repository.
     */
    public void updateUser(User user) throws UserNotFoundException {
        Optional<User> userOptional = this.userRepository.findById(user.getId());
        if (userOptional.isEmpty()) {
            throw UserNotFoundException.createWithId(user.getId());
        }
        this.userRepository.save(user);
    }
}
