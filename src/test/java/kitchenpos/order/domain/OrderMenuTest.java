package kitchenpos.order.domain;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderMenu")
class OrderMenuTest {

    @DisplayName("생성")
    @Test
    void create() {
        assertThatNoException().isThrownBy(() -> OrderMenu.of(1L, new Name("A"), new Price(BigDecimal.ONE)));
    }

    @DisplayName("메뉴 아이디가 없을 수 없다.")
    @Test
    void create_menuId() {
        assertThatThrownBy(() -> OrderMenu.of(null, new Name("A"), new Price(BigDecimal.ONE)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름이 없을 수 없다.")
    @Test
    void create_name() {
        assertThatThrownBy(() -> OrderMenu.of(1L, null, new Price(BigDecimal.ONE)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 없을 수 없다.")
    @Test
    void create_price() {
        assertThatThrownBy(() -> OrderMenu.of(1L, new Name("A"), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

