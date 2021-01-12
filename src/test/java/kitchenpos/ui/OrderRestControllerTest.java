package kitchenpos.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderRestControllerTest extends MockMvcTest {

	@DisplayName("주문을 요청한다.")
	@Test
	void create() throws Exception {
		Order order = createOrder(2L);

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/orders", order))
				.andExpect(status().isCreated())
				.andReturn();

		Order created = toObject(mvcResult, Order.class);
		assertThat(created.getId()).isNotNull();
	}

	private Order createOrder(long tableId) throws Exception {
		Order order = new Order();
		order.setOrderLineItems(Arrays.asList(getOrderLineItem(1L), getOrderLineItem(2L)));
		order.setOrderTableId(tableId);
		setEmpty(tableId);
		return order;
	}

	private void setEmpty(long tableId) throws Exception {
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(false);
		String uri = String.format("/api/tables/%d/empty", tableId);

		mockMvc.perform(putAsJson(uri, orderTable));
	}

	private OrderLineItem getOrderLineItem(long id) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(id);
		orderLineItem.setQuantity(1);
		return orderLineItem;
	}

	@DisplayName("주문을 조회한다.")
	@Test
	void list() throws Exception {
		mockMvc.perform(postAsJson("/api/orders", createOrder(8L)));

		MvcResult mvcResult = mockMvc.perform(get("/api/orders")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<Order> orders = toList(mvcResult, Order.class);
		assertThat(orders).isNotEmpty();
	}

	@DisplayName("주문의 상태를 변경한다.")
	@Test
	void changeOrderStatus() throws Exception {
		// given
		MvcResult createMvcResult = mockMvc.perform(postAsJson("/api/orders", createOrder(7L)))
				.andReturn();
		Order created = toObject(createMvcResult, Order.class);
		Order param = new Order();
		param.setOrderStatus(OrderStatus.COMPLETION.name());
		String uri = String.format("/api/orders/%d/order-status", created.getId());

		// when then
		mockMvc.perform(putAsJson(uri, created))
				.andExpect(status().isOk());
	}
}
