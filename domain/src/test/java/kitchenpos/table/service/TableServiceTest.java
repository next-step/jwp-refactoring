package kitchenpos.table.service;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.TableStatus;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 생성 한다")
    public void createOrderTable() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, TableStatus.ORDER);

        // when
        OrderTableResponse createOrderTableResponse = tableService.create(orderTableRequest);

        // then
        assertThat(createOrderTableResponse.getId()).isNotNull();
        assertThat(createOrderTableResponse.getNumberOfGuests()).isZero();

    }

    @Test
    @DisplayName("주문 테이블 리스트를 가져온다")
    public void selectOrderTableList() {
        // when
        List<OrderTableResponse> orderTableResponses = tableService.list();

        // then
        assertThat(orderTableResponses).isNotEmpty();
        for (OrderTableResponse orderTableResponse : orderTableResponses) {
            assertThat(orderTableResponse.getId()).isNotNull();
        }

    }

    @Test
    @DisplayName("주문 테이블 상태를 빈 테이블로 변경 한다")
    public void modifyOrderTableEmpty() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, TableStatus.EMPTY);

        // when
        OrderTableResponse changeOrderTableResponse = tableService.changeEmpty(4L, orderTableRequest);

        // then
        assertThat(changeOrderTableResponse.getId()).isNotNull();
        assertThat(changeOrderTableResponse.getTableStatus()).isEqualTo(TableStatus.EMPTY);
    }

    @Test
    @DisplayName("주문 테이블 손님 수를 변경 한다")
    public void modifyOrderTableGuests() {
        // given
        OrderTableRequest orderTable = new OrderTableRequest(3, TableStatus.ORDER);

        // when
        OrderTableResponse changeOrderTableResponse = tableService.changeNumberOfGuests(2L, orderTable);

        // then
        assertThat(changeOrderTableResponse.getId()).isNotNull();
        assertThat(changeOrderTableResponse.getNumberOfGuests()).isEqualTo(3);
    }

}
