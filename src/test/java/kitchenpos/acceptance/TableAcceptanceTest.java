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
import kitchenpos.domain.TableGroup;

public class TableAcceptanceTest extends AcceptanceTest {
	@DisplayName("주문 테이블 등록, 인원수 조정, 빈테이블로 변경 및 조회 시나리오")
	@Test
	void createOrderTableAndChangeNumberOfGuestAndChangeEmptyAndFindScenario() {
		// Scenario
		// When : 주문 테이블 등록
		ExtractableResponse<Response> tableCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(10, false))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTable = tableCreatedResponse.as(OrderTable.class);

		// Then
		assertThat(tableCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(10);

		// When : 인원수 조정
		ExtractableResponse<Response> changeNumberResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(5))
			.when().put("/api/tables/" + createdOrderTable.getId() + "/number-of-guests")
			.then().log().all()
			.extract();
		OrderTable changeNumberOrderTable = changeNumberResponse.as(OrderTable.class);

		// Then
		assertThat(changeNumberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeNumberOrderTable.getNumberOfGuests()).isEqualTo(5);

		// When : 빈 테이블로 변경
		ExtractableResponse<Response> changeEmptyResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(true))
			.when().put("/api/tables/" + createdOrderTable.getId() + "/empty")
			.then().log().all()
			.extract();
		OrderTable changeEmptyOrderTable = changeEmptyResponse.as(OrderTable.class);

		// Then
		assertThat(changeNumberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeEmptyOrderTable.isEmpty()).isEqualTo(true);

		// When : 주문 테이블 조회
		ExtractableResponse<Response> findOrderTableResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/tables")
			.then().log().all()
			.extract();

		boolean idOrderTableEmpty = findOrderTableResponse.jsonPath().getList(".", OrderTable.class).stream()
			.filter(orderTable -> orderTable.getId() == createdOrderTable.getId())
			.map(OrderTable::isEmpty)
			.findFirst()
			.get()
			;

		// Then
		assertThat(findOrderTableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(idOrderTableEmpty).isEqualTo(true);
	}

	@DisplayName("주문 테이블 오류 시나리오")
	@Test
	void orderTableErrorScenario() {
		// Scenario
		// When : 빈 테이블 등록
		ExtractableResponse<Response> changeNumberWithNotExistsOrderTableResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(true))
			.when().put("/api/tables/" + 0L + "/empty")
			.then().log().all()
			.extract();
		// Then
		assertThat(changeNumberWithNotExistsOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 테이블 등록
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(5, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(10, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableGroupCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(
				LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		TableGroup createdTableGroup = tableGroupCreatedResponse.as(TableGroup.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When : 테이블 비움
		ExtractableResponse<Response> changeEmptyWithTableGroupAlreadyExistsResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(true))
			.when().put("/api/tables/" + createdOrderTableWithFivePeople.getId() + "/empty")
			.then().log().all()
			.extract();
		// Then
		assertThat(changeEmptyWithTableGroupAlreadyExistsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 등록
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

		Order cookingOrder = new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(createdMenu.getId(), 2)));

		ExtractableResponse<Response> orderCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(cookingOrder)
			.when().post("/api/orders")
			.then().log().all()
			.extract();
		Order createdOrder = orderCreatedResponse.as(Order.class);

		assertThat(orderCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When : 빈 테이블로 변경
		ExtractableResponse<Response> changeEmptyWithCookingOrderResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(true))
			.when().put("/api/tables/" + createdOrderTable.getId() + "/empty")
			.then().log().all()
			.extract();
		// Then
		assertThat(changeEmptyWithCookingOrderResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 음수로 테이블 인원 변경
		ExtractableResponse<Response> changeNumberWithMinusNumberResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(-1))
			.when().put("/api/tables/" + createdOrderTable.getId() + "/number-of-guests")
			.then().log().all()
			.extract();
		// Then
		assertThat(changeNumberWithMinusNumberResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 미존재 테이블에 대한 변경 요청
		ExtractableResponse<Response> changeNumberWithNotExistsTableResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(8))
			.when().put("/api/tables/" + 0L + "/number-of-guests")
			.then().log().all()
			.extract();
		// Then
		assertThat(changeNumberWithNotExistsTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 테이블 등록
		ExtractableResponse<Response> tableWithOnePeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(1, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithOnePeople = tableWithOnePeopleCreatedResponse.as(OrderTable.class);
		// When : 비어 있는 테이블에 대한 인원 변경 요청
		ExtractableResponse<Response> changeNumberWithEmptyTableResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(8))
			.when().put("/api/tables/" + createdOrderTableWithOnePeople.getId() + "/number-of-guests")
			.then().log().all()
			.extract();
		// Then
		assertThat(changeNumberWithEmptyTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
