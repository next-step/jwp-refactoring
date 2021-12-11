package kitchenpos.menu.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.fixture.ProductDomainFixture.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 관리")
class ProductTest {

    @Nested
    @DisplayName("상품 생성")
    class CreateProduct {
        @Test
        @DisplayName("성공")
        public void create() {
            // given
            String name = "양념 치킨";
            BigDecimal price = BigDecimal.valueOf(15000);

            // when
            Product actual = product(name, price);

            // then
            assertAll(
                    () -> assertThat(actual.getName()).isEqualTo(name),
                    () -> assertThat(actual.getProductPrice().getPrice()).isEqualTo(price)
            );
        }

        @Test
        @DisplayName("실패 - 상품명이 없음")
        public void failNameEmpty() {
            // given
            String name = "";
            BigDecimal price = BigDecimal.valueOf(15000);

            // when
            Assertions.assertThatThrownBy(() -> {
                Product actual = product(name, price);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 금액이 없음")
        public void failPriceEmpty() {
            // given
            String name = "양념 치킨";

            // when
            Assertions.assertThatThrownBy(() -> {
                Product actual = product(name, null);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("실패 - 상품의 가격이 0보다 작음")
        public void failPriceIllegal() {
            // given
            String name = "양념 치킨";
            BigDecimal price = BigDecimal.valueOf(-1);

            // when
            Assertions.assertThatThrownBy(() -> {
                Product actual = product(name, price);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
