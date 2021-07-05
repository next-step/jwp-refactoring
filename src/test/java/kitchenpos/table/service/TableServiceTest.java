package kitchenpos.table.service;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
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
        OrderTable orderTable = new OrderTable(0, false);

        // when
        OrderTable createOrderTable = tableService.create(orderTable);

        // then
        assertThat(createOrderTable.getId()).isNotNull();
        assertThat(createOrderTable.getNumberOfGuests()).isZero();

    }

    @Test
    @DisplayName("주문 테이블 리스트를 가져온다")
    public void selectOrderTableList() {
        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).isNotEmpty();
        for (OrderTable orderTable : orderTables) {
            assertThat(orderTable.getId()).isNotNull();
        }

    }

    @Test
    @DisplayName("주문 테이블 상태를 빈 테이블로 변경 한다")
    public void modifyOrderTableEmpty() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // when
        OrderTable changeOrderTable = tableService.changeEmpty(4L, orderTable);

        // then
        assertThat(changeOrderTable.getId()).isNotNull();
        assertThat(changeOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블 손님 수를 변경 한다")
    public void modifyOrderTableGuests() {
        // given
        OrderTable orderTable = new OrderTable(3, false);

        // when
        OrderTable changeOrderTable = tableService.changeNumberOfGuests(2L, orderTable);

        // then
        assertThat(changeOrderTable.getId()).isNotNull();
        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

}
