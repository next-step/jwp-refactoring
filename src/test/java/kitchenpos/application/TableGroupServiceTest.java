package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableGroupTryException;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableUngroupTryException;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;
import kitchenpos.ui.dto.orderTable.OrderTableRequest;
import kitchenpos.ui.dto.orderTable.OrderTableResponse;
import kitchenpos.ui.dto.tableGroup.OrderTableInTableGroupRequest;
import kitchenpos.ui.dto.tableGroup.TableGroupRequest;
import kitchenpos.ui.dto.tableGroup.TableGroupResponse;
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
    void createTableGroupFailWithEmptyTable(List<OrderTableInTableGroupRequest> orderTables) {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.group(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("2개 미만의 주문 테이블로 단체 지정할 수 없다.");
    }
    public static Stream<Arguments> tableGroupFailWithEmptyTableResource() {
        return Stream.of(
                Arguments.of(new ArrayList<>()),
                Arguments.of(Collections.singletonList(new OrderTableInTableGroupRequest(1L)))
        );
    }

    @DisplayName("존재하지 않는 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithNotExistTableGroups() {
        // given
        OrderTableInTableGroupRequest notExist1 = new OrderTableInTableGroupRequest(10000L);
        OrderTableInTableGroupRequest notExist2 = new OrderTableInTableGroupRequest(10001L);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(notExist1, notExist2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.group(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("존재하지 않는 주문 테이블을 단체 지정할 수 없습니다.");
    }

    @DisplayName("비어있지 않은 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithFullOrderTables() {
        // given
        OrderTableResponse orderTable1Response = orderTableService.create(fullOrderTableRequest1);
        assertThat(orderTable1Response.isEmpty()).isFalse();
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(fullOrderTableRequest2);
        assertThat(orderTable2Response.isEmpty()).isFalse();
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.group(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("빈 주문 테이블들로만 단체 지정할 수 있습니다.");
    }

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTableResponse orderTable1Response = orderTableService.create(emptyOrderTableRequest1);
        assertThat(orderTable1Response.isEmpty()).isTrue();
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(emptyOrderTableRequest2);
        assertThat(orderTable2Response.isEmpty()).isTrue();
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.group(tableGroupRequest);

        // then
        assertThat(tableGroupResponse.getCreatedDate()).isNotNull();
        tableGroupResponse.getOrderTables().forEach(it -> {
            assertThat(it.getId()).isNotNull();
        });
    }

    @DisplayName("이미 단체 지정된 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithAlreadyGrouped() {
        // given
        OrderTableResponse orderTable1Response = orderTableService.create(emptyOrderTableRequest1);
        assertThat(orderTable1Response.isEmpty()).isTrue();
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(emptyOrderTableRequest2);
        assertThat(orderTable2Response.isEmpty()).isTrue();
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        tableGroupService.group(tableGroupRequest);

        // when, then
        assertThatThrownBy(() -> tableGroupService.group(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("이미 단체 지정된 주문 테이블을 단체 지정할 수 없습니다.");
    }

    @DisplayName("주문 상태가 조리나 식사인 단체 지정을 해제할 수 없다.")
    @Test
    void unGroupFailWithInvalidOrderStatus() {
        // given
        OrderTableResponse orderTable1Response = orderTableService.create(emptyOrderTableRequest1);
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(emptyOrderTableRequest2);
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        TableGroupResponse tableGroupResponse = tableGroupService.group(tableGroupRequest);

        OrderRequest orderRequest1 = new OrderRequest(
                orderTable1.getId(), Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        orderService.create(orderRequest1);
        OrderRequest orderRequest2 = new OrderRequest(
                orderTable2.getId(), Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        orderService.create(orderRequest2);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(InvalidTableUngroupTryException.class)
                .hasMessage("조리 중이거나 식사 중인 단체 지정을 해제할 수 없습니다.");
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void unGroupTest() {
        // given
        OrderTableResponse orderTable1Response = orderTableService.create(emptyOrderTableRequest1);
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(emptyOrderTableRequest2);
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        TableGroupResponse tableGroupResponse = tableGroupService.group(tableGroupRequest);

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
        tableGroupService.ungroup(tableGroupResponse.getId());

        // then
        // TODO: 단체 지정 해제 관련 로직 자체가 수정되야 함.
    }
}
