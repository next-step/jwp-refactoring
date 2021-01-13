package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.TestDomainConstructor;

@DisplayName("주문 Controller 테스트")
public class OrderRestControllerTest extends BaseControllerTest {

	private OrderLineItem orderLineItem;
	private OrderLineItem orderLineItem2;
	private List<OrderLineItem> orderLineItems;
	private Order order;
	private int orderItemSize;
	private static final Long NEW_ORDER_ID = 1L;
	private static final Long NOT_EMPTY_ORDER_TABLE_ID = 9L;

	@BeforeEach
	public void setUp() {
		super.setUp();
		orderLineItem = TestDomainConstructor.orderLineItem(null, 1L, 2);
		orderLineItem2 = TestDomainConstructor.orderLineItem(null, 2L, 1);
		orderLineItems = Arrays.asList(orderLineItem, orderLineItem2);
		orderItemSize = orderLineItems.size();
		order = TestDomainConstructor.order(
			NOT_EMPTY_ORDER_TABLE_ID, null, null, Arrays.asList(orderLineItem, orderLineItem2));
	}

	@Test
	@DisplayName("주문을 등록할 수 있다 - 주문 등록 후, 등록된 주문의 아이디를 포함한 정보를 반환한다.")
	void create() throws Exception {
		//when-then
		mockMvc.perform(post("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(order)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
			.andExpect(jsonPath("$.orderedTime").isNotEmpty())
			.andExpect(jsonPath("$..seq").isNotEmpty())
			.andExpect(jsonPath("$..orderId").isNotEmpty());
	}

	@Test
	@DisplayName("주문의 목록을 조회할 수 있다.")
	void list() throws Exception {
		//when-then
		mockMvc.perform(get("/api/orders"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isNotEmpty())
			.andExpect(jsonPath("$..id").isNotEmpty())
			.andExpect(jsonPath("$..orderStatus").isNotEmpty())
			.andExpect(jsonPath("$..orderedTime").isNotEmpty())
			.andExpect(jsonPath("$..orderLineItems").isNotEmpty())
			.andExpect(jsonPath("$..seq").isNotEmpty())
			.andExpect(jsonPath("$..orderId").isNotEmpty())
			.andExpect(jsonPath("$..menuId").isNotEmpty())
			.andExpect(jsonPath("$..quantity").isNotEmpty());
	}

	@Test
	@DisplayName("주문 상태를 변경할 수 있다.")
	void changeOrderStatus() throws Exception {
		//given
		create();
		Order savedOrder = TestDomainConstructor.orderWithId(NOT_EMPTY_ORDER_TABLE_ID, OrderStatus.MEAL.name(),
			LocalDateTime.now(), orderLineItems, NEW_ORDER_ID);

		//when-then
		mockMvc.perform(put(String.format("/api/orders/%d/order-status", NEW_ORDER_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(savedOrder)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(NEW_ORDER_ID))
			.andExpect(jsonPath("$.orderStatus").value(OrderStatus.MEAL.name()))
			.andExpect(jsonPath("$.orderedTime").isNotEmpty())
			.andExpect(jsonPath("$..seq", hasSize(orderItemSize)))
			.andExpect(jsonPath("$..orderId", hasSize(orderItemSize)));
	}
}

