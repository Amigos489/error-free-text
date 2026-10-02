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

    @Column(name = "text",
            nullable = false)
    private String text;

    @Column(name = "language",
            nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageType language;

    @Column(name = "status",
            nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    public Task(String text, LanguageType language, TaskStatus status) {
        this.text = text;
        this.language = language;
        this.status = status;
    }
}
