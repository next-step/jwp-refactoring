package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
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
		ExtractableResponse<Response> menuGroupResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new MenuGroup("인기 메뉴"))
			.when().post("/api/menu-groups")
			.then().log().all()
			.extract();
		MenuGroup menuGroup = menuGroupResponse.as(MenuGroup.class);

		// And
		ExtractableResponse<Response> productResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new Product("매운 라면", new BigDecimal(8000)))
			.when().post("/api/products")
			.then().log().all()
			.extract();
		Product product = productResponse.as(Product.class);

		// And
		ExtractableResponse<Response> menuCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new Menu("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProduct(product.getId(), 2L))))
			.when().post("/api/menus")
			.then().log().all()
			.extract();
		Menu createdMenu = menuCreatedResponse.as(Menu.class);

		// And
		ExtractableResponse<Response> tableCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(2, false))
			.when().post("/api/tables/")
			.then().log().all()
			.extract();
		OrderTable createdOrderTable = tableCreatedResponse.as(OrderTable.class);

		// Scenario
		// When
		Order cookingOrder = new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(createdMenu.getId(), 2)));

		ExtractableResponse<Response> orderCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(cookingOrder)
			.when().post("/api/orders")
			.then().log().all()
			.extract();
		Order createdOrder = orderCreatedResponse.as(Order.class);

		// Then
		assertThat(orderCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdOrder.getOrderStatus()).isEqualTo("COOKING");

		// When
		ExtractableResponse<Response> changeOrderResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new Order("MEAL"))
			.when().put("/api/orders/" + createdOrder.getId() + "/order-status")
			.then().log().all()
			.extract();
		Order statusChangedOrder = changeOrderResponse.as(Order.class);

		// Then
		assertThat(changeOrderResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(statusChangedOrder.getOrderStatus()).isEqualTo("MEAL");

		// When
		ExtractableResponse<Response> findOrderResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/orders")
			.then().log().all()
			.extract();

		String orderStatus = findOrderResponse.jsonPath().getList(".", Order.class).stream()
			.filter(order -> order.getId() == createdOrder.getId())
			.map(Order::getOrderStatus)
			.findFirst()
			.get()
			;

		assertThat(orderStatus).isEqualTo("MEAL");
	}
}
