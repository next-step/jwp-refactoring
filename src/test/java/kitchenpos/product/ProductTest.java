package kitchenpos.product;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품을 등록합니다.")
    void save() {
        String name = "치킨";
        Product product = ProductTestSupport.createProduct(name, BigDecimal.valueOf(15000));

        Product savedProduct = this.productRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("상품을 조회합니다")
    void findById() {
        String name = "치킨";
        Product product = ProductTestSupport.createProduct(name, BigDecimal.valueOf(15000));

        Product savedProduct = this.productRepository.save(product);

        Product foundProduct = this.productRepository.findById(savedProduct.getId()).get();

        assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
        assertThat(foundProduct.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("전체 상품을 조회합니다.")
    void findAll() {
        List<Product> products = this.productRepository.findAll();

        assertThat(products).hasSize(6);
    }
}
