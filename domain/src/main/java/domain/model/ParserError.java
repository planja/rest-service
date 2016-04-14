package domain.model;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Objects;

/**
 * Created by Никита on 12.04.2016.
 */
@Entity
@Table(name = "parser_errors")
public class ParserError {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "parser")
    private String parser;

    @Column(name = "error_text")
    private String errorText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public ParserError() {
    }

    public ParserError(Long id, Request request, String parser, String errorText) {
        this.id = id;
        this.request = request;
        this.parser = parser;
        this.errorText = errorText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParserError that = (ParserError) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (parser != null ? !parser.equals(that.parser) : that.parser != null) return false;
        return !(errorText != null ? !errorText.equals(that.errorText) : that.errorText != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (parser != null ? parser.hashCode() : 0);
        result = 31 * result + (errorText != null ? errorText.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ParserError{" +
                "id=" + id +
                ", request=" + request +
                ", parser='" + parser + '\'' +
                ", errorText='" + errorText + '\'' +
                '}';
    }
}
