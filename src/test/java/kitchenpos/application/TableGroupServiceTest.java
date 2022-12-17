package kitchenpos.application;

import kitchenpos.domain.order.*;
import kitchenpos.domain.product.TableGroup;
import kitchenpos.domain.product.TableGroupRepository;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.utils.Message.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;
    private OrderTable 주문_테이블3;
    private TableGroupRequest 단체지정요청;

    @BeforeEach
    void setUp() {
        주문_테이블1 = OrderTable.of(1L, 2, true);
        주문_테이블2 = OrderTable.of(2L, 2, true);
        주문_테이블3 = OrderTable.of(1L, 2, true);

        단체지정요청 = TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()));
    }

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        when(orderTableRepository.findById(주문_테이블1.getId())).thenReturn(Optional.of(주문_테이블3));
        when(orderTableRepository.findById(주문_테이블2.getId())).thenReturn(Optional.of(주문_테이블3));
        when(tableGroupRepository.save(any())).thenReturn(TableGroup.from(Arrays.asList(주문_테이블1, 주문_테이블2)));

        TableGroupResponse result = tableGroupService.create(단체지정요청);

        assertThat(result.getOrderTables()).containsExactly(
                OrderTableResponse.from(주문_테이블1), OrderTableResponse.from(주문_테이블2)
        );
    }

    @DisplayName("단체 지정할 주문 테이블이 2개 이상이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException() {
        TableGroupRequest 단체지정요청 = TableGroupRequest.from(Arrays.asList(주문_테이블1.getId()));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정할 주문 테이블이 등록된 주문 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException2() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블 하나라도 빈 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException3() {
        OrderTable 비어있지_않은_주문_테이블 = OrderTable.of(3L, 3, false);
        when(orderTableRepository.findById(비어있지_않은_주문_테이블.getId())).thenReturn(Optional.of(비어있지_않은_주문_테이블));
        when(orderTableRepository.findById(주문_테이블1.getId())).thenReturn(Optional.of(주문_테이블1));

        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블1, 비어있지_않은_주문_테이블);
        TableGroupRequest 단체지정요청 =
                TableGroupRequest.from(주문_테이블_목록.stream()
                        .map(OrderTable::getId)
                        .collect(Collectors.toList()));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_EMPTY_ORDER_TABLE);
    }

    @DisplayName("이미 단체 지정이 된 주문 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createException4() {
        OrderTable 단체_지정된_주문_테이블 = OrderTable.of(3L, 2L, 3, true);
        when(orderTableRepository.findById(단체_지정된_주문_테이블.getId())).thenReturn(Optional.of(단체_지정된_주문_테이블));
        when(orderTableRepository.findById(주문_테이블1.getId())).thenReturn(Optional.of(주문_테이블1));

        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블1, 단체_지정된_주문_테이블);
        TableGroupRequest 단체지정요청 =
                TableGroupRequest.from(주문_테이블_목록.stream()
                        .map(OrderTable::getId)
                        .collect(Collectors.toList()));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(CONTAIN_ALREADY_GROUPED_ORDER_TABLE);

    }

    @DisplayName("단체 지정을 취소할 수 있다.")
    @Test
    void ungroup() {
        OrderTable 주문_테이블1 = OrderTable.of(1L, 3, true);
        OrderTable 주문_테이블2 = OrderTable.of(2L, 3, true);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블1, 주문_테이블2);
        TableGroup.of(1L, 주문_테이블_목록);
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(주문_테이블_목록);

        tableGroupService.ungroup(1L);

        assertAll(
                () -> assertThat(주문_테이블1.isGrouping()).isTrue(),
                () -> assertThat(주문_테이블2.isGrouping()).isTrue()
        );
    }

    @DisplayName("단체 지정된 주문 테이블들의 상태가 조리 상태면 단체 지정을 취소할 수 없다.")
    @Test
    void ungroupException() {
        OrderTable 주문_테이블1 = OrderTable.of(1L, 3, true);
        OrderTable 주문_테이블2 = OrderTable.of(2L, 3, true);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블1, 주문_테이블2);
        TableGroup.of(1L, 주문_테이블_목록);

        Order.of(주문_테이블1, Arrays.asList(OrderLineItem.of(1L, 2)));
        Order.of(주문_테이블2, Arrays.asList(OrderLineItem.of(2L, 2)));

        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(주문_테이블_목록);

        Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CANCEL_ORDER_TABLES_STATUS);
    }

    @DisplayName("단체 지정된 주문 테이블들이 식사 상태면 단체 지정을 취소할 수 없다.")
    @Test
    void ungroupException3() {
        OrderTable 주문_테이블1 = OrderTable.of(1L, 3, true);
        OrderTable 주문_테이블2 = OrderTable.of(2L, 3, true);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블1, 주문_테이블2);
        TableGroup.of(1L, 주문_테이블_목록);

        Order 주문1 = Order.of(주문_테이블1, Arrays.asList(OrderLineItem.of(1L, 2)));
        Order 주문2 = Order.of(주문_테이블2, Arrays.asList(OrderLineItem.of(2L, 2)));

        주문1.changeOrderStatus(OrderStatus.MEAL);
        주문2.changeOrderStatus(OrderStatus.MEAL);

        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(주문_테이블_목록);

        Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CANCEL_ORDER_TABLES_STATUS);
    }
}
