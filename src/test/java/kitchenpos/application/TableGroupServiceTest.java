package kitchenpos.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.order.port.OrderPort;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.tablegroup.port.TableGroupPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderPort orderPort;

    @Mock
    private OrderTablePort orderTablePort;

    @Mock
    private TableGroupPort tableGroupPort;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블_일번;
    private OrderTable 주문테이블_이번;
    private TableGroup 단체지정;

    private List<OrderTable> 주문테이블_리스트;

    @BeforeEach
    void setUp() {
        주문테이블_일번 = new OrderTable(1L, null, 3, true);
        주문테이블_이번 = new OrderTable(2L, null, 7, true);

        주문테이블_리스트 = Arrays.asList(주문테이블_일번, 주문테이블_이번);
        단체지정 = new TableGroup(new OrderTables(주문테이블_리스트));
    }

    @Test
    @DisplayName("단체지정을 등록 할 수 있다.")
    void createTableGroup() {
        given(orderTablePort.findAllByIdIn(
                Arrays.asList(주문테이블_일번.getId(), 주문테이블_이번.getId()))
        ).willReturn(주문테이블_리스트);

        given(tableGroupPort.save(any())).willReturn(단체지정);

        TableGroupResponse result =
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)));

        assertThat(result.getOrderTables().get(0).getNumberOfGuests()).isEqualTo(주문테이블_일번.getNumberOfGuests());
        assertThat(result.getOrderTables().get(1).getNumberOfGuests()).isEqualTo(주문테이블_이번.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블이 비어있어야만 단체지정이 가능한다.")
    void emptyOrderTableAddTableGroup() {
        assertThatThrownBy(() ->
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L, 3L)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 2개 이여야만 단채 지정이 가능하다.")
    void tableGroupsSizeMinTwo() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Arrays.asList(1L)))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 테이블은 주문 테이블이여야한다.")
    void tableGroupIsOrderTable() {
        given(orderTablePort.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Collections.emptyList());

        assertThatThrownBy(() ->
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)))
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문 테이블이 이미 단체지정 되어있으면(이용중) 등록 할 수 없다.")
    void tableGroupIsAlreadyUseFail() {
        주문테이블_일번 = new OrderTable(1L, 단체지정, 7, false);
        주문테이블_이번 = new OrderTable(2L, 단체지정, 7, false);

        given(orderTablePort.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(주문테이블_일번, 주문테이블_이번));

        assertThatThrownBy(() ->
                tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체지정 등록 취소 할 수 있다.")
    void cancelTableGroup() {
        given(tableGroupPort.findById(1L)).willReturn(단체지정);
        given(orderPort.findAllByOrderTableIdIn(any())).willReturn(Arrays.asList(new Order(1L, 주문테이블_일번.getId(), OrderStatus.COMPLETION, null)));

        tableGroupService.ungroup(1L);

        assertThat(주문테이블_일번.getTableGroup()).isNull();
        assertThat(주문테이블_이번.getTableGroup()).isNull();
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("주문 테이블이 이미 조리중이거나 식사중이면 취소가 불가능하다")
    void cancelTableGroupIfCookingAndMealFail(OrderStatus status) {
        List<Order> orderList = Arrays.asList(new Order(1L, 주문테이블_일번.getId(), OrderStatus.COOKING, null), new Order(2L, 주문테이블_이번.getId(), status, null));

        given(tableGroupPort.findById(any())).willReturn(단체지정);
        given(orderPort.findAllByOrderTableIdIn(Arrays.asList(1L, 2L)))
                .willReturn(orderList);

        assertThatThrownBy(() ->
                tableGroupService.ungroup(1L)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
