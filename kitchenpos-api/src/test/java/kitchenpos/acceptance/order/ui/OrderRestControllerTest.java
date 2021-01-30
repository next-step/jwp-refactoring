package kitchenpos.acceptance.order.ui;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.ui.OrderRestController;

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

	private List<OrderLineItemRequest> orderLineItemRequests;
	private List<OrderLineItemResponse> orderLineItemResponses;

	@BeforeEach
	void setUp() {
		orderLineItemRequests = Arrays.asList(
			OrderLineItemRequest.of(1L, 2),
			OrderLineItemRequest.of(2L, 3)
		);

		orderLineItemResponses = Arrays.asList(
			OrderLineItemResponse.of(1L, 1L, 1L, 2),
			OrderLineItemResponse.of(2L, 1L, 2L, 3)
		);
	}

	@DisplayName("주문을 등록한다.")
	@Test
	void createOrderTest() throws Exception {
		// given
		OrderRequest request = OrderRequest.of(1L, orderLineItemRequests);
		OrderResponse response = OrderResponse.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
			orderLineItemResponses);
		given(orderService.create(any())).willReturn(response);

		// when
		final ResultActions resultActions = mvc.perform(post("/api/orders")
			.content(mapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl("/api/orders" + "/" + response.getId()))
			.andExpect(jsonPath("$.id").value(response.getId()))
			.andDo(log());
	}

	@DisplayName("등록된 주문 목록을 조회한다.")
	@Test
	void selectOrdersTest() throws Exception {
		// given
		OrderResponse response = OrderResponse.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
			orderLineItemResponses);
		given(orderService.list()).willReturn(Collections.singletonList(response));

		// when
		final ResultActions resultActions = mvc.perform(get("/api/orders")
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$.[0].id").value(1L));
	}

	@DisplayName("주문 상태를 변경한다.")
	@Test
	void changeOrderStatusTest() throws Exception {
		// given
		OrderResponse response = OrderResponse.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
			orderLineItemResponses);
		Map<String, String> param = new HashMap<>();
		param.put("orderStatus", OrderStatus.MEAL.name());
		given(orderService.changeOrderStatus(anyLong(), any())).willReturn(response);

		// when
		final ResultActions resultActions = mvc.perform(put("/api/orders/{orderId}/order-status", response.getId())
			.content(mapper.writeValueAsString(param))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk());
	}
}
