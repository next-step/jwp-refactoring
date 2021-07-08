package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

class OrderLineItemTest {

	@DisplayName("주문항목은 주문메뉴정보와 주문수량정보 생성된다.")
	@Test
	void createTest() {
		OrderMenu 치킨 = OrderMenu.of(1L, Name.valueOf("치킨"), Price.wonOf(3000));

		OrderLineItem 주문항목 = new OrderLineItem(치킨, 2L);

		assertThat(주문항목.getMenuId()).isEqualTo(1L);
		assertThat(주문항목.getQuantity()).isEqualTo(2L);
	}

}
