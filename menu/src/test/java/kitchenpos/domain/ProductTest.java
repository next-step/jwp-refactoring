package kitchenpos.domain;

import kitchenpos.dao.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 생성한다.")
    @Test
    void save() {
        // when
        Product 마늘치킨 = productRepository.save(new Product("마늘치킨", 1000));

        // then
        assertThat(마늘치킨.getId()).isNotNull();
    }

    @DisplayName("이름을 입력하지 않으면 상품을 생성할 수 없다.")
    @Test
    void save_throwException_givenEmptyName() {
        // when
        // then
        assertThatThrownBy(() -> productRepository.save(new Product(null, 1000)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("0원 보다 작은 금액으로 상품을 생성할 수 없다.")
    @Test
    void save_throwException_givenPriceLessThanZero() {
        // when
        // then
        assertThatThrownBy(() -> productRepository.save(new Product("마늘치킨", -1000)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
