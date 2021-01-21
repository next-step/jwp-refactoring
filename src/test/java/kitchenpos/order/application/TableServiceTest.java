package kitchenpos.order.application;


import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;


    @AfterEach
    void cleanup() {
        orderRepository.deleteAllInBatch();
        orderTableRepository.deleteAllInBatch();
        tableGroupRepository.deleteAllInBatch();

    }

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        OrderTableRequest request = new OrderTableRequest(0, true);
        OrderTableResponse actual = tableService.create(request);


        assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경")
    @Test
    void changeEmpty() {
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(0, true));
        OrderTableRequest request = new OrderTableRequest(0, false);

        OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), request);

        assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 단체 지정인 경우")
    @Test
    void validExistTableGroup() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.changeTableGroupId(tableGroup.getId());
        orderTableRepository.save(orderTable);

        OrderTableRequest request = new OrderTableRequest(0, false);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), request);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("손님의 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(0, false));
        OrderTableRequest request = new OrderTableRequest(10, false);

        OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

        assertThat(actual.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("손님의 수 변경 예외 - 0보다 작을 경우")
    @Test
    void validNumberOfGuests() {
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(0, true));
        OrderTableRequest request = new OrderTableRequest(-1, false);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), request);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}