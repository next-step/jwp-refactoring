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

@DisplayName("주문항목들 도메인 테스트")
public class OrderLineItemsTest {

	private OrderLineItem 생성된_주문_항목;

	@BeforeEach
	void setup() {
		Product 후라이드 = Product.of(1L, "후라이드", BigDecimal.valueOf(17_000));
		MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");
		MenuProduct 메뉴_상품 = MenuProduct.of(1L, null, 후라이드.getId(), 2L);

		Menu 더블후라이드 = Menu.of(1L, "더블 후라이드", BigDecimal.valueOf(30_000), 추천메뉴);
		더블후라이드.addMenuProducts(Collections.singletonList(메뉴_상품));

		생성된_주문_항목 = OrderLineItem.of(1L, 더블후라이드.getId(), 1L);
	}

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(OrderLineItems.of(Collections.singletonList(생성된_주문_항목)))
			.isEqualTo(OrderLineItems.of(Collections.singletonList(생성된_주문_항목)));
	}

	@DisplayName("추가할 수 있다")
	@Test
	void addTest() {
		OrderLineItems 주문항목들 = OrderLineItems.empty();
		주문항목들.add(생성된_주문_항목);

		// when
		assertThat(주문항목들.toList()).containsAll(Collections.singletonList(생성된_주문_항목));
	}
}
