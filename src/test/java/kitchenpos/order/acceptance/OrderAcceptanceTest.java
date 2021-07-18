package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.order.acceptance.OrderAcceptanceTestMethod.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTestMethod.*;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderLineItemRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderAcceptanceTest extends AcceptanceTest {
	@DisplayName("주문 등록, 상태변경 조회 시나리오")
	@Test
	void createOrderAndChangeStatusAndFindOrderScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroupRequest("인기 메뉴"));
		MenuGroupResponse menuGroup = menuGroupResponse.as(MenuGroupResponse.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new ProductRequest("매운 라면", 8000L));
		ProductResponse product = productResponse.as(ProductResponse.class);
		// And
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(product.getId(), 2L))));
		MenuResponse createdMenu = menuCreatedResponse.as(MenuResponse.class);
		// And
		ExtractableResponse<Response> tableCreatedResponse = createOrderTable(new OrderTableRequest(2, false));
		OrderTableResponse createdOrderTable = tableCreatedResponse.as(OrderTableResponse.class);

		// Scenario
		// When
		ExtractableResponse<Response> orderCreatedResponse = createOrder(new OrderRequest(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(createdMenu.getId(), 2))));
		OrderResponse createdOrder = orderCreatedResponse.as(OrderResponse.class);
		// Then
		assertThat(orderCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdOrder.getOrderStatus()).isEqualTo("COOKING");

		// When
		ExtractableResponse<Response> changeOrderResponse = changeOrderStatus(createdOrder.getId(), new OrderRequest("MEAL"));
		OrderResponse statusChangedOrder = changeOrderResponse.as(OrderResponse.class);
		// Then
		assertThat(changeOrderResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(statusChangedOrder.getOrderStatus()).isEqualTo("MEAL");

		// When
		ExtractableResponse<Response> findOrderResponse = findOrder();
		// Then
		String orderStatus = findOrderResponse.jsonPath().getList(".", OrderRequest.class).stream()
			.filter(order -> order.getId() == createdOrder.getId())
			.map(OrderRequest::getOrderStatus)
			.findFirst()
			.get();
		assertThat(orderStatus).isEqualTo("MEAL");
	}

	@DisplayName("주문 오류 시나리오")
	@Test
	void orderErrorScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroupRequest("인기 메뉴"));
		MenuGroupResponse menuGroup = menuGroupResponse.as(MenuGroupResponse.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new ProductRequest("매운 라면", 8000L));
		ProductResponse product = productResponse.as(ProductResponse.class);
		// And
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(product.getId(), 2L))));
		MenuResponse createdMenu = menuCreatedResponse.as(MenuResponse.class);
		// And
		ExtractableResponse<Response> tableCreatedResponse = createOrderTable(new OrderTableRequest(2, false));
		OrderTableResponse createdOrderTable = tableCreatedResponse.as(OrderTableResponse.class);

		// Scenario
		// When
		ExtractableResponse<Response> orderWithoutOrderListResponse = createOrder(new OrderRequest(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), null));
		// Then
		assertThat(orderWithoutOrderListResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> orderWithNotExistsMenuResponse = createOrder(new OrderRequest(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(0L, 2))));
		// Then
		assertThat(orderWithNotExistsMenuResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> orderWithoutOrderTableResponse = createOrder(new OrderRequest(0L, "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(0L, 2))));
		// Then
		assertThat(orderWithoutOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> changeStatusWithNotExitsOrderResponse = changeOrderStatus(0L, new OrderRequest("MEAL"));
		// Then
		assertThat(changeStatusWithNotExitsOrderResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 등록
		ExtractableResponse<Response> orderCreatedResponse = createOrder(new OrderRequest(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(createdMenu.getId(), 2))));
		OrderResponse createdOrder = orderCreatedResponse.as(OrderResponse.class);
		// When
		ExtractableResponse<Response> changeStatusWithNotExistsStatus = changeOrderStatus(createdOrder.getId(), new OrderRequest("PREPARING"));
		// Then
		assertThat(changeStatusWithNotExistsStatus.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given
		ExtractableResponse<Response> changeOrderResponse = changeOrderStatus(createdOrder.getId(), new OrderRequest("COMPLETION"));
		OrderResponse statusChangedOrder = changeOrderResponse.as(OrderResponse.class);
		// When
		ExtractableResponse<Response> changeStatusWithCompletedOrder = changeOrderStatus(createdOrder.getId(), new OrderRequest("MEAL"));
		// Then
		assertThat(changeStatusWithCompletedOrder.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
