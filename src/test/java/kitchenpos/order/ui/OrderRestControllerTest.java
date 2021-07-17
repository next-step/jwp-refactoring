package kitchenpos.order.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.dto.OrderLineItemRequest;
import kitchenpos.WebMvcTestConfiguration;

@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest extends WebMvcTestConfiguration {
	private OrderRequest order1;
	private OrderRequest order2;

	@MockBean
	private OrderService orderService;

	@BeforeEach
	public void setUp() {
		super.setUp();
		order1 = new OrderRequest(1L, "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(1L, 1L, 5L), new OrderLineItemRequest(1L, 2L, 4L)));
		order2 = new OrderRequest(2L, "COMPLETION", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(2L, 1L, 3L)));
	}

	@Test
	void createTest() throws Exception {
		when(orderService.create(any())).thenReturn(new OrderResponse());

		mockMvc.perform(post("/api/orders")
			.content(objectMapper.writeValueAsString(new OrderRequest()))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void listTest() throws Exception {
		// Given
		when(orderService.list()).thenReturn(Arrays.asList(new OrderResponse(), new OrderResponse()));
		// When, Then
		mockMvc.perform(get("/api/orders"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void changeOrderStatus() throws Exception {
		Long orderId = 1L;
		// Given
		given(orderService.changeOrderStatus(orderId, new OrderRequest())).willReturn(new OrderResponse());
		// When, Then
		mockMvc.perform(put("/api/orders/" + orderId + "/order-status")
			.param("orderId", orderId + "")
			.content(objectMapper.writeValueAsString(new OrderRequest()))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
