package kitchenpos.product;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductTest {

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("상품을 등록합니다.")
    void save() {
        String name = "치킨";
        Product product = ProductTestSupport.createProduct(name, BigDecimal.valueOf(15000));

        Product savedProduct = this.productDao.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("상품을 조회합니다")
    void findById() {
        String name = "치킨";
        Product product = ProductTestSupport.createProduct(name, BigDecimal.valueOf(15000));

        Product savedProduct = this.productDao.save(product);

        Product foundProduct = productDao.findById(savedProduct.getId()).get();

        assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
        assertThat(foundProduct.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("전체 상품을 조회합니다.")
    void findAll() {
        List<Product> products = this.productDao.findAll();

        assertThat(products).hasSize(6);
    }
}
