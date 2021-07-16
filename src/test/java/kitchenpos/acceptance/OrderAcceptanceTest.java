package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.acceptance.OrderAcceptanceTestMethod.*;
import static kitchenpos.acceptance.ProductAcceptanceTestMethod.*;
import static kitchenpos.acceptance.TableAcceptanceTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class OrderAcceptanceTest extends AcceptanceTest {
	@DisplayName("주문 등록, 상태변경 조회 시나리오")
	@Test
	void createOrderAndChangeStatusAndFindOrderScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroup("인기 메뉴"));
		MenuGroup menuGroup = menuGroupResponse.as(MenuGroup.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new Product("매운 라면", new BigDecimal(8000)));
		Product product = productResponse.as(Product.class);
		// And
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new Menu("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProduct(product.getId(), 2L))));
		Menu createdMenu = menuCreatedResponse.as(Menu.class);
		// And
		ExtractableResponse<Response> tableCreatedResponse = createOrderTable(new OrderTable(2, false));
		OrderTable createdOrderTable = tableCreatedResponse.as(OrderTable.class);

		// Scenario
		// When
		ExtractableResponse<Response> orderCreatedResponse = createOrder(new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(createdMenu.getId(), 2))));
		Order createdOrder = orderCreatedResponse.as(Order.class);
		// Then
		assertThat(orderCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdOrder.getOrderStatus()).isEqualTo("COOKING");

		// When
		ExtractableResponse<Response> changeOrderResponse = changeOrderStatus(createdOrder.getId(), new Order("MEAL"));
		Order statusChangedOrder = changeOrderResponse.as(Order.class);
		// Then
		assertThat(changeOrderResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(statusChangedOrder.getOrderStatus()).isEqualTo("MEAL");

		// When
		ExtractableResponse<Response> findOrderResponse = findOrder();
		// Then
		String orderStatus = findOrderResponse.jsonPath().getList(".", Order.class).stream()
			.filter(order -> order.getId() == createdOrder.getId())
			.map(Order::getOrderStatus)
			.findFirst()
			.get();
		assertThat(orderStatus).isEqualTo("MEAL");
	}

	@DisplayName("주문 오류 시나리오")
	@Test
	void orderErrorScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroup("인기 메뉴"));
		MenuGroup menuGroup = menuGroupResponse.as(MenuGroup.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new Product("매운 라면", new BigDecimal(8000)));
		Product product = productResponse.as(Product.class);
		// And
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new Menu("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProduct(product.getId(), 2L))));
		Menu createdMenu = menuCreatedResponse.as(Menu.class);
		// And
		ExtractableResponse<Response> tableCreatedResponse = createOrderTable(new OrderTable(2, false));
		OrderTable createdOrderTable = tableCreatedResponse.as(OrderTable.class);

		// Scenario
		// When
		ExtractableResponse<Response> orderWithoutOrderListResponse = createOrder(new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), null));
		// Then
		assertThat(orderWithoutOrderListResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> orderWithNotExistsMenuResponse = createOrder(new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(0L, 2))));
		// Then
		assertThat(orderWithNotExistsMenuResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> orderWithoutOrderTableResponse = createOrder(new Order(0L, "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(0L, 2))));
		// Then
		assertThat(orderWithoutOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> changeStatusWithNotExitsOrderResponse = changeOrderStatus(0L, new Order("MEAL"));
		// Then
		assertThat(changeStatusWithNotExitsOrderResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 등록
		ExtractableResponse<Response> orderCreatedResponse = createOrder(new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(createdMenu.getId(), 2))));
		Order createdOrder = orderCreatedResponse.as(Order.class);
		// When
		ExtractableResponse<Response> changeStatusWithNotExistsStatus = changeOrderStatus(createdOrder.getId(), new Order("PREPARING"));
		// Then
		assertThat(changeStatusWithNotExistsStatus.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given
		ExtractableResponse<Response> changeOrderResponse = changeOrderStatus(createdOrder.getId(), new Order("COMPLETION"));
		Order statusChangedOrder = changeOrderResponse.as(Order.class);
		// When
		ExtractableResponse<Response> changeStatusWithCompletedOrder = changeOrderStatus(createdOrder.getId(), new Order("MEAL"));
		// Then
		assertThat(changeStatusWithCompletedOrder.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
