package com.virtual.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "books")
public class Book implements Serializable {
    @Id
    @Column(name = "book_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "book_uuid", columnDefinition = "UUID")
    private UUID uuid;
    @Column(name = "title", columnDefinition = "VARCHAR(255)")
    private String title;
    @Column(name = "data_publication", columnDefinition = "VARCHAR(255)")
    private LocalDate dataPublication;
    @ManyToOne (fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;

    public Book(UUID uuid, String title, LocalDate dataPublication) {
        this.uuid = uuid;
        this.title = title;
        this.dataPublication = dataPublication;
    }

    @JsonBackReference
    public Author getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", title='" + title + '\'' +
                ", dataPublication=" + dataPublication +
                "author=" + author +
                '}';
    }
}