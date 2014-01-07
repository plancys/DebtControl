package com.kalandyk.server.service;

import com.kalandyk.api.model.User;
import com.kalandyk.server.neo4j.entity.UserEntity;
import com.kalandyk.server.neo4j.repository.UserRepository;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public User createUser(User userEntity){
        UserEntity toSave = new UserEntity(userEntity);
        UserEntity savedUser = userRepository.save(toSave);
        Node savedNode = getNodeById(savedUser.getId());
        addRelationToRootNode(savedNode);
        return savedUser.toUserModel();
    }

    public User findUserByUsername(String username){
        UserEntity byUsername = userRepository.findByUsername(username);
        return byUsername.toUserModel();
    }

    public List<User> findUsersFriends(String username) {
        return null;
    }


    enum RelationFromRootToUsers implements RelationshipType {
        EXIST_USER
    }

    private void addRelationToRootNode(Node node){
        getRootNode().createRelationshipTo(node, RelationFromRootToUsers.EXIST_USER);
    }

    private Node getRootNode(){
        return neo4jTemplate.getGraphDatabaseService().getNodeById(0l);

    }

    private Node getNodeById(long id){
        return neo4jTemplate.getGraphDatabaseService().getNodeById(id);

    }


}
