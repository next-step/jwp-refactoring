package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupName;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;

@DisplayName("주문 테이블")
class OrderTableTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		NumberOfGuests numberOfGuests = NumberOfGuests.of(4);
		boolean empty = false;

		// when
		OrderTable orderTable = OrderTable.of(numberOfGuests, empty);

		// then
		assertAll(
			() -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests),
			() -> assertThat(orderTable.isEmpty()).isEqualTo(empty));
	}

	@DisplayName("빈 상태 변경")
	@Test
	void changeEmpty() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), false);

		// when
		orderTable.changeEmpty(true);

		// then
		assertThat(orderTable.isEmpty()).isEqualTo(true);
	}

	@DisplayName("빈 상태 변경 실패 - 주문 테이블 그룹에 속해 있는 경우")
	@Test
	void changeEmptyFailOnBelongToOrderTableGroup() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTableGroup.of(Arrays.asList(orderTable1, orderTable2));

		// when
		ThrowingCallable throwingCallable = () -> orderTable1.changeEmpty(true);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("빈 상태 변경 실패 - 완료되지 않은 주문이 남아 있는 경우")
	@Test
	void changeEmptyFailOnOrderNotCompleted() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), false);
		OrderLineItems orderLineItems = OrderLineItems.of(Collections.singletonList(OrderLineItem.of(
			Menu.of(
				MenuName.of("후라이드+후라이드"),
				Price.of(BigDecimal.valueOf(25000)),
				MenuGroup.of(MenuGroupName.of("추천메뉴")),
				MenuProducts.of(Collections.singletonList(
					MenuProduct.of(
						Product.of(ProductName.of("후라이드치킨"), Price.of(BigDecimal.valueOf(17000))),
						Quantity.of(2L))))),
			Quantity.of(1L))));

		Order.of(orderTable, orderLineItems);

		// when
		ThrowingCallable throwingCallable = () -> orderTable.changeEmpty(true);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("손님 수 변경")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), false);
		NumberOfGuests numberOfGuests = NumberOfGuests.of(6);

		// when
		orderTable.changeNumberOfGuests(numberOfGuests);

		// then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@DisplayName("손님 수 변경 실패 - 비어 있는 경우")
	@Test
	void changeNumberOfGuestsFailOnEmpty() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), true);
		NumberOfGuests numberOfGuests = NumberOfGuests.of(6);

		// when
		ThrowingCallable throwingCallable = () -> orderTable.changeNumberOfGuests(numberOfGuests);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}
}
