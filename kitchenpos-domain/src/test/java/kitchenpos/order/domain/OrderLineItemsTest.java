package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.OrderLineItemFixtures.주문정보;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : kitchenpos.order.domain
 * fileName : OrderLineItemsTest
 * author : haedoang
 * date : 2021-12-22
 * description :
 */
@DisplayName("OrderLineItems 일급컬렉션 테스트")
class OrderLineItemsTest {

    @Test
    @DisplayName("일급컬렉션을 생성한다.")
    public void create() throws Exception {
        // given
        List<OrderLineItem> given = Arrays.asList(주문정보(), 주문정보(), 주문정보());
        OrderLineItems orderLineItems = new OrderLineItems();

        // when
        orderLineItems.add(given);

        // then
        assertThat(orderLineItems.value()).hasSize(3);
    }

}
