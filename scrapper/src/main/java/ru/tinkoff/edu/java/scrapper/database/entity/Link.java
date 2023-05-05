package ru.tinkoff.edu.java.scrapper.database.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private long id;

    @Column(name = "url")
    private String url;

    @ManyToMany(mappedBy = "links", fetch = FetchType.EAGER)
    private Set<Chat> chats = new HashSet<>();

    @Column(name = "checked_at")
    private OffsetDateTime checkedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public void setChats(Set<Chat> chats) {
        this.chats = chats;
    }

    public OffsetDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(OffsetDateTime checkedAT) {
        this.checkedAt = checkedAT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return id == link.id && Objects.equals(url, link.url) && Objects.equals(checkedAt, link.checkedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, checkedAt);
    }
}
