package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;

class OrderLineItemTest {

	@DisplayName("주문항목의 주문메뉴정보와 주문수량정보는 필수 정보이다.")
	@Test
	void createTest() {
		OrderMenu orderMenu = OrderMenu.of(1L, Name.valueOf("치킨"), Price.wonOf(3000));

		OrderLineItem orderLineItem = new OrderLineItem(orderMenu, 2L);

		assertThat(orderLineItem.getMenuId()).isEqualTo(1L);
		assertThat(orderLineItem.getQuantity()).isEqualTo(Quantity.of(2));
	}

}

