package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domian.Quantity;
import kitchenpos.error.InvalidRequestException;

@DisplayName("상품id 수량 일급 컬렉션")
class QuantitiesTest {

    Map<Long, Quantity> quantityMap;

    @BeforeEach
    void setup() {
        quantityMap = new HashMap<>();
        quantityMap.put(1L, new Quantity(1L));
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given
        // when
        Quantities quantities = new Quantities(quantityMap, 1);
        // then
        assertThat(quantities).isNotNull();
        assertThat(quantities.size()).isEqualTo(1);
    }

    @DisplayName("생성 실패 - 요청 개수가 다름")
    @Test
    void createFailedByRequestSize() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Quantities(quantityMap, 2))
                .isInstanceOf(InvalidRequestException.class);
    }
}