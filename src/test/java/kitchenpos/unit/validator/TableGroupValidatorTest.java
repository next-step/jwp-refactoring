package kitchenpos.unit.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.port.OrderPort;
import kitchenpos.table.port.OrderTablePort;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @Mock
    private OrderPort orderPort;

    @Mock
    private OrderTablePort orderTablePort;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @Test
    @DisplayName("요청으로 들어온 주문테이블의 아이디는 비어있으면 안된다.")
    void validCheckOrderTableIdsIsEmpty() {
        assertThatThrownBy(() ->
                tableGroupValidator.validOrderTableIds(Arrays.asList())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("요청 으로 들어온 주문테이블 아이디의 크기는 2보다 작으면 안된다.")
    void validCheckOrderTableMisSize() {
        assertThatThrownBy(() ->
                tableGroupValidator.validOrderTableIds(Arrays.asList())
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @ParameterizedTest
    @DisplayName("주문 테이블을 취소할 때 상태는 조리중이거나 요리중이면 안된다")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void validUngroupable(OrderStatus status) {
        Order 주문 = new Order(1L, 1L, status, null);
        OrderTable 주문테이블 = new OrderTable(1L, null, 0, false);

        given(orderTablePort.findAllByTableGroupId(any())).willReturn(Arrays.asList(주문테이블));
        given(orderPort.findAllByOrderTableIdIn(any())).willReturn(Arrays.asList(주문));

        assertThatThrownBy(() ->
                tableGroupValidator.validCheckUngroup(new TableGroup())
        ).isInstanceOf(IllegalArgumentException.class);

    }
}
