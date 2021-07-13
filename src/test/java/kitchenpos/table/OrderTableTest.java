package kitchenpos.table;

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
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;

@DisplayName("주문 테이블 도메인 테스트")
public class OrderTableTest {

	Order order;

	@BeforeEach
	void setUp() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		Product product = new Product(1L, "상품이름", new Price(new BigDecimal(1000)));
		MenuProduct menuProduct = new MenuProduct(1L, product, new Quantity(1));
		Menu menu = new Menu(1L, "메뉴", new Price(new BigDecimal(1000)), menuGroup,
			new MenuProducts(Arrays.asList(menuProduct)));
		OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(1), false);
		OrderLineItem orderLineItem = new OrderLineItem(1L, menu, new Quantity(1));
		OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(orderLineItem));
		order = new Order(1L, orderTable, orderLineItems);
	}

	@DisplayName("주문 테이블 생성 테스트")
	@Test
	void 주문_테이블_생성_테스트() {
		NumberOfGuests numberOfGuests = new NumberOfGuests(1);
		OrderTable actual = new OrderTable(1L, numberOfGuests, false);

		주문_테이블_생성_확인(actual, 1L);
	}

	@DisplayName("주문 테이블 비우거나 채우기 테스트")
	@Test
	void 주문_테이블_비우거나_채우기_테스트() {
		NumberOfGuests numberOfGuests = new NumberOfGuests(1);
		OrderTable actual = new OrderTable(1L, numberOfGuests, false);
		order.changeOrderStatus(OrderStatus.COMPLETION);
		actual.changeEmpty(false, order);

		주문_테이블_비우거나_채우기_확인(actual);
	}

	@DisplayName("주문 테이블의 손님 수를 변경하기 테스트")
	@Test
	void 주문_테이블의_손님_수를_변경하기_테스트() {
		NumberOfGuests numberOfGuests = new NumberOfGuests(1);
		OrderTable actual = new OrderTable(1L, numberOfGuests, false);
		actual.changeNumberOfGuests(new NumberOfGuests((2)));

		주문_테이블의_손님_수_변경_확인(actual, 2);
	}

	private void 주문_테이블의_손님_수_변경_확인(OrderTable actual, int changeNumberOfGuests) {
		assertThat(actual.getNumberOfGuests().value()).isEqualTo(changeNumberOfGuests);
	}

	private void 주문_테이블_비우거나_채우기_확인(OrderTable actual) {
		assertThat(actual.isEmpty()).isFalse();
	}

	private void 주문_테이블_생성_확인(OrderTable actual, Long expectedId) {
		assertThat(actual).isNotNull();
		assertThat(actual.getId()).isEqualTo(expectedId);
		assertThat(actual.getTableGroup()).isNull();
	}
}
