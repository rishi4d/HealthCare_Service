package com.example.project.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.project.Model.Appointment;
import com.example.project.repository.AppointmentRepository;
import com.example.project.service.AppointmentService;

@RestController
@RequestMapping("appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("register")
    public ResponseEntity<Appointment> registerAppointment(@RequestBody Appointment appointment){
        return ResponseEntity.ok(appointmentService.save(appointment));
    }

    @GetMapping("list")
    public ResponseEntity<List<Appointment>> listAppointment(){
        return new ResponseEntity<List<Appointment>>(appointmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("view/{appointmentId}")
    public ResponseEntity<Appointment> viewAppointment(@PathVariable String appointmentId){
        return ResponseEntity.ok(appointmentService.findById(appointmentId).get());
    }

    @GetMapping("list/{patientid}")
    public ResponseEntity<List<Appointment>> listAppointmentofPatient(@PathVariable String patientId){
        return new ResponseEntity<List<Appointment>>(AppointmentRepository.findByPatientId(patientId), HttpStatus.OK);
    }

    @DeleteMapping("delete/{appointmentId}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable String appointmentId){
        return ResponseEntity.ok(appointmentService.deleteById(appointmentId));
    }

}
