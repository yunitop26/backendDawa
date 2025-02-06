package ug.edu.ec.dawa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ug.edu.ec.dawa.entity.ManagerData;
import ug.edu.ec.dawa.entity.Person;
import ug.edu.ec.dawa.entity.dto.DocumentIn;
import ug.edu.ec.dawa.entity.dto.SaveManaDataDTO;
import ug.edu.ec.dawa.repository.ManagerDataRepository;
import ug.edu.ec.dawa.repository.PersonRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemService {
    private final ManagerDataRepository managerDataRepository;
    private final PersonRepository personRepository;

    @Autowired
    public SystemService(ManagerDataRepository managerDataRepository,
                         PersonRepository personRepository) {
        this.managerDataRepository = managerDataRepository;
        this.personRepository = personRepository;
    }

    public String saveManagerData(SaveManaDataDTO dtoData) {
        DocumentIn[] documentos = dtoData.getDocument();

        // Validación: asegúrate de que se proporcione al menos un documento
        if (documentos.length == 0) {
            throw new IllegalArgumentException("No se proporcionó ningún documento para guardar.");
        }

        // Conversión del primer documento
        DocumentIn documento = documentos[0];
        byte[] documentBytes = saveDocument(documento);

        // Crear instancia de ManagerData y asignar valores
        ManagerData data = new ManagerData();
        data.setStudent(dtoData.getStudent());
        data.setTeacher(dtoData.getTeacher());
        data.setDocument(documentBytes); // Guardar el documento como bytes
        data.setDocumentName(documento.getName());
        data.setNote(dtoData.getNote());
        data.setObservation(dtoData.getObservation());

        // Guarda la entidad ManagerData en la base de datos
        managerDataRepository.save(data);

        return "Datos guardados correctamente";
    }

    public byte[] saveDocument(DocumentIn document) {
        if (document.getBase64() == null || document.getBase64().isEmpty()) {
            throw new IllegalArgumentException("El documento Base64 está vacío.");
        }

        // Convertir Base64 a byte[]
        byte[] documentBytes = java.util.Base64.getDecoder().decode(document.getBase64());

        // Validación adicional: limitar tamaño del documento (ejemplo: 16 MB = 16 * 1024 * 1024 bytes)
        int maxFileSize = 16 * 1024 * 1024; // 16 MB
        if (documentBytes.length > maxFileSize) {
            throw new IllegalArgumentException(
                    "El archivo es demasiado grande. Tamaño máximo permitido: 16 MB.");
        }

        return documentBytes;
    }



    public List<Person> getStudents() {
        return personRepository.findAll().stream()
                .filter(person -> person.getRole() != null && person.getRole().toLowerCase().contains("estudiante"))
                .collect(Collectors.toList());
    }

    public List<Person> getTeacher() {
        System.out.println();
        return personRepository.findAll().stream()
                .filter(person -> person.getRole() != null && person.getRole().toLowerCase().contains("profesor"))
                .collect(Collectors.toList());
    }

    public List<ManagerData> getAllManagerData() {
        return managerDataRepository.findAll();
    }



}
