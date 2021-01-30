package kitchenpos.order;

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

import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableResponse;

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
		OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L, 2, false);
		OrderResponse orderResponse = new OrderResponse(1L, orderTableResponse, "COOKING",
			Collections.singletonList(new OrderLineItemResponse(1L, 1L, 1L, 2)));

		when(orderService.create(any(OrderRequest.class))).thenReturn(orderResponse);

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
		OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L, 2, false);
		OrderResponse orderResponse = new OrderResponse(1L, orderTableResponse, "COOKING",
			Collections.singletonList(new OrderLineItemResponse(1L, 1L, 1L, 2)));

		List<OrderResponse> orderResponses = Collections.singletonList(orderResponse);

		when(orderService.list()).thenReturn(orderResponses);

		mockMvc.perform(get("/api/orders"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(orderResponses)));
	}

	@DisplayName("주문 상태 변경 요청")
	@Test
	void changeOrderStatus() throws Exception {
		OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L, 2, false);
		OrderResponse orderResponse = new OrderResponse(1L, orderTableResponse, "MEAL",
			Collections.singletonList(new OrderLineItemResponse(1L, 1L, 1L, 2)));

		when(orderService.changeOrderStatus(anyLong(), any(OrderRequest.class))).thenReturn(orderResponse);

		mockMvc.perform(
			put("/api/orders/1/order-status")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\n"
					+ "  \"orderStatus\": \"MEAL\"\n"
					+ "}")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(objectMapper.writeValueAsString(orderResponse)));
	}
}
