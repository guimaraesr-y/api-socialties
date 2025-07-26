package br.com.socialties.domain.comment;

import br.com.socialties.domain.post.Post;
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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String text;

    @ManyToOne
    private User author;

    private Integer likesCount = 0;
    private Integer dislikesCount = 0;

    @OneToMany
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> likes = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> dislikes = new ArrayList<>();

    @ManyToOne
    private Post post;

}
