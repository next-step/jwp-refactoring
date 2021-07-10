package kitchenpos.domain.order;

import kitchenpos.common.exception.OrderLineItemNotEmptyException;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderLineItemsTest {

    @Test
    void size() {
        // given
        List<OrderLineItem> orderLineItemList = Arrays.asList(new OrderLineItem(), new OrderLineItem(), new OrderLineItem());
        OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);

        // when
        int size = orderLineItems.size();

        // then
        assertThat(size).isEqualTo(orderLineItemList.size());
    }

    @Test
    @DisplayName("비어있지 않는 상태에서 업데이트시 OrderLineItemNotEmptyException이 발생한다")
    void 비어있지_않는_상태에서_업데이트시_OrderLineItemNotEmptyException이_발생한다() {
        List<OrderLineItem> arrays = Arrays.asList(new OrderLineItem(1L, 1L, 1L, new Name("Hello"), new Price(0), new Quantity(1)));
        OrderLineItems orderLineItems = new OrderLineItems(arrays);

        assertThatExceptionOfType(OrderLineItemNotEmptyException.class)
                .isThrownBy(() -> orderLineItems.update(arrays));
    }
}