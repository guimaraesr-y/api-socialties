package br.com.socialties.domain.post.comment;

import br.com.socialties.domain.post.Post;
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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String text;

    @ManyToOne
    private User author;

    private Integer likesCount;
    private Integer dislikesCount;

    @OneToMany
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> likes;

    @OneToMany
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> dislikes;

    @ManyToOne
    private Post post;

}
