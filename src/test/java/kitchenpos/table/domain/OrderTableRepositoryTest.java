package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.JpaRepositoryTest;
import kitchenpos.table.domain.fixture.OrderTableFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderTableRepositoryTest extends JpaRepositoryTest {
    OrderTable orderTable1;
    OrderTable orderTable2;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderTable1 = OrderTableFixtureFactory.createEmptyOrderTable();
        orderTable2 = OrderTableFixtureFactory.createEmptyOrderTable();
        orderTableRepository.saveAll(Lists.newArrayList(orderTable1, orderTable2));
    }


    @Test
    @DisplayName("아이디 여러개로 주문테이블 찾기")
    void finaAllByIdInTest() {
        List<OrderTable> orderTableList = orderTableRepository.findAllByIdIn(
                Lists.newArrayList(orderTable1.getId(), orderTable2.getId()));
        assertThat(orderTableList).hasSize(2);
    }


    @Test
    @DisplayName("유효하지 않은 아이디의 경우 빈 array를 리턴")
    void finaAllByIdInTestWithNotValidId() {
        List<OrderTable> orderTableList = orderTableRepository.findAllByIdIn(Lists.newArrayList(-1L, -2L));
        assertThat(orderTableList).isNotNull().isEmpty();
    }
}
