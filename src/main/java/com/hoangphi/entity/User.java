package com.hoangphi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoangphi.entity.social.Comments;
import com.hoangphi.entity.social.LikedComments;
import com.hoangphi.entity.social.Likes;
import com.hoangphi.entity.social.Posts;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_id")
    private String id;
    private String username;
    @Nationalized
    private String fullname;
    private LocalDate birthday;
    private Boolean gender;
    private String phone;
    private String avatar;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private Boolean isActive;
    @JsonIgnore
    private Boolean isEmailVerified;
    @JsonIgnore
    private String token;
    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime tokenCreatedAt;
    @JsonIgnore
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "provider")
    private String provider;
    @Column(name = "display_name")
    private String displayName;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Adopt> adopts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Favourite> favourites;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Addresses> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Orders> orders;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Authorities> authorities;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Review> reviews;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<SearchHistory> searchHistories;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<RecentView> recentViews;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Posts> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Likes> likes;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Comments> comments;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<LikedComments> likedComments;



}
