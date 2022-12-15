package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.exception.CannotGroupOrderTablesException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;
    private OrderTable 주문_테이블3;
    private OrderTable 주문_테이블4;
    private TableGroupRequest 단체지정요청;

    @BeforeEach
    void setUp() {
        주문_테이블1 = OrderTable.of(1L, 2, true);
        주문_테이블2 = OrderTable.of(2L, 2, true);
        주문_테이블3 = OrderTable.of(1L, 2, false);
        주문_테이블4 = OrderTable.of(2L, 2, false);

        단체지정요청 = TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()));
    }

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        when(orderTableRepository.findById(주문_테이블1.getId())).thenReturn(Optional.of(주문_테이블1));
        when(orderTableRepository.findById(주문_테이블2.getId())).thenReturn(Optional.of(주문_테이블2));
        when(tableGroupRepository.save(any())).thenReturn(TableGroup.createEmpty());

        TableGroupResponse result = tableGroupService.create(단체지정요청);

        assertThat(result.getOrderTables()).containsExactly(
                OrderTableResponse.from(주문_테이블3), OrderTableResponse.from(주문_테이블4)
        );
    }

    @DisplayName("단체 지정할 주문 테이블이 2개 이상이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException() {
        TableGroupRequest 단체지정요청 = TableGroupRequest.from(Arrays.asList(주문_테이블1.getId()));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.ORDER_TABLE_NOT_FOUND);
    }

    @DisplayName("단체 지정할 주문 테이블이 등록된 주문 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException2() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.ORDER_TABLE_NOT_FOUND);
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
                .isInstanceOf(CannotGroupOrderTablesException.class)
                .hasMessageStartingWith(ExceptionMessage.NOT_EMPTY_ORDER_TABLE_EXIST);
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
                .isInstanceOf(CannotGroupOrderTablesException.class)
                .hasMessageStartingWith(ExceptionMessage.ALREADY_GROUPED_ORDER_TABLE_EXIST);
    }

    @DisplayName("단체 지정을 취소할 수 있다.")
    @Test
    void ungroup() {
        OrderTable 주문_테이블1 = OrderTable.of(1L, 3, true);
        OrderTable 주문_테이블2 = OrderTable.of(2L, 3, true);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블1, 주문_테이블2);
        TableGroup.of(1L);
        doNothing().when(orderValidator).checkUnGroupable(anyList());
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(주문_테이블_목록);

        tableGroupService.ungroup(1L);

        assertAll(
                () -> assertThat(주문_테이블1.isGrouping()).isFalse(),
                () -> assertThat(주문_테이블2.isGrouping()).isFalse()
        );
    }
}
