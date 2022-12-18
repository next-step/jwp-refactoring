package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.*;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.exception.BadRequestException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.codehaus.groovy.control.messages.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.utils.Message.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;

    private OrderTable orderTable4;
    private TableGroupRequest groupRequest;

    @BeforeEach
    void setUp() {
        orderTable1 = OrderTable.of(1L, 2, true);
        orderTable2 = OrderTable.of(2L, 2, true);
        orderTable3 = OrderTable.of(1L, 2, false);
        orderTable4 = OrderTable.of(1L, 2, false);

        groupRequest = TableGroupRequest.from(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
    }

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        when(orderTableRepository.findById(orderTable1.getId())).thenReturn(Optional.of(orderTable1));
        when(orderTableRepository.findById(orderTable2.getId())).thenReturn(Optional.of(orderTable2));
        when(tableGroupRepository.save(any())).thenReturn(TableGroup.createEmpty());

        TableGroupResponse result = tableGroupService.create(groupRequest);

        assertThat(result.getOrderTables()).containsExactly(
                OrderTableResponse.from(orderTable3), OrderTableResponse.from(orderTable4)
        );
    }

    @DisplayName("단체 지정할 주문 테이블이 2개 이상이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException() {
        TableGroupRequest groupRequest = TableGroupRequest.from(Arrays.asList(orderTable1.getId()));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(groupRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(NOT_EXISTS_ORDER_TABLE);;
    }

    @DisplayName("단체 지정할 주문 테이블이 등록된 주문 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException2() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> tableGroupService.create(groupRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(NOT_EXISTS_ORDER_TABLE);

    }

    @DisplayName("등록된 주문 테이블 하나라도 빈 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException3() {
        OrderTable notEmptyOrderTable = OrderTable.of(3L, 3, false);
        when(orderTableRepository.findById(notEmptyOrderTable.getId())).thenReturn(Optional.of(notEmptyOrderTable));
        when(orderTableRepository.findById(orderTable1.getId())).thenReturn(Optional.of(orderTable1));

        List<OrderTable> orderTable_목록 = Arrays.asList(orderTable1, notEmptyOrderTable);
        TableGroupRequest groupRequest =
                TableGroupRequest.from(orderTable_목록.stream()
                        .map(OrderTable::getId)
                        .collect(Collectors.toList()));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(groupRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_EMPTY_ORDER_TABLE);
    }

    @DisplayName("이미 단체 지정이 된 주문 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createException4() {
        OrderTable groupOrderTable = OrderTable.of(3L, 2L, 3, true);
        when(orderTableRepository.findById(groupOrderTable.getId())).thenReturn(Optional.of(groupOrderTable));
        when(orderTableRepository.findById(orderTable1.getId())).thenReturn(Optional.of(orderTable1));

        List<OrderTable> orderTables = Arrays.asList(orderTable1, groupOrderTable);
        TableGroupRequest groupRequest =
                TableGroupRequest.from(orderTables.stream()
                        .map(OrderTable::getId)
                        .collect(Collectors.toList()));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(groupRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(CONTAIN_ALREADY_GROUPED_ORDER_TABLE);

    }

    @DisplayName("단체 지정을 취소할 수 있다.")
    @Test
    void ungroup() {
        OrderTable orderTable1 = OrderTable.of(1L, 3, true);
        OrderTable orderTable2 = OrderTable.of(2L, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup.of(1L);

        doNothing().when(orderValidator).checkCanBeUngrouped(anyList());
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(orderTables);

        tableGroupService.ungroup(1L);

        assertAll(
                () -> assertThat(orderTable1.isGrouping()).isTrue(),
                () -> assertThat(orderTable2.isGrouping()).isTrue()
        );
    }
}
