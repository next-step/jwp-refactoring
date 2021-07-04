package kitchenpos.application;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderTableId;
import kitchenpos.dto.order.TableGroupRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroupRequest tableGroupRequest;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    public final long ORDER_TABLE_ID_1L = 1L;
    public final long ORDER_TABLE_ID_2L = 2L;
    private final long ANY_TABLE_GROUP_ID = 1L;

    @BeforeEach
    void setUp() {

        orderTable1 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable1, "id", ORDER_TABLE_ID_1L);
        orderTable2 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable2, "id", ORDER_TABLE_ID_2L);

        tableGroupRequest = new TableGroupRequest(new ArrayList<>());
    }

    @Test
    @DisplayName("단체 지정을 할 경우, 적어도 2개 이상의 주문된 테이블이 존재해야 한다.")
    void exception_create_test() {

        tableGroupRequest = new TableGroupRequest(new ArrayList<>());
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("should have over 2 orderTables");
    }

    @Test
    @DisplayName("단체 지정 시점에, 주문 테이블이 단체 지정시 주문받은 주문 테이블과 숫자가 맞지 않으면 지정할 수 없다.")
    void exception_orderTable() {
        given(orderTableRepository.findById(ORDER_TABLE_ID_1L)).willReturn(Optional.of(orderTable1));
        given(orderTableRepository.findById(ORDER_TABLE_ID_2L)).willReturn(Optional.of(orderTable2));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Lists.list(orderTable1));

        tableGroupRequest = new TableGroupRequest(Lists.list(new OrderTableId(ORDER_TABLE_ID_1L),
                new OrderTableId(ORDER_TABLE_ID_2L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not same as orderTable size");
    }

    @Test
    @DisplayName("단체 지정 시점에, 주문 테이블이 빈 테이블이 아니라면 단체 지정을 할 수 없다.")
    void exception2_orderTable() {
        given(orderTableRepository.findById(ORDER_TABLE_ID_1L)).willReturn(Optional.of(orderTable1));
        given(orderTableRepository.findById(ORDER_TABLE_ID_2L)).willReturn(Optional.of(orderTable2));
        given(orderTableRepository.findAllByIdIn(any()))
                .willReturn(Lists.list(orderTable1, orderTable2));

        orderTable1 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable1, "id", ORDER_TABLE_ID_1L);
        orderTable2 = OrderTable.of(10, false);
        ReflectionTestUtils.setField(orderTable2, "id", ORDER_TABLE_ID_2L);

        tableGroupRequest = new TableGroupRequest(Lists.list(new OrderTableId(ORDER_TABLE_ID_1L), new OrderTableId(ORDER_TABLE_ID_2L)));
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("savedOrderTable");
    }

    @Test
    @DisplayName("단체 지정을 취소할 수 있다.")
    void ungroup() {
        List<OrderTable> orderTables = Lists.list(orderTable1, orderTable2);
        given(orderTableRepository.findAllByTableGroupId(ANY_TABLE_GROUP_ID))
                .willReturn(orderTables);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Lists.list(ORDER_TABLE_ID_1L, ORDER_TABLE_ID_2L),
                Lists.list(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(false);

        tableGroupService.ungroup(ANY_TABLE_GROUP_ID);

        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
        verify(orderTableRepository).save(orderTable1);
        verify(orderTableRepository).save(orderTable2);
    }

    @Test
    @DisplayName("주문이 식사 또는 조리의 경우 단체 지정을 취소할 수 없다.")
    void exception_upgroup() {
        List<OrderTable> orderTables = Lists.list(orderTable1, orderTable2);
        given(orderTableRepository.findAllByTableGroupId(ANY_TABLE_GROUP_ID))
                .willReturn(orderTables);

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Lists.list(ORDER_TABLE_ID_1L, ORDER_TABLE_ID_2L),
                Lists.list(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(ANY_TABLE_GROUP_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid OrderStatus");
    }
}