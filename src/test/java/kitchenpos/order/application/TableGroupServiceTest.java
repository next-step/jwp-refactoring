package kitchenpos.order.application;

import kitchenpos.order.applicaiton.TableGroupService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private TableGroup tableGroup;
    private OrderTable notEmptyOrderTable1;
    private OrderTable notEmptyOrderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = OrderTable.of(1L, null, 4, true);
        orderTable2 = OrderTable.of(2L, null, 4, true);

        notEmptyOrderTable1 = OrderTable.of(3L, null, 4, false);
        notEmptyOrderTable2 = OrderTable.of(4L, null, 4, false);

        tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1, orderTable2);
    }

    @Test
    @DisplayName("단체 지정 생성 시 주문 테이블이 비었을 경우 Exception")
    public void throwExceptionOrderTableIsEmpty() {
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(1L, 2L));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성 시 주문 테이블이 2개 미만일 경우 Exception")
    public void throwExceptionNumberOfOrderTableIsLessThanTwo() {
        assertThatThrownBy(() -> TableGroupRequest.of(Arrays.asList(1L))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성 시 주문 테이블 수와 저장된 단체 지정의 주문 테이블 수가 다를 경우 Exception")
    public void throwExceptionWhenOrderTableDataIntegrityIsViolated() {
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(1L, 2L));

        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성 시 주문 테이블이 비어있지 않으면 Exception")
    public void throwExceptionWhenOrderTableIsNotEmpty() {
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(3L, 4L));

        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(notEmptyOrderTable1, notEmptyOrderTable2));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성 시 이미 단체 지정된 테이블은 단체로 지정 불가")
    public void canNotCreateTableGroupWhenOrderTableIsAlreadyGrouped() {
        OrderTable orderTable1 = OrderTable.of(1L, null, 4, false);
        OrderTable orderTable2 = OrderTable.of(2L, null, 4, false);
        TableGroup.of(2L, LocalDateTime.now(), orderTable1, orderTable2);
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(1L, 2L));

        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 생성")
    public void createTableGroup() {
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(1L, 2L));

        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);

        assertThat(tableGroupService.create(request).getId()).isEqualTo(tableGroup.getId());
    }

    @Test
    @DisplayName("단체 지정 해제 시 주문 상태가 COOKING이거나 MEAL이면 Exception")
    public void throwExceptionWhenTryToUnGroupOrderIsCookingOrMeal() {
        TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), notEmptyOrderTable1, notEmptyOrderTable2);

        List<Long> orderTableIds = getOrderTableIds(tableGroup);

        given(orderTableRepository.findAllByTableGroup_Id(tableGroup.getId())).willReturn(tableGroup.getOrderTables());
        given(orderRepository.existsByOrderTable_IdInAndOrderStatusIn(orderTableIds,
                Order.orderStatusInProgress)).willReturn(true);
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 해제")
    public void ungroup() {
        TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTable1, orderTable2);

        List<Long> orderTableIds = getOrderTableIds(tableGroup);

        given(orderTableRepository.findAllByTableGroup_Id(tableGroup.getId())).willReturn(
                Arrays.asList(orderTable1, orderTable2)
        );
        given(orderRepository.existsByOrderTable_IdInAndOrderStatusIn(orderTableIds,
                Order.orderStatusInProgress)).willReturn(false);

        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }

    private static List<Long> getOrderTableIds(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderTableIds;
    }
}