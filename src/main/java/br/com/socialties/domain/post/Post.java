package br.com.socialties.domain.post;

import br.com.socialties.domain.comment.Comment;
import br.com.socialties.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
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
    private List<String> contentPaths = new ArrayList<>();

    @ManyToOne
    private User author;

    private Integer likesCount = 0;
    private Integer dislikesCount = 0;
    private Integer commentsCount = 0;

    @OneToMany
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> likes = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> dislikes = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> comments = new ArrayList<>();

    public Boolean like(User user) {
        if (likes.contains(user)) {
            likes.remove(user);
            likesCount -= 1;
            return false;
        }

        if (dislikes.contains(user)) {
            dislikes.remove(user);
            dislikesCount -= 1;
        }

        likes.add(user);
        likesCount += 1;
        return true;
    }

    public Boolean dislike(User user) {
        if (dislikes.contains(user)) {
            dislikes.remove(user);
            dislikesCount -= 1;
            return false;
        }

        if (likes.contains(user)) {
            likes.remove(user);
            likesCount -= 1;
        }

        dislikes.add(user);
        dislikesCount += 1;
        return true;
    }

}
