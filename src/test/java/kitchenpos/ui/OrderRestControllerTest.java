package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@DisplayName("주문 기능")
@SpringBootTest
class OrderRestControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderRestController orderRestController;

	private Order order;

	@BeforeEach
	void setup() {
		// MockMvc
		mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
			.addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
			.alwaysDo(print())
			.build();

		order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus(String.valueOf(OrderStatus.COMPLETION));
	}

	@Test
	@DisplayName("주문을 생성할 수 있다.")
	public void create() throws Exception {
		// given
		given(orderService.create(any())).willReturn(order);

		// when
		final ResultActions actions = 주문_생성_요청();

		// then
		주문_생성에_성공함(actions, order);
	}

	@Test
	@DisplayName("주문 목록을 조회할 수 있다.")
	public void list() throws Exception {
		// given
		given(orderService.list()).willReturn(Arrays.asList(order));

		// when
		final ResultActions actions = 주문_조회_요청();

		// then
		주문_조회에_성공함(actions, order);
	}

	@Test
	@DisplayName("주문 상태를 변경할 수 있다.")
	public void changeOrderStatus() throws Exception {
		// given
		Order changeOrder = new Order();
		changeOrder.setOrderStatus(String.valueOf(OrderStatus.COOKING));
		changeOrder.setId(order.getId());
		changeOrder.setOrderTableId(order.getOrderTableId());
		given(orderService.changeOrderStatus(any(), any())).willReturn(changeOrder);

		// when
		final ResultActions actions = 주문_상태_변경_요청();

		// then
		주문_상태가_변경됨(actions, changeOrder);
	}

	private ResultActions 주문_생성_요청() throws Exception {
		return mockMvc.perform(post("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(order)));
	}

	private void 주문_생성에_성공함(ResultActions actions, Order order) throws Exception {
		actions.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/orders" + "/1"))
			.andExpect(content().string(containsString(order.getOrderStatus())))
			.andExpect(content().string(containsString(String.valueOf(order.getOrderTableId())))
			);
	}

	private ResultActions 주문_조회_요청() throws Exception {
		return mockMvc.perform(get("/api/orders")
			.contentType(MediaType.APPLICATION_JSON));
	}

	private void 주문_조회에_성공함(ResultActions actions, Order order) throws Exception {
		actions.andExpect(status().isOk())
			.andExpect(content().string(containsString(order.getOrderStatus())))
			.andExpect(content().string(containsString(String.valueOf(order.getOrderTableId()))));
	}

	private ResultActions 주문_상태_변경_요청() throws Exception {
		return mockMvc.perform(put("/api/orders/{orderId}/order-status" , order.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(order)));
	}

	private void 주문_상태가_변경됨(ResultActions actions, Order order) throws Exception {
		actions.andExpect(status().isOk())
			.andExpect(content().string(containsString(order.getOrderStatus())))
			.andExpect(content().string(containsString(String.valueOf(order.getOrderTableId()))));
	}

}
