package kitchenpos.order.domain;

import kitchenpos.JpaEntityTest;
import kitchenpos.order.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 도메인 테스트")
public class OrderTableTest extends JpaEntityTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("테이블 생성 테스트")
    @Test
    void createOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(4, true);

        // when
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // then
        assertThat(savedOrderTable).isNotNull();
    }
}
