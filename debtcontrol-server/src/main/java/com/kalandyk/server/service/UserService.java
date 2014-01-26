package com.kalandyk.server.service;

import com.kalandyk.api.model.User;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;

import org.dozer.Mapper;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kamil on 1/5/14.
 */

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Autowired
    private Mapper mapper;

    public User createUser(User user) {
        UserEntity toSave = new UserEntity(user);
        UserEntity alreadyExist = userRepository.findByLogin(toSave.getLogin());
        if(alreadyExist != null){
            return alreadyExist.toUserModel();
        }
        UserEntity savedUser = userRepository.save(toSave);
        Node savedNode = getNodeById(savedUser.getId());
        addRelationToRootNode(savedNode);
        return savedUser.toUserModel();
    }

    public boolean acceptAddingToFriendRequest(User decisionUser, User requesterUser) {
        UserEntity requester = userRepository.findOne(requesterUser.getId());
        UserEntity decisionMaker = userRepository.findOne(decisionUser.getId());

        Set<UserEntity> requests = decisionMaker.getFriendshipInvitation();
        if (!requests.contains(requester)) {
            return false;
        }
        return addToFriends(requester, decisionMaker);
    }

    public boolean cancelAddingToFriendRequest(User decisionUser, User requesterUser){
        UserEntity requester = userRepository.findOne(requesterUser.getId());
        UserEntity decisionMaker = userRepository.findOne(decisionUser.getId());

        Set<UserEntity> requests = decisionMaker.getFriendshipInvitation();
        requests.remove(requester);

        decisionMaker = userRepository.save(decisionMaker);
        return decisionMaker != null;
    }

    public boolean createAddingToFriendRequest(User decisionUser, User requesterUser){
        UserEntity requester = userRepository.findOne(requesterUser.getId());
        UserEntity decisionMaker = userRepository.findOne(decisionUser.getId());

        Set<UserEntity> requests = decisionMaker.getFriendshipInvitation();
        requests.add(requester);

        decisionMaker = userRepository.save(decisionMaker);
        return decisionMaker != null;
    }

    private boolean addToFriends(UserEntity requester, UserEntity decisionMaker) {

        Set<UserEntity> approverFriends = decisionMaker.getFriends();
        Set<UserEntity> requesterFriends = requester.getFriends();
        requesterFriends.add(decisionMaker);
        approverFriends.add(requester);

        Set<UserEntity> requests = decisionMaker.getFriendshipInvitation();
        boolean removed = requests.remove(requester);

        decisionMaker = userRepository.save(decisionMaker);
        requester = userRepository.save(requester);
        return removed && decisionMaker != null && requester != null;
    }

    public User findUserByLogin(String username) {
        UserEntity byUsername = userRepository.findByLogin(username);
        if(byUsername == null){
            return null;
        }
        return mapper.map(byUsername, User.class);
    }

    public Set<User> findUserFriendsByLogin(String login) {
        Set<User> usersFriends = new HashSet<User>();
        UserEntity user = userRepository.findByLogin(login);
        for(UserEntity friend : user.getFriends()){
            UserEntity friendFetched = userRepository.findOne(friend.getId());
            usersFriends.add(mapper.map(friendFetched, User.class));
        }
        return usersFriends;
    }

    public User authenticateUser(String login, String password) {
        UserEntity user = userRepository.findByLogin(login);
        if(user == null){
            return null;
        }

        if( user.getPassword() != null && !user.getPassword().equals(password)){
            return null;
        }

        return mapper.map(user, User.class);
    }


    enum RelationFromRootToUsers implements RelationshipType {
        EXIST_USER
    }

    private void addRelationToRootNode(Node node) {
        getRootNode().createRelationshipTo(node, RelationFromRootToUsers.EXIST_USER);
    }

    private Node getRootNode() {
        return neo4jTemplate.getGraphDatabaseService().getNodeById(0l);

    }

    private Node getNodeById(long id) {
        return neo4jTemplate.getGraphDatabaseService().getNodeById(id);

    }


}
