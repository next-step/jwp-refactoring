package kitchenpos.Product.domain;

import kitchenpos.menugroup.domain.MenuGroupEntity;
import kitchenpos.product.domain.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProductEntityTest {

    @DisplayName("이름 없는 상품 객체는 생성 할 수 없다")
    @Test
    void createFailBecauseofNameNull() {
        //when && then
        assertThatThrownBy(() -> new ProductEntity(null, BigDecimal.valueOf(1000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품 이름은 필수로 입력되어야 합니다.");
    }

    @DisplayName("가격이 없거나 음수인 상품 객체는 생성 할 수 없다")
    @Test
    void createFailBecauseofnullPrice() {
        //when && then
        assertThatThrownBy(() -> new ProductEntity("테스트", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 없거나 음수인 상품은 등록할 수 없습니다.");

        //when && then
        assertThatThrownBy(() -> new ProductEntity("테스트",BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 없거나 음수인 상품은 등록할 수 없습니다.");
    }


}