package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("가격을 생성한다.")
    @Test
    void create() {
        //given
        BigDecimal element = new BigDecimal(1000);

        //when
        Price price = new Price(element);

        //then
        assertAll(
                () -> assertThat(price).isNotNull(),
                () -> assertThat(price.get()).isEqualTo(element)
        );
    }

    @DisplayName("금액이 없으면 가격을 생성할 수 없다.")
    @Test
    void create_invalidNull() {
        //when & then
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 0보다 작을 수 없습니다.");
    }

    @DisplayName("금액이 0보다 적으면 가격을 생성할 수 없다.")
    @Test
    void create_invalidMinus() {
        //given
        BigDecimal element = new BigDecimal(-1);

        //when & then
        assertThatThrownBy(() -> new Price(element))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격은 0보다 작을 수 없습니다.");
    }
}
