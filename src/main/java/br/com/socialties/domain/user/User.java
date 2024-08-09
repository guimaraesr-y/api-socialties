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

    @Column(unique = true)
    private String email;
    private String password;

    private Integer numFollowers;
    private Integer numFollowing;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<User> followers;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<User> following;

    @OneToMany
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Post> posts;

}
