package ex.multisource.service;

import ex.multisource.domain.Product;
import ex.multisource.dto.ProductCreateReq;
import ex.multisource.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "product-service")
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product saveProduct(ProductCreateReq request) {
        Product product = Product.create(request.name(), request.price());
        return productRepository.save(product);
    }
}
