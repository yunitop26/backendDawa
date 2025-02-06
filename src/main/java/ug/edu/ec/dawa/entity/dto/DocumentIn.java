package ug.edu.ec.dawa.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentIn {
    private String name;
    private String base64;
    private String type;
}

