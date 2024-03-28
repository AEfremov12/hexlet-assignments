package exercise.controller;

import java.util.HashSet;
import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.CategoryMapper;
import exercise.mapper.ProductMapper;
import exercise.mapper.ReferenceMapper;
import exercise.model.Product;
import exercise.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN

    @Autowired
    private ReferenceMapper referenceMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("")
    public List<ProductDTO> showAll(){
        var products = productRepository.findAll();
        return products.stream()
                .map(p -> productMapper.map(p))
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDTO show(@PathVariable long id){
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id " + id));
        return productMapper.map(product);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@Valid @RequestBody ProductCreateDTO createDTO){
        var savedResult = productRepository.save(productMapper.map(createDTO));
        return productMapper.map(savedResult);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable long id,@Valid @RequestBody ProductUpdateDTO updateDTO){
        var foundProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
//        var originalCategory = categoryRepository.findById(foundProduct.getCategory().getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        productMapper.update(updateDTO, foundProduct);
//        var newCategory = categoryRepository.findById(updateDTO.getCategoryId().get())
//                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
//        if (originalCategory != newCategory) {
//            foundProduct.setCategory(categoryRepository.findById(newCategory.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Not found")));
//        }
        var updatedProduct = productRepository.save(foundProduct);
        return productMapper.map(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id){
        productRepository.deleteById(id);
    }
    // END
}
