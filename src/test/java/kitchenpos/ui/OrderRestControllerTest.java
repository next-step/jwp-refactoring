package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
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
		Order order = new Order(1L, null, null, null, null);
		given(orderService.create(order)).willReturn(order);

		mockMvc.perform(
				post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", BASE_URL + "/1"));
	}

	@Test
	void listTest() throws Exception {
		List<Order> orders = Lists.list(new Order(), new Order());
		given(orderService.list()).willReturn(orders);

		mockMvc.perform(
				get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	@Test
	void changeOrderStatusTest() throws Exception {
		Order order = new Order(1L, null, OrderStatus.COMPLETION.name(), null, null);
		given(orderService.changeOrderStatus(order.getId(), order)).willReturn(order);

		mockMvc.perform(
				put(BASE_URL + "/{orderId}/order-status", order.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(order)))
				.andExpect(status().isOk());
	}
}
