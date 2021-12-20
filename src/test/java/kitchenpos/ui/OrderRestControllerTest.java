package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

class OrderRestControllerTest extends IntegrationTest {

	private static final String BASE_PATH = "/api/orders";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@DisplayName("주문 등록")
	@Test
	void create() throws Exception {
		//given
		List<OrderLineItem> orderLineItems = Arrays.asList(
			new OrderLineItem(1L, 1),
			new OrderLineItem(2L, 3));
		Map<String, Object> params = 주문정보(1L, orderLineItems);
		Order expectedOrder = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
		given(orderService.create(any()))
			.willReturn(expectedOrder);

		//when
		MockHttpServletResponse response = mockMvc.perform(post(BASE_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(params))
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		Order savedOrder = objectMapper.readValue(response.getContentAsString(), Order.class);
		assertThat(savedOrder).isEqualTo(expectedOrder);
	}

	@DisplayName("주문 목록 조회")
	@Test
	void list() throws Exception {
		//given
		List<OrderLineItem> orderLineItems1 = Arrays.asList(
			new OrderLineItem(1L, 1),
			new OrderLineItem(2L, 3));

		List<OrderLineItem> orderLineItems2 = Arrays.asList(
			new OrderLineItem(1L, 2),
			new OrderLineItem(3L, 2));

		List<Order> expectedOrders = Arrays.asList(
			new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems1),
			new Order(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems2)
		);
		given(orderService.list())
			.willReturn(expectedOrders);

		//when
		MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH)
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		List<Order> findOrders = objectMapper.readValue(response.getContentAsString(),
			new TypeReference<List<Order>>() {
			});
		assertThat(findOrders).containsAll(expectedOrders);
	}

	@DisplayName("주문_상태_변경")
	@Test
	void changeOrderStatus() throws Exception {
		//given
		List<OrderLineItem> orderLineItems = Arrays.asList(
			new OrderLineItem(1L, 1),
			new OrderLineItem(2L, 3));
		String changeOrderStatus = OrderStatus.MEAL.name();
		Map<String, String> params = 주문_상태_변경_정보(changeOrderStatus);
		Order expectedOrder = new Order(1L, 1L, changeOrderStatus, LocalDateTime.now(), orderLineItems);
		given(orderService.changeOrderStatus(any(), any()))
			.willReturn(expectedOrder);

		//when
		MockHttpServletResponse response = mockMvc.perform(
			put(BASE_PATH + "/{orderId}/order-status", expectedOrder.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(params))
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Order changedOrder = objectMapper.readValue(response.getContentAsString(), Order.class);
		assertThat(changedOrder).isEqualTo(expectedOrder);
	}

	private Map<String, Object> 주문정보(long orderTableId, List<OrderLineItem> orderLineItems) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderTableId", String.valueOf(orderTableId));
		params.put("orderLineItems", orderLineItems);
		return params;
	}

	private Map<String, String> 주문_상태_변경_정보(String orderStatus) {
		Map<String, String> params = new HashMap<>();
		params.put("orderStatus", orderStatus);
		return params;
	}

}
