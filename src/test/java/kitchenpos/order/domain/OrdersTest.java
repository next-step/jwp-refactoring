package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : kitchenpos.order.domain
 * fileName : OrdersTest
 * author : haedoang
 * date : 2021-12-22
 * description :
 */
@DisplayName("Orders 일급컬렉션 테스트")
class OrdersTest {

    @Mock
    private Order order;

    @Test
    @DisplayName("일급컬렉션을 생성한다.")
    public void create() throws Exception {
        // given
        List<Order> given = Arrays.asList(order, order, order);
        Orders orders = new Orders();

        // when
        given.stream()
                .forEach(orders::add);

        // then
        assertThat(orders.value()).hasSize(3);
    }
}
