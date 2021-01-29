package kitchenpos.table;

import kitchenpos.BaseServiceTest;
import kitchenpos.exception.NotFoundEntityException;
import kitchenpos.order.OrderService;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.InvalidChangeException;
import kitchenpos.table.exception.NegativeNumberOfGuestsException;
import kitchenpos.tablegroup.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static kitchenpos.utils.TestHelper.*;

class TableServiceTest extends BaseServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTable() {
        OrderTableResponse orderTableResponse = tableService.create(
                new OrderTableRequest(등록되어_있지_않은_orderTable_id, 1, false));

        assertThat(orderTableResponse.getId()).isEqualTo(등록되어_있지_않은_orderTable_id);
        assertThat(orderTableResponse.getTableGroupId()).isNull();
    }

    @DisplayName("빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        List<OrderLineItemRequest> orderLineItemRequests = Collections.singletonList(new OrderLineItemRequest(등록된_menu_id, 2));
        OrderRequest orderRequest = new OrderRequest(1L, 비어있지_않은_orderTable_id, orderLineItemRequests);
        orderService.create(orderRequest);

        orderRequest = new OrderRequest(1L, 비어있지_않은_orderTable_id, OrderStatus.COMPLETION, orderLineItemRequests);
        orderService.changeOrderStatus(1L, orderRequest);

        OrderTableRequest orderTableRequest = new OrderTableRequest(비어있지_않은_orderTable_id, 0, true);
        OrderTableResponse result = tableService.changeEmpty(orderTableRequest.getId(), orderTableRequest);

        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeEmptyException1() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(등록되어_있지_않은_orderTable_id, 1, false);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableRequest.getId(), orderTableRequest))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("해당 주문 테이블이 등록되어 있지 않습니다.");
    }

    @DisplayName("주문 테이블이 단체 지정일 경우 변경할 수 없다.")
    @Test
    void changeEmptyException2() {
        OrderTable orderTable1 = tableService.findOrderTableById(빈_orderTable_id1);
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(orderTable1.getId(), orderTable1.getNumberOfGuests(), orderTable1.isEmpty());
        OrderTable orderTable2 = tableService.findOrderTableById(빈_orderTable_id2);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(orderTable2.getId(), orderTable2.getNumberOfGuests(), orderTable2.isEmpty());

        tableGroupService.create(new TableGroupRequest(1L, Arrays.asList(orderTableRequest1, orderTableRequest2)));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableRequest1.getId(), orderTableRequest1))
                .isInstanceOf(InvalidChangeException.class)
                .hasMessage("단체 지정 테이블입니다.");
    }

    @DisplayName("주문들 중에서 주문 상태가 조리 또는 식사가 하나라도 있을 경우 변경할 수 없다.")
    @Test
    void changeEmptyException3() {
        List<OrderLineItemRequest> orderLineItemRequests = Collections.singletonList(new OrderLineItemRequest(등록된_menu_id, 2));
        OrderRequest orderRequest = new OrderRequest(1L, 비어있지_않은_orderTable_id, orderLineItemRequests);
        orderService.create(orderRequest);

        OrderTableRequest orderTableRequest = new OrderTableRequest(비어있지_않은_orderTable_id, 0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableRequest.getId(), orderTableRequest))
                .isInstanceOf(InvalidChangeException.class)
                .hasMessage("주문 상태가 조리 또는 식사이므로 변경할 수 없습니다.");
    }

    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(비어있지_않은_orderTable_id, 3, false);

        OrderTableResponse result = tableService.changeNumberOfGuests(orderTableRequest.getId(), orderTableRequest);

        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("방문한 손님 수가 0보다 작으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException1() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(비어있지_않은_orderTable_id, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.getId(), orderTableRequest))
                .isInstanceOf(NegativeNumberOfGuestsException.class)
                .hasMessage("방문한 손님 수는 0명 이상이어야 합니다.");
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(등록되어_있지_않은_orderTable_id, 3, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.getId(), orderTableRequest))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("해당 주문 테이블이 등록되어 있지 않습니다.");
    }

    @DisplayName("빈 테이블일 경우 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException3() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(빈_orderTable_id1, 4, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.getId(), orderTableRequest))
                .isInstanceOf(InvalidChangeException.class)
                .hasMessage("빈 테이블입니다.");
    }
}
