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

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<ProductDTO> index() {
        var products = productRepository.findAll();
        return products.stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping(path = "/{id}")
    public ProductDTO show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        var productDTO = toDTO(product);
        return productDTO;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO productData) {
        var product = toEntity(productData);
        productRepository.save(product);
        var productDto = toDTO(product);
        return productDto;
    }

    // BEGIN
    @PutMapping("/{id}")
    public ProductDTO update (@PathVariable long id, @RequestBody ProductUpdateDTO product) {
        var foundProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        foundProduct.setTitle(product.getTitle());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setCreatedAt(product.getCreatedAt());
        foundProduct.setUpdatedAt(product.getUpdatedAt());

        var updatedProduct = productRepository.save(foundProduct);

        var returnResult = new ProductDTO();
        returnResult.setId(updatedProduct.getId());
        returnResult.setPrice(updatedProduct.getPrice());
        returnResult.setTitle(updatedProduct.getTitle());
        returnResult.setVendorCode(updatedProduct.getVendorCode());
        returnResult.setCreatedAt(updatedProduct.getCreatedAt());
        returnResult.setUpdatedAt(updatedProduct.getUpdatedAt());

        return returnResult;
    }
    // END

    private Product toEntity(ProductCreateDTO productDto) {
        var product = new Product();
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setVendorCode(productDto.getVendorCode());
        return product;
    }

    private ProductDTO toDTO(Product product) {
        var dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setVendorCode(product.getVendorCode());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
