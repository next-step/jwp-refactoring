package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.application.OrderSupport;
import kitchenpos.table.application.TableValidator;
import kitchenpos.table.domain.OrderTable;

@DisplayName("주문 테이블 유효성 검사")
@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @Mock
    private OrderSupport orderSupport;

    @InjectMocks
    private TableValidator tableValidator;

    @DisplayName("주문 테이블 비우기 - ID에 해당하는 주문 테이블 단체 지정 이미 존재")
    @Test
    void changeEmpty_saved_order_table_table_group_id_is_not_null() {
        // given
        OrderTable savedOrderTable = savedOrderTable(1L, 1L);

        // when, then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블 비우기 - 조리중, 식사중 상태")
    @Test
    void changeEmpty_exists_cooking_or_meal() {
        // given
        OrderTable savedOrderTable = savedOrderTable(1L, null);
        given(orderSupport.validateOrderChangeable(1L)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
