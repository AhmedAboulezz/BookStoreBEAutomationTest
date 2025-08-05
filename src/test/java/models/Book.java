package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title; // nullable

    @JsonProperty("description")
    private String description; // nullable

    @JsonProperty("pageCount")
    private Integer pageCount;

    @JsonProperty("excerpt")
    private String excerpt; // nullable

    @JsonProperty("publishDate")
    private OffsetDateTime publishDate; // format: date-time

    public Book() {}

    public Book(Integer id, String title, String description, Integer pageCount, String excerpt, OffsetDateTime publishDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pageCount = pageCount;
        this.excerpt = excerpt;
        this.publishDate = publishDate;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public Book setId(Integer id) { this.id = id; return this; }

    public String getTitle() { return title; }
    public Book setTitle(String title) { this.title = title; return this; }

    public String getDescription() { return description; }
    public Book setDescription(String description) { this.description = description; return this; }

    public Integer getPageCount() { return pageCount; }
    public Book setPageCount(Integer pageCount) { this.pageCount = pageCount; return this; }

    public String getExcerpt() { return excerpt; }
    public Book setExcerpt(String excerpt) { this.excerpt = excerpt; return this; }

    public OffsetDateTime getPublishDate() { return publishDate; }
    public Book setPublishDate(OffsetDateTime publishDate) { this.publishDate = publishDate; return this; }

    // equals/hashCode/toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
               Objects.equals(title, book.title) &&
               Objects.equals(description, book.description) &&
               Objects.equals(pageCount, book.pageCount) &&
               Objects.equals(excerpt, book.excerpt) &&
               Objects.equals(publishDate, book.publishDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, pageCount, excerpt, publishDate);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pageCount=" + pageCount +
                ", excerpt='" + excerpt + '\'' +
                ", publishDate=" + "2025-08-04T14:26:00.228Z" +
                '}';
    }
}
