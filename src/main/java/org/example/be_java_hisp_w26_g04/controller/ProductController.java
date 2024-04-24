package org.example.be_java_hisp_w26_g04.controller;

import org.example.be_java_hisp_w26_g04.model.Post;
import org.example.be_java_hisp_w26_g04.repository.seller.SellersRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    SellersRepositoryImp sellersRepositoryImp;
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody Post post){
        if(sellersRepositoryImp.save(post)){
            return ResponseEntity.ok().build();
        } else{
            return ResponseEntity.badRequest().build();
        }
    }

}