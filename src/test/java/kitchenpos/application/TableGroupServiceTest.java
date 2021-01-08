package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exceptions.orderTable.OrderTableEntityNotFoundException;
import kitchenpos.domain.exceptions.tableGroup.InvalidTableGroupTryException;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;
import kitchenpos.ui.dto.orderTable.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderService orderService;

    private OrderTableRequest emptyOrderTableRequest1;
    private OrderTableRequest emptyOrderTableRequest2;
    private OrderTableRequest fullOrderTableRequest1;
    private OrderTableRequest fullOrderTableRequest2;

    @BeforeEach
    void setup() {
        emptyOrderTableRequest1 = new OrderTableRequest(0, true);
        emptyOrderTableRequest2 = new OrderTableRequest(0, true);
        fullOrderTableRequest1 = new OrderTableRequest(500, false);
        fullOrderTableRequest2 = new OrderTableRequest(500, false);
    }

    @DisplayName("2개 이하의 주문테이블로 단체 지정할 수 없다.")
    @ParameterizedTest
    @MethodSource("tableGroupFailWithEmptyTableResource")
    void createTableGroupFailWithEmptyTable(List<OrderTable> orderTables) {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("2개 미만의 주문 테이블로 단체 지정할 수 없다.");
    }
    public static Stream<Arguments> tableGroupFailWithEmptyTableResource() {
        return Stream.of(
                Arguments.of(new ArrayList<>()),
                Arguments.of(Collections.singletonList(new OrderTable()))
        );
    }

    @DisplayName("존재하지 않는 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithNotExistTableGroups() {
        // given
        OrderTable notExistTable1 = new OrderTable();
        notExistTable1.setId(1000001L);
        OrderTable notExistTable2 = new OrderTable();
        notExistTable1.setId(1000002L);

        TableGroup tableGroupWithNotExistOrderTables = new TableGroup();
        tableGroupWithNotExistOrderTables.setOrderTables(Arrays.asList(notExistTable1, notExistTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithNotExistOrderTables))
                .isInstanceOf(OrderTableEntityNotFoundException.class)
                .hasMessage("존재하지 않는 주문 테이블로 단체 지정할 수 없습니다.");
    }

    @DisplayName("비어있지 않은 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithFullOrderTables() {
        // given
        OrderTable orderTable1 = orderTableService.create(fullOrderTableRequest1);
        assertThat(orderTable1.isEmpty()).isFalse();

        OrderTable orderTable2 = orderTableService.create(fullOrderTableRequest1);
        assertThat(orderTable2.isEmpty()).isFalse();

        TableGroup tableGroupWithFullOrderTables = new TableGroup();
        tableGroupWithFullOrderTables.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithFullOrderTables))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("비어있지 않은 주문 테이블로 단체 지정할 수 없습니다.");
    }

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTable orderTable1 = orderTableService.create(emptyOrderTableRequest1);
        assertThat(orderTable1.isEmpty()).isTrue();

        OrderTable orderTable2 = orderTableService.create(emptyOrderTableRequest2);
        assertThat(orderTable2.isEmpty()).isTrue();

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroupRequest = new TableGroup();
        tableGroupRequest.setOrderTables(orderTables);

        // when
        TableGroup tableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(tableGroup.getCreatedDate()).isNotNull();
        tableGroup.getOrderTables().forEach(it -> {
            assertThat(it.getTableGroupId()).isEqualTo(tableGroup.getId());
            assertThat(it.isEmpty()).isFalse();
        });
    }

    @DisplayName("이미 단체 지정된 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithAlreadyGrouped() {
        // given
        OrderTable orderTable1 = orderTableService.create(emptyOrderTableRequest1);
        assertThat(orderTable1.isEmpty()).isTrue();

        OrderTable orderTable2 = orderTableService.create(emptyOrderTableRequest2);
        assertThat(orderTable2.isEmpty()).isTrue();

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroupRequest = new TableGroup();
        tableGroupRequest.setOrderTables(orderTables);

        tableGroupService.create(tableGroupRequest);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("이미 단체 지정된 주문 테이블을 또 단체 지정할 수 없습니다.");
    }

    // TODO: 현재 상태로는 조리와 식사를 한 유닛 테스트에서 모두 확인하기 상당히 어려운 상태
    @DisplayName("주문 상태가 조리나 식사인 단체 지정을 해제할 수 없다.")
    @Test
    void unGroupFailWithInvalidOrderStatus() {
        // given
        OrderTable orderTable1 = orderTableService.create(emptyOrderTableRequest1);
        OrderTable orderTable2 = orderTableService.create(emptyOrderTableRequest2);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroupRequest = new TableGroup();
        tableGroupRequest.setOrderTables(orderTables);

        TableGroup tableGroup = tableGroupService.create(tableGroupRequest);

        OrderRequest orderRequest1 = new OrderRequest(
                orderTable1.getId(), Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        orderService.create(orderRequest1);
        OrderRequest orderRequest2 = new OrderRequest(
                orderTable2.getId(), Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        orderService.create(orderRequest2);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void unGroupTest() {
        // given
        OrderTable orderTable1 = orderTableService.create(emptyOrderTableRequest1);
        OrderTable orderTable2 = orderTableService.create(emptyOrderTableRequest2);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        TableGroup tableGroupRequest = new TableGroup();
        tableGroupRequest.setOrderTables(orderTables);

        TableGroup tableGroup = tableGroupService.create(tableGroupRequest);

        OrderRequest orderRequest1 = new OrderRequest(
                orderTable1.getId(), Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        OrderResponse orderResponse1 = orderService.create(orderRequest1);
        OrderRequest orderRequest2 = new OrderRequest(
                orderTable2.getId(), Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        OrderResponse orderResponse2 = orderService.create(orderRequest2);

        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(orderResponse1.getId(), orderStatusChangeRequest);
        orderService.changeOrderStatus(orderResponse2.getId(), orderStatusChangeRequest);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        List<OrderTable> allOrderTables = orderTableService.list();
        List<OrderTable> found = allOrderTables.stream()
                .filter(it -> it.getId().equals(orderTable1.getId()) || it.getId().equals(orderTable2.getId()))
                .collect(Collectors.toList());
        found.forEach(it -> assertThat(it.getTableGroupId()).isNull());
    }
}
