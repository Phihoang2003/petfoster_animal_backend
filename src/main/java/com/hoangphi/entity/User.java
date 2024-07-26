package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoangphi.entity.social.Comments;
import com.hoangphi.entity.social.LikedComments;
import com.hoangphi.entity.social.Likes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;
import org.hibernate.criterion.Order;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_id")
    private String id;
    private String username;
    @Nationalized
    private String fullname;
    private Date birthday;
    private Boolean gender;
    private String phone;
    private String avatar;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private Date createdAt;
    @JsonIgnore
    private Boolean isActive;
    @JsonIgnore
    private Boolean isEmailVerified;
    @JsonIgnore
    private String token;
    @JsonIgnore
    private Date tokenCreatedAt;
    @JsonIgnore
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "provider")
    private String provider;
    @Column(name = "display_name")
    private String displayName;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Adopt> adopts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Favourite> favourites;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Addresses> addresses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Authorities> authorities;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SearchHistory> searchHistories;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RecentView> recentViews;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Posts> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Likes> likes;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comments> comments;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LikedComments> likedComments;

}
