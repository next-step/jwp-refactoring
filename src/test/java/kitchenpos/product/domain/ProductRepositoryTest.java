package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("생성한 상품을 저장 한다")
    public void createProduct() {
        //given
        String name = "치킨";
        BigDecimal price = new BigDecimal(20000);
        Product product = new Product(name, price);

        // when
        Product saveProduct = productRepository.save(product);

        // then
        assertThat(saveProduct).isEqualTo(product);
    }

    @Test
    @DisplayName("상품 리스트를 가져온다")
    public void selectProductList() {
        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).isNotEmpty();
        for (Product product : products) {
            assertThat(product.id()).isNotNull();
        }
    }
}
