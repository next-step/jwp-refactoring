package kitchenpos.tablegroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderTableRepository 테스트")
@DataJpaTest
class OrderTableRepositoryTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문_테이블_Id_목록을_입력받아_주문_테이블_목록_조회() {
        OrderTable 주문테이블1 = orderTableRepository.save(new OrderTable(5, false));
        OrderTable 주문테이블2 = orderTableRepository.save(new OrderTable(2, false));
        OrderTable 주문테이블3 = orderTableRepository.save(new OrderTable(3, false));

        List<OrderTable> orderTables = orderTableRepository.findAllById(
                Arrays.asList(주문테이블1.getId(), 주문테이블2.getId(), 주문테이블3.getId()));

        assertThat(orderTables).hasSize(3);
    }
}