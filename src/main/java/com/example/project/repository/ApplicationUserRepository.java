package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.Model.Appointment;
import org.springframework.stereotype.Repository;


public interface ApplicationUserRepository extends JpaRepository<Appointment,String>{

}
