package kitchenpos.product.application;

import kitchenpos.ServiceTest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 서비스 테스트")
class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        Product product = new Product("매운양념치킨", 18_000);

        // when
        Product savedProduct = productService.create(product);

        // then
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice().compareTo(product.getPrice())).isZero()
        );
    }

    @Test
    @DisplayName("0보다 작은 가격으로 상품을 등록하면 예외를 발생한다.")
    void createThrowException() {
        // given
        Product product = new Product("매운양념치킨", -1);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> productService.create(product));
    }

    @Test
    @DisplayName("상품의 목록을 조회한다.")
    void list() {
        // when
        List<Product> products = productService.list();

        // then
        assertThat(products.size()).isPositive();
    }
}
