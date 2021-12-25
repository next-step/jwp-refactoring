package kitchenpos.table;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 테이블 생성")
    @Test
    void createTable() {

        //given
        final int numberOfGuests = 10;
        final boolean empty = true;
        OrderTable orderTable = OrderTable.create(numberOfGuests, empty);

        //when
        OrderTable actualOrderTable = orderTableRepository.save(orderTable);

        //then
        assertThat(actualOrderTable.getId()).isGreaterThan(0L);
    }

    @DisplayName("주문 테이블 리스트 조회")
    @Test
    void getTables() {

//        //given
//
//        //when
//        List<OrderTable> orderTables = orderTableRepository.findAll();
//
//        //then
//        assertThat(orderTables).isNotEmpty();
    }

}
