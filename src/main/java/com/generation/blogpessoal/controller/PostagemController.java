package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

	
	@Autowired
	PostagemRepository postagemRepository;
	
	
	@GetMapping
	public ResponseEntity<List<Postagem>> listarPostagens() {
		return ResponseEntity.ok(postagemRepository.findAll());
	}
	
	@GetMapping("/titulo={titulo}")
	public ResponseEntity<List<Postagem>> listarPorTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	@GetMapping("/id={id}")
	public ResponseEntity<Postagem> listarPorId(@PathVariable Long id){
		return ResponseEntity.ok(postagemRepository.findById(id).orElse(null));
	}
	
	@PostMapping("/post")
	public ResponseEntity<Postagem> salvarPostagem(@RequestBody Postagem postagem){
		return ResponseEntity.ok(postagemRepository.save(postagem));
	}
	
	@PutMapping("/post")
	public ResponseEntity<Postagem> updatePostagem(@RequestBody Postagem postagem){
		return ResponseEntity.ok(postagemRepository.save(postagem));
	}
	
	@DeleteMapping("/delete/id={id}")
	public ResponseEntity<String> deletePostagem(@PathVariable Long id){
		if(postagemRepository.existsById(id)) {
			postagemRepository.deleteById(id);
			return ResponseEntity.ok("Deletado com Sucesso!.");
		}else {
			return ResponseEntity.badRequest().body("O ID informado não existe.");
		}
	}
}
