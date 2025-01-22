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

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

	
	@Autowired
	TemaRepository temaRepository;
	
	
	@GetMapping
	public ResponseEntity<List<Tema>> listarPostagens() {
		return ResponseEntity.ok(temaRepository.findAll());
	}
	
	@GetMapping("/descricao={descricao}")
	public ResponseEntity<List<Tema>> listarPorTitulo(@PathVariable String descricao){
		return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
	}
	
	@GetMapping("/id={id}")
	public ResponseEntity<Tema> listarPorId(@PathVariable Long id){
		return ResponseEntity.ok(temaRepository.findById(id).orElse(null));
	}
	
	@PostMapping("/post")
	public ResponseEntity<Tema> salvarPostagem(@RequestBody Tema tema){
		return ResponseEntity.ok(temaRepository.save(tema));
	}
	
	@PutMapping("/put")
	public ResponseEntity<Tema> updatePostagem(@RequestBody Tema tema){
		return ResponseEntity.ok(temaRepository.save(tema));
	}
	
	@DeleteMapping("/delete/id={id}")
	public ResponseEntity<String> deletePostagem(@PathVariable Long id){
		if(temaRepository.existsById(id)) {
			temaRepository.deleteById(id);
			return ResponseEntity.ok("Deletado com Sucesso!.");
		}else {
			return ResponseEntity.badRequest().body("O ID informado não existe.");
		}
	}
}
