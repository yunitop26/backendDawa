package ug.edu.ec.dawa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Person student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Person teacher;

    @Lob
    @Column(name = "document", columnDefinition = "MEDIUMBLOB") // Cambiar BLOB a MEDIUMBLOB
    private byte[] document;

    private String documentName;

    private Double note;

    private String observation;


}
