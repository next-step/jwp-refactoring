package kitchenpos.order.domain;

import kitchenposNew.order.domain.OrderTable;
import kitchenposNew.order.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 생성 테스트")
public class OrderTest {
    @Test
    void 주문_생성_테스트() {
        Order order = new Order(new OrderTable(), new ArrayList<>());

        assertThat(order).isEqualTo(new Order(new OrderTable(), new ArrayList<>()));
    }
}
