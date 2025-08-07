package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("idBook")
    private Integer idBook;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    public Author() {}

    public Author(Integer id, Integer idBook, String firstName, String lastName) {
        this.id = id;
        this.idBook = idBook;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public Author setId(Integer id) { this.id = id; return this; }

    public Integer getIdBook() { return idBook; }
    public Author setIdBook(Integer idBook) { this.idBook = idBook; return this; }

    public String getFirstName() { return firstName; }
    public Author setFirstName(String firstName) { this.firstName = firstName; return this; }

    public String getLastName() { return lastName; }
    public Author setLastName(String lastName) { this.lastName = lastName; return this; }


}
