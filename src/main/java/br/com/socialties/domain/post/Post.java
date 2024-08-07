package br.com.socialties.domain.post;

import br.com.socialties.domain.post.comment.Comment;
import br.com.socialties.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private Integer commentsCount;

    @OneToMany
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> likes;

    @OneToMany
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> dislikes;

    @OneToMany
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> comments;

}
