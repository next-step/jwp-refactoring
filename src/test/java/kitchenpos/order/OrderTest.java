package kitchenpos.order;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.Product;

@DisplayName("주문 도메인 테스트")
public class OrderTest {

	Menu menu;

	@BeforeEach
	void setUp() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		Product product = new Product(1L, "상품이름", new Price(new BigDecimal(1000)));
		MenuProduct menuProduct = new MenuProduct(1L, product, new Quantity(1));
		menu = new Menu(1L, "메뉴", new Price(new BigDecimal(1000)), menuGroup);
	}

	@DisplayName("주문 생성 테스트")
	@Test
	void 주문_생성() {
		OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(1), false);
		OrderLineItem orderLineItem = new OrderLineItem(1L, menu, new Quantity(1));
		OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));
		Order actual = new Order(1L, orderTable);

		assertThat(actual).isNotNull();
		assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
		assertThat(actual.getOrderTable()).isNotNull();
	}

	@DisplayName("주문 생성 시 주문 항목이 0개 이하일 경우 에러 발생")
	@Test
	void 주문_생성_시_주문_항목이_0개_이하일_경우_에러_발생() {
		OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(1));
		assertThatThrownBy(() -> {
			OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList());
			new Order(1L, orderTable);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 생성 시 빈 테이블이 있으면 에러 발생")
	@Test
	void 주문_생성_시_빈_테이블이_있으면_에러_발생() {
		OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(1));
		OrderLineItem orderLineItem = new OrderLineItem(1L, menu, new Quantity(1));
		OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));
		assertThatThrownBy(() ->
			new Order(1L, orderTable)
		).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 상태를 변경")
	@Test
	public void 주문_상태_변경() {
		OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(1), false);
		OrderLineItem orderLineItem = new OrderLineItem(1L, menu, new Quantity(1));
		OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));
		Order order = new Order(1L, orderTable);

		order.changeOrderStatus(OrderStatus.MEAL);

		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);

	}
}
