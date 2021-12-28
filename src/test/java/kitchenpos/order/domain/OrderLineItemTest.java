package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;

@DisplayName("주문 항목 도메인 테스트")
public class OrderLineItemTest {

	private Order 생성된_주문;
	private OrderLineItem 생성된_주문_항목;
	private Menu 더블후라이드;

	@BeforeEach
	void setup() {
		Product 후라이드 = Product.of(1L, "후라이드", BigDecimal.valueOf(17_000));
		MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");
		MenuProduct 메뉴_상품 = MenuProduct.of(1L, null, 후라이드, 2L);

		더블후라이드 = Menu.of(1L, "더블 후라이드", BigDecimal.valueOf(30_000), 추천메뉴);
		더블후라이드.addMenuProducts(Collections.singletonList(메뉴_상품));

		OrderTable 테이블 = OrderTable.of(1L, 2, false);

		생성된_주문_항목 = OrderLineItem.of(1L, 더블후라이드, 1L);
		생성된_주문 = Order.of(1L, 테이블, OrderStatus.COOKING);
	}

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(OrderLineItem.of(1L, 더블후라이드, 1L))
			.isEqualTo(OrderLineItem.of(1L, 더블후라이드, 1L));
	}

	@DisplayName("주문을 설정할 수 있다")
	@Test
	void setOrderTest() {
		// when
		생성된_주문_항목.setOrder(생성된_주문);

		// then
		assertThat(생성된_주문_항목.getOrder()).isEqualTo(생성된_주문);
	}

}
