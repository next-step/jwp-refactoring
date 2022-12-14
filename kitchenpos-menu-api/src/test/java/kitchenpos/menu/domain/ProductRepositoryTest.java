package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductRepository 테스트")
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품_Id_목록을_입력받아_상품_목록_조회() {
        Product 스테이크 = productRepository.save(new Product("스테이크", new BigDecimal(25000)));
        Product 스파게티 = productRepository.save(new Product("스파게티", new BigDecimal(18000)));
        Product 에이드 = productRepository.save(new Product("에이드", new BigDecimal(3500)));

        List<Product> products = productRepository.findAllById(
                Arrays.asList(스테이크.getId(), 스파게티.getId(), 에이드.getId()));

        assertThat(products).hasSize(3);
    }
}
