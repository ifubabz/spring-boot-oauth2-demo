package com.example.auth.account;

import com.example.auth.oauth.SocialOAuthProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter @EqualsAndHashCode(of="id")
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SocialOAuthProvider provider;

    private String imageUrl;

    @Builder
    public Account(String name, String email, SocialOAuthProvider provider, String imageUrl){
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.imageUrl = imageUrl;
    }
}
