package kitchenpos.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("주문 컨트롤러 테스트")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
	private static final String BASE_URL = "/api/orders";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private OrderRestController orderRestController;

	@MockBean
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(print())
				.build();
	}

	@Test
	void createTest() throws Exception {
		OrderRequest orderRequest = new OrderRequest(1L, LocalDateTime.of(2021, 1, 1, 1, 1), Arrays.asList(new OrderLineItemRequest()));
		OrderResponse orderResponse = new OrderResponse(1L, 1L, OrderStatus.COOKING, LocalDateTime.of(2021, 1, 1, 1, 1), Arrays.asList(new OrderLineItemResponse()));

		given(orderService.create(orderRequest)).willReturn(orderResponse);

		mockMvc.perform(
				post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", BASE_URL + "/1"));
	}

	@Test
	void listTest() throws Exception {
		List<OrderResponse> orderResponses = Lists.list(new OrderResponse(), new OrderResponse());
		given(orderService.list()).willReturn(orderResponses);

		mockMvc.perform(
				get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	@Test
	void changeOrderStatusTest() throws Exception {
		OrderResponse orderResponse = new OrderResponse(1L, 1L, OrderStatus.COMPLETION, null, Arrays.asList(new OrderLineItemResponse(), new OrderLineItemResponse()));
		given(orderService.changeOrderStatus(orderResponse.getOrderId(), OrderStatus.COMPLETION.name())).willReturn(orderResponse);

		mockMvc.perform(
				put(BASE_URL + "/{orderId}/order-status", orderResponse.getOrderId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderResponse)))
				.andExpect(status().isOk());
	}
}
