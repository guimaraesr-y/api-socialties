package br.com.socialties.domain.post;

import br.com.socialties.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private List<String> contentPaths;

    @ManyToOne
    private User author;

    private Integer likesCount;
    private Integer dislikesCount;

    @OneToMany
    private List<User> likes;

    @OneToMany
    private List<User> dislikes;

}
