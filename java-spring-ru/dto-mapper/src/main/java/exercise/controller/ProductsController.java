package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashSet;
import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @GetMapping("")
    public List<ProductDTO> showAll() {
        var result = productRepository.findAll();
        HashSet<ProductDTO> returnResult = new HashSet<>();
        result.forEach(product -> {
            returnResult.add(productMapper.map(product));
        });
        return returnResult.stream().toList();
    }

    @GetMapping("/{id}")
    public ProductDTO show (@PathVariable long id) {
        var result = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        return productMapper.map(result);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create (@RequestBody ProductCreateDTO product) {
        var productToCreate = productMapper.map(product);
        var result = productRepository.save(productToCreate);
        return productMapper.map(result);
    }

    @PutMapping("/{id}")
    public ProductDTO update (@PathVariable long id, @RequestBody ProductUpdateDTO product) {
        var foundProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        productMapper.update(product, foundProduct);
        var updatedProduct = productRepository.save(foundProduct);
        return productMapper.map(updatedProduct);
    }
    // END
}
