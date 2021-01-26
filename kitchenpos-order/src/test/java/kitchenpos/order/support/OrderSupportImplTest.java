package kitchenpos.order.support;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.support.OrderSupport;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("테이블 주문 체크 서포트")
@SpringBootTest
public class OrderSupportImplTest {
    @MockBean
    private OrderRepository orderRepository;
    private final OrderSupport orderSupport;

    @Autowired
    public OrderSupportImplTest(OrderSupport orderSupport) {
        this.orderSupport = orderSupport;
    }

    @DisplayName("테이블 주문 체크 서포트 테스트")
    @Test
    void support() {
        when(orderRepository.existsByOrderTableAndOrderStatusIn(any(),any())).thenReturn(true);
        assertThat(orderSupport.isUsingTable(new OrderTable())).isTrue();

        when(orderRepository.existsByOrderTableInAndOrderStatusIn(any(),any())).thenReturn(true);
        assertThat(orderSupport.isUsingTable(new OrderTable())).isTrue();

        when(orderRepository.existsByOrderTableAndOrderStatusIn(any(),any())).thenReturn(false);
        assertThat(orderSupport.isUsingTable(new OrderTable())).isFalse();

        when(orderRepository.existsByOrderTableInAndOrderStatusIn(any(),any())).thenReturn(false);
        assertThat(orderSupport.isUsingTable(new OrderTable())).isFalse();
    }
}
