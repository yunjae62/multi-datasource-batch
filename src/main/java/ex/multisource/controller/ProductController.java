package ex.multisource.controller;

import ex.multisource.domain.Product;
import ex.multisource.dto.ProductCreateReq;
import ex.multisource.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "product-controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody ProductCreateReq request) {
        Product product = productService.saveProduct(request);
        return ResponseEntity.ok(product);
    }
}