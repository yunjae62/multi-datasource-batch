package ex.multisource.service;

import ex.multisource.domain.Product;
import ex.multisource.dto.ProductCreateReq;
import ex.multisource.repository.ProductRepository;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "product-service")
@Service
@RequiredArgsConstructor
public class ProductService {

    private final DataSource dataSource;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        logDataSourceInfo();
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Transactional
    public Product saveProduct(ProductCreateReq request) {
        logDataSourceInfo();
        Product product = Product.create(request.name(), request.price());
        return productRepository.save(product);
    }

    private void logDataSourceInfo() {
        try (Connection connection = dataSource.getConnection()) {
            log.info("Connected to database: {}", connection.getMetaData().getURL());
        } catch (SQLException e) {
            log.error("Failed to log DataSource information", e);
        }
    }
}