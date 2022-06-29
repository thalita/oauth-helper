package com.pucsp.oauthhelper.controllers;

import com.pucsp.oauthhelper.domain.entities.Principal;
import com.pucsp.oauthhelper.domain.services.EmailService;
import com.pucsp.oauthhelper.domain.services.PhoneService;
import com.pucsp.oauthhelper.domain.services.PrincipalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class OtpPasswordControllerTest {

    @Mock
    private PrincipalRepository repository;

    @Mock
    private EmailService emailService;

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private OtpPasswordController otpPasswordController;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(otpPasswordController).build();
    }

    @Test
    public void deveRetornarResponseEntity201CreatedQuandoCanalEmail() throws Exception {
        this.mockMvc.perform(post("/otp/create")
                .contentType(APPLICATION_JSON)
                .content(buildCreateEmail()))
                .andExpect(status().isCreated());
    }

    @Test
    public void deveRetornarResponseEntity201CreatedQuandoCanalPhone() throws Exception {
        this.mockMvc.perform(post("/otp/create")
                .contentType(APPLICATION_JSON)
                .content(buildCreateTelefone()))
                .andExpect(status().isCreated());
    }


    @Test
    public void deveRetornarResponseEntity400BadRequestQuandoCanalInvalido() throws Exception {
        this.mockMvc.perform(post("/otp/create")
                .contentType(APPLICATION_JSON)
                .content(buildCreateInvalido()))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void deveRetornarResponseEntity204NoContentQuandoOTPValido() throws Exception {
        when(repository.findById(anyString())).thenReturn(of(buildPrincipal()));
        this.mockMvc.perform(post("/otp/verify")
                .contentType(APPLICATION_JSON)
                .content("{\"identifier\":\"123\",\"code\":\"p98dd\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deveRetornarResponseEntity401QuandoOTPInvalido() throws Exception {
        this.mockMvc.perform(post("/otp/verify")
                .contentType(APPLICATION_JSON)
                .content("{\"identifier\":\"123\",\"code\":\"p98dd\"}"))
                .andExpect(status().isUnauthorized());
    }



    private String buildCreateEmail() {
        return "{\"identifier\":\"123\",\"channel\":\"email\",\"email\": \"abc@gmail.com\", \"phone\":\"1111233445\"}";
    }

    private String buildCreateTelefone() {
        return "{\"identifier\":\"123\",\"channel\":\"phone\",\"email\": \"abc@gmail.com\", \"phone\":\"1111233445\"}";
    }

    private String buildCreateInvalido(){
        return "{\"identifier\":\"123\",\"channel\":\"bla\",\"email\": \"abc@gmail.com\", \"phone\":\"1111233445\"}";
    }

    private Principal buildPrincipal(){
        return Principal.builder()
                .code("123")
                .code("p98dd")
                .build();
    }
}