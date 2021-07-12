package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest extends WebMvcTestConfiguration {
	private Order order1;
	private Order order2;

	@MockBean
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		order1 = new Order(1L, "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(1L, 1L, 5L), new OrderLineItem(1L, 2L, 4L)));
		order2 = new Order(2L, "COMPLETION", LocalDateTime.now(), Arrays.asList(new OrderLineItem(2L, 1L, 3L)));
	}

	@Test
	void createTest() throws Exception {
		when(orderService.create(any())).thenReturn(new Order());

		mockMvc.perform(post("/api/orders")
			.content(objectMapper.writeValueAsString(new Order()))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void listTest() throws Exception {
		// Given
		when(orderService.list()).thenReturn(Arrays.asList(order1, order2));
		// When, Then
		mockMvc.perform(get("/api/orders"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void changeOrderStatus() throws Exception {
		Long orderId = 1L;
		// Given
		given(orderService.changeOrderStatus(orderId, new Order())).willReturn(new Order());
		// When, Then
		mockMvc.perform(put("/api/orders/" + orderId + "/order-status")
			.param("orderId", orderId + "")
			.content(objectMapper.writeValueAsString(new Order()))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
