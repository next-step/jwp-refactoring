package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;

@SpringBootTest
class ProductMenuServiceTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMenuService productMenuService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = productRepository.save(new Product("A", BigDecimal.valueOf(10_000.00)));
    }

    @Test
    @DisplayName("구현체를 주입받아 실제로 계산로직 동작 확인")
    void calculatePrice() {
        // when
        BigDecimal totalPrice = productMenuService.calculateProductsPrice(product.getId(), 2L);

        // then
        assertThat(totalPrice.compareTo(BigDecimal.valueOf(20_000.00))).isZero();
    }

    @Test
    @DisplayName("구현체를 주입받아 실제로 에외상황 동작 확인")
    void calculateProductsPrice_notFound_exception() {
        // then
        assertThatThrownBy(() -> productMenuService.calculateProductsPrice(10L, 2L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("조회된 제품이 없습니다.");
    }
}
