package kitchenpos.ui.order;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.OrderRestController;

@DisplayName("주문 컨트롤러 테스트")
@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OrderService orderService;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();
	}

	@DisplayName("create")
	@Test
	public void create() throws Exception {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
		given(orderService.create(any())).willReturn(order);

		mockMvc.perform(post("/api/orders")
			.content(objectMapper.writeValueAsString(order))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/orders/" + order.getId()))
		;
	}

	@DisplayName("list")
	@Test
	public void list() throws Exception {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
		given(orderService.list()).willReturn(Arrays.asList(order));

		mockMvc.perform(get("/api/orders"))
			.andExpect(status().isOk())
		;
	}

	@DisplayName("changeOrderStatus")
	@Test
	public void changeOrderStatus() throws Exception {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
		given(orderService.changeOrderStatus(anyLong(), any())).willReturn(order);

		mockMvc.perform(put("/api/orders/" + order.getId() + "/order-status")
			.content(objectMapper.writeValueAsString(order))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		;
	}
}
