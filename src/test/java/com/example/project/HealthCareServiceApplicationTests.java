package com.example.project;


import com.example.project.Model.ApplicationUser;
import com.example.project.Model.Appointment;
import com.example.project.Model.Patient;
import com.example.project.repository.PatientRepository;
import com.example.project.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
@SpringBootTest
@Transactional
public class HealthCareServiceApplicationTests {
	public String token = "";
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private PatientRepository pr;

	@Autowired
	private AppointmentService appointmentService;


	private static MockMvc mockMvc;
	@Autowired
	WebApplicationContext context;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}


	@Test
	public void test1() throws Exception {
		ApplicationUser u = new ApplicationUser("user2", "user2@hcs.com", "user@2", "9999999999", "chennai");
		byte[] iJson = toJson(u);
		mockMvc.perform(post("/register")
						.content(iJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	public void test2() throws Exception {
		ApplicationUser u = new ApplicationUser("user1", "user1@hcs.com", "User@123", "9999999989", "chennai");
		byte[] iJson = toJson(u);
		MvcResult result = mockMvc.perform(post("/register")
						.content(iJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void test3() throws Exception {
		createUser();
		ApplicationUser u = new ApplicationUser("user1", "User@123");
		byte[] iJson = toJson(u);
		MvcResult result = mockMvc.perform(post("/signin")
						.content(iJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
		Patient p =  new Patient("patient2","patient2@hcs.com","8989998989", new Date());
		byte[] iJson1 = toJson(p);
		mockMvc.perform(get("/viewprofile/user1")
						.header(HttpHeaders.AUTHORIZATION,
								"Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.user_name").value("user1"));
	}

	public void createUser() throws Exception {
		ApplicationUser u = new ApplicationUser("user1", "user1@hcs.com", "User@123", "9999999989", "chennai");
		byte[] iJson = toJson(u);
		mockMvc.perform(post("/register")
				.content(iJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
	}

	@Test
	public void test4() throws Exception {
		createUser();
		ApplicationUser u = new ApplicationUser("user1", "User@123");
		byte[] iJson1 = toJson(u);
		MvcResult result = mockMvc.perform(post("/signin")
						.content(iJson1)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");

		registerPatientents(token);
		MvcResult result1 = mockMvc.perform(get("/patients/list")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		List<Patient> patient = pr.findAll();
		String patientid = patient.get(0).getPatient_Id();
		String patientid1 = patient.get(1).getPatient_Id();
		mockMvc.perform(get("/patients/view/" + patientid)
						.header(HttpHeaders.AUTHORIZATION,
								"Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.patient_name").value("patient1"));
		mockMvc.perform(get("/patients/view/" + patientid1)
						.header(HttpHeaders.AUTHORIZATION,
								"Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.patient_name").value("patient2"));


	}

	private void registerPatientents(String token1) throws Exception {
		Patient p =  new Patient("patient1","patient1@hcs.com","8989998989", new Date());
		byte[] iJson = toJson(p);
		mockMvc.perform(post("/patients/register")
						.header("Authorization", "Bearer " + token1)
						.content(iJson)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))

				.andExpect(status().isOk());
		Patient p1 = new Patient("patient2","patient2@hcs.com","8989998989", new Date());
		byte[] iJson2 = toJson(p1);
		mockMvc.perform(post("/patients/register")
						.header("Authorization", "Bearer " + token1)
						.content(iJson2)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))

				.andExpect(status().isOk());
	}

	@Test
	public void test5() throws Exception {
		createUser();
		ApplicationUser u = new ApplicationUser("user1", "User@123");
		byte[] iJson1 = toJson(u);
		MvcResult result = mockMvc.perform(post("/signin")
						.content(iJson1)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
		registerPatientents(token);
		List<Patient> patient = pr.findAll();
		String patientid = patient.get(0).getPatient_Id();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sd.parse("27/05/1996");
		Appointment A1 = new Appointment("disease1", date1, "High", patientid);
		byte[] iJson2 = toJson(A1);
		mockMvc.perform(post("/appointment/register")
						.header(HttpHeaders.AUTHORIZATION,
								"Bearer " + token)
						.content(iJson2)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	public void test6() throws Exception {
		createUser();
		ApplicationUser u = new ApplicationUser("user1", "User@123");
		byte[] iJson1 = toJson(u);
		MvcResult result = mockMvc.perform(post("/signin")
						.content(iJson1)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
		registerPatientents(token);
		List<Patient> patient = pr.findAll();
		String patientid = patient.get(0).getPatient_Id();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sd.parse("27/05/1996");
		Appointment A1 = new Appointment("disease1", date1, "High", patientid);
		byte[] iJson2 = toJson(A1);
		MvcResult result1 = mockMvc.perform(post("/appointment/register")
						.header(HttpHeaders.AUTHORIZATION,
								"Bearer " + token)
						.content(iJson2)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();


		assert appointmentService.getAllAppointments().size()==1;

		String appintId = appointmentService.getAllAppointments().get(0).getBooking_id();


//        mockMvc.perform(post("/appointment/delete/"+appintId).header(HttpHeaders.AUTHORIZATION,
//                "Bearer " + token));

		appointmentService.deleteAppointment(appintId);

		assert appointmentService.getAllAppointments().size()==0;

	}

	private byte[] toJson(Object r) throws Exception {
		ObjectMapper map = new ObjectMapper();
		return map.writeValueAsString(r).getBytes();
	}

}