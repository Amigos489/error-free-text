package balabol.error.free.text.entities;

import balabol.error.free.text.enums.LanguageType;
import balabol.error.free.text.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(name = "text",
            nullable = false)
    private String text;

    @Column(name = "status",
            nullable = false,
            length = 20)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "language",
            nullable = false,
            length = 2)
    @Enumerated(EnumType.STRING)
    private LanguageType language;

    @Column(name = "error_message")
    private String errorMessage;

    public Task(String text,
                LanguageType language,
                TaskStatus status) {
        this.text = text;
        this.language = language;
        this.status = status;
    }
}
