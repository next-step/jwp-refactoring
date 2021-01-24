package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private OrderService orderService;

	@DisplayName("주문 등록 요청")
	@Test
	void create() throws Exception {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setQuantity(1L);

		Order savedOrder = new Order();
		savedOrder.setId(1L);
		savedOrder.setOrderTableId(1L);
		savedOrder.setOrderLineItems(Collections.singletonList(orderLineItem));

		when(orderService.create(any(Order.class))).thenReturn(savedOrder);

		mockMvc.perform(
			post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\n"
					+ "  \"orderTableId\": 1,\n"
					+ "  \"orderLineItems\": [\n"
					+ "    {\n"
					+ "      \"menuId\": 1,\n"
					+ "      \"quantity\": 1\n"
					+ "    }\n"
					+ "  ]\n"
					+ "}")
		)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/orders/1"));
	}

	@DisplayName("주문 목록 조회 요청")
	@Test
	void list() throws Exception {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setQuantity(1L);

		Order savedOrder = new Order();
		savedOrder.setId(1L);
		savedOrder.setOrderTableId(1L);
		savedOrder.setOrderLineItems(Collections.singletonList(orderLineItem));

		List<Order> orders = Collections.singletonList(savedOrder);

		when(orderService.list()).thenReturn(orders);

		mockMvc.perform(get("/api/orders"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(orders)));
	}

	@DisplayName("주문 상태 변경 요청")
	@Test
	void changeOrderStatus() throws Exception {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setQuantity(1L);

		Order savedOrder = new Order();
		savedOrder.setId(1L);
		savedOrder.setOrderTableId(1L);
		savedOrder.setOrderStatus(OrderStatus.MEAL.name());
		savedOrder.setOrderLineItems(Collections.singletonList(orderLineItem));

		when(orderService.changeOrderStatus(anyLong(), any(Order.class))).thenReturn(savedOrder);

		mockMvc.perform(
			put("/api/orders/1/order-status")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\n"
					+ "  \"orderStatus\": \"MEAL\"\n"
					+ "}")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(savedOrder)));
	}
}