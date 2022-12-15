package kitchenpos.application;

import static kitchenpos.domain.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 유효성 검사")
@SpringBootTest
class TableValidatorTest {

    @Autowired
    private TableValidator tableValidator;

    @DisplayName("주문 테이블 비우기 API - ID에 해당하는 주문 테이블 단체 지정 이미 존재")
    @Test
    void changeEmpty_saved_order_table_table_group_id_is_not_null() {
        // given
        OrderTable savedOrderTable = savedOrderTable(1L, 1L);
        boolean statusCookingOrMeal = true;

        // when, then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(savedOrderTable, statusCookingOrMeal))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블 비우기 API - ID에 해당하는 주문 테이블 단체 지정 이미 존재")
    @Test
    void changeEmpty_exists_cooking_or_meal() {
        // given
        OrderTable savedOrderTable = savedOrderTable(1L, null);
        boolean statusCookingOrMeal = true;

        // when, then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(savedOrderTable, statusCookingOrMeal))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
