package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductEntityTest {

  @DisplayName("상품 명과 가격을 입력받아 상품을 만든다.")
  @Test
  void createTest() {
    //given
    String productName = "상품";
    Double productPrice = 1_000D;

    //when
    ProductEntity productEntity = new ProductEntity(productName, productPrice);

    //then
    assertAll(
        () -> assertThat(productEntity.getName()).isEqualTo(productName),
        () -> assertThat(productEntity.getPrice()).isEqualTo(BigDecimal.valueOf(productPrice))
    );
  }

  @DisplayName("가격은 0이상이어야 한다.")
  @NullSource
  @ValueSource(doubles = {-0.1, -1})
  @ParameterizedTest
  void ceateFailTest(Double givenPrice) {
    //given
    String productName = "상품";

    //when & then
    assertThatThrownBy(() -> new ProductEntity(productName, givenPrice)).isInstanceOf(IllegalArgumentException.class);
  }
}
