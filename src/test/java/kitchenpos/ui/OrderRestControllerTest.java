package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

@DisplayName("주문 Controller 테스트")
@WebMvcTest(OrderRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class OrderRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private OrderService orderService;

	private OrderLineItem orderLineItem;

	@BeforeEach
	void setUp() {
		orderLineItem = OrderLineItem.of(null, null, 1L, 1);
	}

	@DisplayName("주문을 등록한다.")
	@Test
	void createOrderTest() throws Exception {
		// given
		Order order = Order.of(1L, 1L, null, LocalDateTime.now(), Collections.singletonList(orderLineItem));
		given(orderService.create(any())).willReturn(order);

		// when
		final ResultActions resultActions = mvc.perform(post("/api/orders")
			.content(mapper.writeValueAsString(order))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl("/api/orders" + "/" + order.getId()))
			.andExpect(jsonPath("$.id").value(order.getId()))
			.andDo(log());
	}

	@DisplayName("등록된 주문 목록을 조회한다.")
	@Test
	void selectOrdersTest() throws Exception {
		// given
		Order order1 = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
			Collections.singletonList(orderLineItem));
		Order order2 = Order.of(2L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
			Collections.singletonList(orderLineItem));
		given(orderService.list()).willReturn(Arrays.asList(order1, order2));

		// when
		final ResultActions resultActions = mvc.perform(get("/api/orders")
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$.[0].id").value(1L))
			.andExpect(jsonPath("$.[1].id").value(2L));
	}

	@DisplayName("주문 상태를 변경한다.")
	@Test
	void changeOrderStatusTest() throws Exception {
		// given
		Order expected = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
			Collections.singletonList(orderLineItem));
		Order actual = Order.of(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(),
			Collections.singletonList(orderLineItem));
		when(orderService.changeOrderStatus(anyLong(), any(Order.class))).thenReturn(expected);

		// when
		final ResultActions resultActions = mvc.perform(put("/api/orders/{orderId}/order-status", expected.getId())
			.content(mapper.writeValueAsString(actual))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk());
	}
}
