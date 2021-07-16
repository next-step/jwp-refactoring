package kitchenpos.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.event.OrderCreateEvent;
import kitchenpos.order.event.OrderLineItemEventHandler;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.Quantity;

@DisplayName("주문 항목 이벤트 핸들러 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderLineItemEventHandlerTest {

	@Mock
	private OrderLineItemRepository orderLineItemRepository;

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private OrderLineItemEventHandler orderLineItemEventHandler;

	Menu 메뉴1번;
	Menu 메뉴2번;

	@BeforeEach
	void setUp() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		Product product = new Product(1L, "상품이름", new Price(new BigDecimal(1000)));
		MenuProduct menuProduct = new MenuProduct(1L, product, new Quantity(1));
		메뉴1번 = new Menu(1L, "메뉴", new Price(new BigDecimal(1000)), menuGroup);
		메뉴2번 = new Menu(2L, "메뉴", new Price(new BigDecimal(1000)), menuGroup);
	}

	@DisplayName("주문 생성 시 주문 항목들 생성")
	@Test
	void 주문_생성_시_주문_항목들_생성() {
		OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(1), false);
		Order order = new Order(1L, orderTable);
		OrderLineItemRequest 주문항목_요청1 = new OrderLineItemRequest(1L, 1);
		OrderLineItemRequest 주문항목_요청2 = new OrderLineItemRequest(2L, 1);
		given(menuRepository.findById(메뉴1번.getId())).willReturn(Optional.of(메뉴1번));
		given(menuRepository.findById(메뉴2번.getId())).willReturn(Optional.of(메뉴2번));

		orderLineItemEventHandler.createOrderLineItem(new OrderCreateEvent(order, Arrays.asList(주문항목_요청1, 주문항목_요청2)));

	}

	@DisplayName("주문 생성 시 주문의 주문 항목들이 메뉴에 존재하지 않으면 생성할 수 없다.")
	@Test
	void 주문_생성_시_주문의_주문_항목들이_메뉴에_존재하지_않으면_생성할_수_없다() {
		OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(1), false);
		Order order = new Order(1L, orderTable);
		OrderLineItemRequest 주문항목_요청1 = new OrderLineItemRequest(1L, 1);
		OrderLineItemRequest 주문항목_요청2 = new OrderLineItemRequest(2L, 1);
		given(menuRepository.findById(메뉴1번.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			orderLineItemEventHandler.createOrderLineItem(
				new OrderCreateEvent(order, Arrays.asList(주문항목_요청1, 주문항목_요청2)));
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 생성 시 주문의 주문 항목이 1개 이상이어야 한다.")
	@Test
	void 주문_생성_시_주문의_주문_항목이_1개_이상이어야_한다() {
		OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(1), false);
		Order order = new Order(1L, orderTable);

		assertThatThrownBy(() -> {
			orderLineItemEventHandler.createOrderLineItem(new OrderCreateEvent(order, Collections.EMPTY_LIST));
		}).isInstanceOf(OrderException.class);
	}
}
