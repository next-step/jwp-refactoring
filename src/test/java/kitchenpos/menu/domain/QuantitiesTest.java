package kitchenpos.menu.domain;

import kitchenpos.common.domian.Quantity;
import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorInfo.NOT_EQUAL_REQUEST_SIZE.message());
    }
}