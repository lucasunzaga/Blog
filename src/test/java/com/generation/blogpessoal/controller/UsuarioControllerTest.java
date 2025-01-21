package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(new Usuario(null, "Root", "root@root.com", "rootroot", "-"));
	}
	
	@Test
	@DisplayName("Cadastrar um Usuário.")
	public void deveCriarUmUsuario() {
		HttpEntity<Usuario> corpoRequisicao = 
				new HttpEntity<Usuario>(new Usuario(null, "João", "joao@hotmail.com", "12345678", "-"));
		
		ResponseEntity<Usuario> resposta = 
				testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(null, "maria", "maria@hotmail.com", "12345678", "-"));
		
		HttpEntity<Usuario> body = 
				new HttpEntity<Usuario>(new Usuario(null, "maria", "maria@hotmail.com", "12345678", "-")); 
		
		ResponseEntity<Usuario> response = 
				testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, body, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	@DisplayName("Atualizar um Usuário.")
	public void deveAtualizarUsuario() {
		Optional<Usuario> usuarioCadastrado = 
				usuarioService.cadastrarUsuario(new Usuario(null, "silva", "silva@hotmail.com", "12345678", "-"));
		
		Usuario usuario = new Usuario(usuarioCadastrado.get().getId(), "silva", "silva@hotmail.com", "12345678", "-");
		
		HttpEntity<Usuario> body = 
				new HttpEntity<Usuario>(usuario);
		
		ResponseEntity<Usuario> response = 
				testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, body, Usuario.class);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	@DisplayName("Listar todos os Usuarios")
	public void deveListarUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(null, "joelson", "joelson@joels.com", "12345678", " "));

		usuarioService.cadastrarUsuario(new Usuario(null, "joels", "joels@joels.com", "12345678", " "));

		ResponseEntity<String> response = 
				testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	@DisplayName("Não deve criar ao tentar Atualizar Usuario Inexistente.")
	public void naoDeveCriarAoAtualizar() {
		
		HttpEntity<Usuario> body = new HttpEntity<Usuario>(new Usuario(90l, "lima", "lima@lima.com", "12345678", " "));

		ResponseEntity<Usuario> response = 
				testRestTemplate	
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, body, Usuario.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	
}
