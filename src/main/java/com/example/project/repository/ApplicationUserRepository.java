package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.Model.ApplicationUser;
import org.springframework.stereotype.Repository;

public interface ApplicationUserRepository  extends JpaRepository<ApplicationUser, String>{

}
