package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @DisplayName("상품을 등록한다.")
  @Test
  void create() {
    // given
    Product product = new Product("사이다", BigDecimal.valueOf(1_000));

    // when
    Product savedProduct = productService.create(product);

    // then
    assertThat(savedProduct.getId()).isNotNull();
    assertThat(savedProduct.getName()).isEqualTo("사이다");
  }

  @DisplayName("상품 등록 예외 - 상품 금액은 0보다 커야 한다.")
  @Test
  void create_exception1() {
    // when, then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> productService.create(new Product("사이다", BigDecimal.valueOf(-1))))
        .withMessage("상품 금액은 0보다 커야 한다.");
  }

  @DisplayName("상품 목록을 조회한다.")
  @Test
  void list() {
    // when
    List<Product> list = productService.list();

    // then
    assertThat(list.size()).isGreaterThan(0);
  }
}
