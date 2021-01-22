package kitchenpos.order.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderRestControllerTest extends MockMvcTest {

	@DisplayName("주문을 요청한다.")
	@Test
	void create() throws Exception {
		long tableId = 2L;
		setEmpty(tableId);
		OrderRequest_Create request = createOrderRequest(tableId);

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/orders", request))
				.andExpect(status().isCreated())
				.andReturn();

		OrderResponse created = toObject(mvcResult, OrderResponse.class);
		assertThat(created.getId()).isNotNull();
	}

	private OrderRequest_Create createOrderRequest(long tableId) {
		return new OrderRequest_Create(Arrays.asList(getOrderLineItem(1L), getOrderLineItem(2L)), tableId);
	}

	private void setEmpty(long tableId) throws Exception {
		OrderTableRequest_ChangeEmpty request = new OrderTableRequest_ChangeEmpty(false);
		String uri = String.format("/api/tables/%d/empty", tableId);

		mockMvc.perform(putAsJson(uri, request));
	}

	private OrderLineItemRequest getOrderLineItem(long menuId) {
		return new OrderLineItemRequest(menuId, 1);
	}

	@DisplayName("주문을 조회한다.")
	@Test
	void list() throws Exception {
		// given
		final long tableId = 8L;
		setEmpty(tableId);
		mockMvc.perform(postAsJson("/api/orders", createOrderRequest(tableId)));

		// when
		MvcResult mvcResult = mockMvc.perform(get("/api/orders")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		// then
		List<OrderResponse> orders = toList(mvcResult, OrderResponse.class);
		assertThat(orders).isNotEmpty();
	}

	@DisplayName("주문의 상태를 변경한다.")
	@Test
	void changeOrderStatus() throws Exception {
		// given
		final long tableId = 7L;
		setEmpty(tableId);
		MvcResult createMvcResult = mockMvc.perform(postAsJson("/api/orders", createOrderRequest(tableId)))
				.andReturn();
		OrderResponse created = toObject(createMvcResult, OrderResponse.class);
		OrderRequest_ChangeStatus request = new OrderRequest_ChangeStatus(OrderStatus.COMPLETION);

		// when then
		String uri = String.format("/api/orders/%d/order-status", created.getId());
		mockMvc.perform(putAsJson(uri, request))
				.andExpect(status().isOk());
	}
}
