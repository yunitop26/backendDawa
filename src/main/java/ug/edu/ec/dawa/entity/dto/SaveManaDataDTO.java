package ug.edu.ec.dawa.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ug.edu.ec.dawa.entity.Person;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveManaDataDTO {
    private Person student;
    private Person teacher;
    private DocumentIn[] document;
    private String documentName;
    private Double note;
    private String observation;
}
