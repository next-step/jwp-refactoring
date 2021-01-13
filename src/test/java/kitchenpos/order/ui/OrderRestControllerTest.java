package kitchenpos.order.ui;

import static kitchenpos.utils.TestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

@DisplayName("주문 Controller 테스트")
public class OrderRestControllerTest extends BaseControllerTest {

	private static final Long 주문가능한_TABLE_ID = 테이블_비어있지않은_2명_9.getId();
	private static final OrderRequest CHANGE_STATUS_REQUEST = new OrderRequest(OrderStatus.MEAL.name());
	private OrderLineItemRequest orderLineItemRequest;
	private OrderLineItemRequest orderLineItemRequest2;
	private List<OrderLineItemRequest> orderLineItemRequests;

	@BeforeEach
	public void setUp() {
		orderLineItemRequest = new OrderLineItemRequest(메뉴_후라이드.getId(), 1);
		orderLineItemRequest2 = new OrderLineItemRequest(메뉴_양념치킨.getId(), 1);
		orderLineItemRequests = Arrays.asList(orderLineItemRequest, orderLineItemRequest2);
	}

	@Test
	@DisplayName("주문을 등록할 수 있다 - 주문 등록 후, 등록된 주문의 아이디를 포함한 정보를 반환한다.")
	void create() throws Exception {
		//given
		OrderRequest orderRequest = new OrderRequest(주문가능한_TABLE_ID, orderLineItemRequests);

		//when-then
		mockMvc.perform(post("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderRequest)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.orderStatus").value(주문_신규_주문상태))
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
		//when-then
		mockMvc.perform(put(String.format("/api/orders/%d/order-status", 주문_조리중_테이블11.getId()))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(CHANGE_STATUS_REQUEST)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(주문_조리중_테이블11.getId()))
			.andExpect(jsonPath("$.orderStatus").value(CHANGE_STATUS_REQUEST.getOrderStatus()))
			.andExpect(jsonPath("$.orderedTime").isNotEmpty())
			.andExpect(jsonPath("$..seq").isNotEmpty())
			.andExpect(jsonPath("$..orderId").isNotEmpty());
	}
}

