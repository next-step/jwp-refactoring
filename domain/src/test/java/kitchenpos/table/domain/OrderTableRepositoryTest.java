package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    private List<OrderTable> orderTableList;
    private int numberOfGuests;
    private TableStatus tableStatus;

    @BeforeEach
    public void setup() {
        orderTableList = new ArrayList<>();
        numberOfGuests = 1;
        tableStatus = TableStatus.ORDER;
    }

    @Test
    @DisplayName("생성한 주문 테이블을 저장 한다")
    public void createOrderTable() {
        //given
        OrderTable orderTable = new OrderTable(numberOfGuests, tableStatus);

        //when
        OrderTable saveOrderTable = orderTableRepository.save(orderTable);

        //then
        assertThat(saveOrderTable).isEqualTo(orderTable);

    }

    @Test
    @DisplayName("주문 테이블 리스트를 가져온다")
    public void selectOrderTableList() {
        //given
        OrderTable orderTable = new OrderTable(numberOfGuests, tableStatus);
        orderTableRepository.save(orderTable);

        //when
        List<OrderTable> orderTables = orderTableRepository.findAll();

        //then
        assertThat(orderTables).isNotEmpty();
        for (OrderTable table : orderTables) {
            assertThat(table.id()).isNotNull();
        }
    }

    @Test
    @DisplayName("주문 테이블 상태를 빈 테이블로 변경 한다")
    public void modifyOrderTableEmpty() {
        //given
        OrderTable orderTable = new OrderTable(numberOfGuests, tableStatus);
        orderTable.changeTableStatus(TableStatus.EMPTY);

        //when
        OrderTable saveOrderTable = orderTableRepository.save(orderTable);

        //then
        assertThat(saveOrderTable.tableStatus()).isEqualTo(TableStatus.EMPTY);
    }

    @Test
    @DisplayName("주문 테이블 손님 수를 변경 한다")
    public void modifyOrderTableGuests() {
        //given
        OrderTable orderTable = new OrderTable(numberOfGuests, tableStatus);
        orderTable.changeNumberOfGuests(3);

        //when
        OrderTable saveOrderTable = orderTableRepository.save(orderTable);

        //then
        assertThat(saveOrderTable.numberOfGuests()).isEqualTo(3);

    }
}
