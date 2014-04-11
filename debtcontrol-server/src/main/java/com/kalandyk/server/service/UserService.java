package com.kalandyk.server.service;

import com.kalandyk.api.model.User;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.exception.ExceptionType;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mapper mapper;

    public UserEntity createUser(UserEntity user) throws DebtControlException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new DebtControlException(ExceptionType.DEBT_CREATION_EXCEPTION,
                    "User with that login already exist (" + user.getEmail() + ")");
        }
        user = userRepository.save(user);
        return user;
    }

    public UserEntity makeDecisionRegardingFriendshipRequest(UserEntity approver, UserEntity requester, Boolean decision) throws DebtControlException {
        requester = fetchUser(requester);
        approver = fetchUser(approver);

        Set<UserEntity> requests = approver.getFriendshipInvitation();
        if (!requests.contains(requester)) {
            throw new DebtControlException(ExceptionType.USER_EDITING_EXCEPTION,
                    new StringBuilder()
                            .append("Error with approving friendship request: ")
                            .append("User ").append(approver.getEmail())
                            .append(" doesn't have invitation from user ")
                            .append(requester.getEmail()).toString());
        }

        if (decision) {
            approver.getFriends().add(requester);
            requester.getFriends().add(approver);
        }

        approver.getFriendshipInvitation().remove(requester);
        requester.getOutgoingFriendshipInvitation().remove(approver);
        saveUser(requester);
        return saveUser(approver);
    }

    public UserEntity createAddingToFriendRequest(UserEntity targetPerson, UserEntity requester) throws DebtControlException {
        targetPerson = fetchUser(targetPerson);
        requester = fetchUser(requester);
        requester.getOutgoingFriendshipInvitation().add(targetPerson);
        targetPerson.getFriendshipInvitation().add(requester);
        saveUser(targetPerson);
        return saveUser(requester);
    }

    public User findUserByEmail(String email) {
        UserEntity byUsername = userRepository.findByEmail(email);
        if (byUsername == null) {
            return null;
        }
        byUsername.setPassword(null);
        return mapper.map(byUsername, User.class);
    }

    private UserEntity saveUser(UserEntity requester) throws DebtControlException {
        try {
            return userRepository.save(requester);
        } catch (Exception e) {
            throw new DebtControlException(ExceptionType.USER_EDITING_EXCEPTION,
                    new StringBuilder()
                            .append("Error with saving user.")
                            .append("Details: ").append(e.getMessage())
                            .toString());
        }
    }

    private UserEntity fetchUser(UserEntity userEntity) throws DebtControlException {
        UserEntity user = userRepository.findOne(userEntity.getId());
        if (user == null) {
            throw new DebtControlException(ExceptionType.USER_EDITING_EXCEPTION,
                    new StringBuilder()
                            .append("User doesn't exist:").append(userEntity)
                            .toString());
        }
        return user;
    }
}