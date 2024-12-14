package br.com.socialties.domain.user;

import br.com.socialties.domain.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String profilePicturePath;

    @Column(unique = true)
    private String email;
    private String password;
    private Boolean isPublic = true;

    private Integer numFollowers;
    private Integer numFollowing;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<User> followers;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<User> following;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<User> requestFollowers;

    @OneToMany
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Post> posts;

    public void follow(User user) {
        if (isFollowing(user)) return;
        if (!user.isPublic) user.addRequestFollower(this);

        following.add(user);
        user.followers.add(this);
        incrementFollowing();
        user.incrementFollowers();
    }

    public void unfollow(User user) {
        if (!isFollowing(user)) return;

        following.remove(user);
        user.followers.remove(this);
        decrementFollowing();
        user.decrementFollowers();
    }

    public boolean isFollowing(User user) {
        return following.contains(user);
    }

    public boolean isFollower(User user) {
        return followers.contains(user);
    }

    public boolean isRequestFollower(User user) {
        return requestFollowers.contains(user);
    }

    public void addRequestFollower(User user) {
        requestFollowers.add(user);
    }

    public void removeRequestFollower(User user) {
        requestFollowers.remove(user);
    }

    public void acceptFollower(User user) {
        followers.add(user);
        user.following.add(this);
        requestFollowers.remove(user);
        incrementFollowing();
        user.incrementFollowers();
    }

    public void incrementFollowers() {
        numFollowers++;
    }

    public void incrementFollowing() {
        numFollowing++;
    }

    public void decrementFollowers() {
        numFollowers--;
    }

    public void decrementFollowing() {
        numFollowing--;
    }

}
