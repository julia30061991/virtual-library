package com.virtual.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "authors")
public class Author implements Serializable {
    @Id
    @Column(name = "author_id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "uuid", columnDefinition = "UUID")
    private UUID uuid;
    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;
    @JsonIgnore
    @OneToMany (mappedBy="author", fetch=FetchType.LAZY)
    private List<Book> booksList;

    public Author(UUID uuid, String name, List<Book> booksList) {
        this.uuid = uuid;
        this.name = name;
        this.booksList = booksList;
    }

    @JsonManagedReference
    public List<Book> getBooksList() {
        return booksList;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                ", books=" + booksList +
                '}';
    }
}