package kitchenpos.ui;

import static kitchenpos.common.TestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;

@DisplayName("OrderRestController 테스트")
class OrderRestControllerTest extends BaseControllerTest {

	@DisplayName("Order 생성 요청")
	@Test
	void create() throws Exception {
		int expectedId = 6;
		long tableId = 7L;
		OrderRequest order = TestDataUtil.createOrder(tableId, Arrays.asList(주문_메뉴1, 주문_메뉴2));

		mockMvc.perform(post("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(order)))
			.andDo(print())
			.andExpect(header().string("Location", "/api/orders/" + expectedId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(expectedId))
			.andExpect(jsonPath("$.orderTable.id").value(tableId))
			.andExpect(jsonPath("$.orderLineItems", Matchers.hasSize(2)));
		;
	}

	@DisplayName("Order 목록 조회")
	@Test
	void list() throws Exception {
		mockMvc.perform(get("/api/orders"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(5)));
	}

	@DisplayName("Order 상태 변경")
	@Test
	void changeOrderStatus() throws Exception {
		long targetId = 1L;
		OrderRequest order = TestDataUtil.createOrderByIdAndStatus(targetId, OrderStatus.MEAL);
		mockMvc.perform(put("/api/orders/{orderId}/order-status", targetId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(order)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(targetId))
			.andExpect(jsonPath("$.orderStatus").value(OrderStatus.MEAL.name()));
	}
}