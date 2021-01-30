package kitchenpos.ui;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.order.OrderRestController;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.TableRestController;
import kitchenpos.table.dto.OrderTableRequest;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql",
	"/db/migration/V3__remove_two_way.sql"})
class OrderRestControllerTest {

	@Autowired
	OrderRestController orderRestController;

	@Autowired
	TableRestController tableRestController;

	@Test
	void create() {
		// given
		OrderRequest 주문_요청 = 주문_요청_생성();

		// when
		OrderResponse response = orderRestController.create(주문_요청).getBody();

		// then
		assertAll(
			() -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
			() -> assertThat(response.getOrderTableId()).isEqualTo(주문_테이블1.getId()),
			() -> assertThat(response.getOrderLineItems())
				.map(OrderLineItemResponse::getMenuId)
				.contains(메뉴1.getId(), 메뉴2.getId())
		);
	}

	@Test
	void list() {
		// given
		orderRestController.create(주문_요청_생성());

		// when
		List<OrderResponse> responseList = orderRestController.list().getBody();

		// then
		assertThat(responseList).hasSize(1);
		assertThat(responseList.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
		assertThat(responseList.get(0).getOrderTableId()).isEqualTo(주문_테이블1.getId());
		assertThat(responseList.get(0).getOrderLineItems())
			.map(OrderLineItemResponse::getMenuId)
			.contains(메뉴1.getId(), 메뉴2.getId());
	}

	@Test
	void changeOrderStatus() {
		// given
		Long 주문_ID = orderRestController.create(주문_요청_생성()).getBody().getId();

		// when
		OrderResponse response = orderRestController.changeOrderStatus(주문_ID, new OrderRequest(OrderStatus.COMPLETION))
			.getBody();

		// then
		assertAll(
			() -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION),
			() -> assertThat(response.getOrderTableId()).isEqualTo(주문_테이블1.getId()),
			() -> assertThat(response.getOrderLineItems())
				.map(OrderLineItemResponse::getMenuId)
				.contains(메뉴1.getId(), 메뉴2.getId())
		);
	}

	private OrderRequest 주문_요청_생성() {
		tableRestController.changeEmpty(주문_테이블1.getId(), new OrderTableRequest.Builder().empty(false).build());

		OrderLineItemRequest 주문_항목1_요청 = new OrderLineItemRequest(null, 메뉴1.getId(), 1L);
		OrderLineItemRequest 주문_항목2_요청 = new OrderLineItemRequest(null, 메뉴2.getId(), 2L);
		return new OrderRequest(주문_테이블1.getId(), Arrays.asList(주문_항목1_요청, 주문_항목2_요청));
	}
}
