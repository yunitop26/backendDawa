package ug.edu.ec.dawa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ug.edu.ec.dawa.entity.ManagerData;
import ug.edu.ec.dawa.entity.Person;
import ug.edu.ec.dawa.entity.dto.SaveManaDataDTO;
import ug.edu.ec.dawa.service.SystemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/v1/system")
public class SystemController {
    private final SystemService systemService;

    @Autowired
    public SystemController (SystemService service){
        this.systemService = service;
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveData(@RequestBody SaveManaDataDTO managerData) {
        try {
            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", systemService.saveManagerData(managerData));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Respuesta de error
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }



    @GetMapping("/get-students")
    public ResponseEntity<List<Person>> getStudents() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(systemService.getStudents());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los estudiantes: " + e.getMessage());
        }
    }

    @GetMapping("/get-teacher")
    public ResponseEntity<List<Person>> getTeacher() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(systemService.getTeacher());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los estudiantes: " + e.getMessage());
        }
    }

    @GetMapping("/manager-data")
    public ResponseEntity<List<ManagerData>> getAllManagerData() {
        try {
            // Obtener todos los datos desde el servicio
            List<ManagerData> allData = systemService.getAllManagerData();
            return ResponseEntity.ok(allData);
        } catch (Exception e) {
            // Manejar errores en caso de que ocurra algo inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
