package com.kalandyk.server.neo4j.entity;

import com.kalandyk.api.model.User;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kamil on 1/4/14.
 */
@NodeEntity
public class UserEntity extends AbstractEntity {

    public static final String USER_FRIENDSHIP_RELATION = "KNOWS";
    public static final String USER_FRIENDSHIP_REQUEST = "REQUESTS_FRIENDSHIP";
    public static final String USERS_DEBTS = "HAS_DEBT";
    //@Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String login;
    private String name;
    private String forename;
    private String password;
    @RelatedTo(type = USER_FRIENDSHIP_RELATION, elementClass = UserEntity.class, direction = Direction.BOTH)
    private Set<UserEntity> friends;
    @RelatedTo(type = USER_FRIENDSHIP_REQUEST, elementClass = UserEntity.class, direction = Direction.INCOMING)
    private Set<UserEntity> outgoingFriendshipInvitation;
    @RelatedTo(type = USER_FRIENDSHIP_REQUEST, elementClass = UserEntity.class, direction = Direction.INCOMING)
    private Set<UserEntity> friendshipInvitation;
    @RelatedTo(type = DebtEntity.DEBT_DEBTOR_RELATION, elementClass = DebtEntity.class, direction = Direction.INCOMING)
    private Set<DebtEntity> weOwesSbDebts;
    @RelatedTo(type = DebtEntity.DEBT_CREDITOR_RELATION, elementClass = DebtEntity.class, direction = Direction.INCOMING)
    private Set<DebtEntity> sbOwesToUsDebts;
    @RelatedTo(type = ConfirmationEntity.CONFIRMATION_RECEIVER_RELATION, elementClass = ConfirmationEntity.class, direction = Direction.INCOMING)
    private Set<ConfirmationEntity> confirmations;

    public UserEntity() {
        super();
    }

    public UserEntity(User user) {
        setEmail(user.getEmail());
        setLogin(user.getLogin());
        setPassword(user.getPassword());
        setName(user.getName());
        setForename(user.getForename());
    }

    public UserEntity(String email, String login, String password) {
        this.email = email;
        this.password = password;
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserEntity> getFriends() {
        return friends;
    }

    public void setFriends(Set<UserEntity> friends) {
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public Set<UserEntity> getFriendshipInvitation() {
        if (friendshipInvitation == null) {
            friendshipInvitation = new HashSet<UserEntity>();
        }
        return friendshipInvitation;
    }

    public void setFriendshipInvitation(Set<UserEntity> friendshipInvitation) {
        this.friendshipInvitation = friendshipInvitation;
    }

    public Set<ConfirmationEntity> getConfirmations() {
        if (confirmations == null) {
            confirmations = new HashSet<ConfirmationEntity>();
        }
        return confirmations;
    }

    public void setConfirmations(Set<ConfirmationEntity> confirmations) {
        this.confirmations = confirmations;
    }

    public Set<DebtEntity> getWeOwesSbDebts() {
        if (weOwesSbDebts == null) {
            weOwesSbDebts = new HashSet<DebtEntity>();
        }
        return weOwesSbDebts;
    }

    public void setWeOwesSbDebts(Set<DebtEntity> weOwesSbDebts) {
        this.weOwesSbDebts = weOwesSbDebts;
    }

    public Set<DebtEntity> getSbOwesToUsDebts() {
        if (sbOwesToUsDebts == null) {
            sbOwesToUsDebts = new HashSet<DebtEntity>();
        }
        return sbOwesToUsDebts;
    }

    public void setSbOwesToUsDebts(Set<DebtEntity> sbOwesToUsDebts) {
        this.sbOwesToUsDebts = sbOwesToUsDebts;
    }

    public Set<UserEntity> getOutgoingFriendshipInvitation() {
        if (outgoingFriendshipInvitation == null) {
            outgoingFriendshipInvitation = new HashSet<UserEntity>();
        }
        return outgoingFriendshipInvitation;
    }

    public void setOutgoingFriendshipInvitation(Set<UserEntity> outgoingFriendshipInvitation) {
        this.outgoingFriendshipInvitation = outgoingFriendshipInvitation;
    }
}
