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

public class TableGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 그룹 등록 및 그룹 해제 시나리오")
	@Test
	void createTableGroupAndUngroupScenario() {
		// Backgroud
		// Given
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

		// Scenario
		// When
		ExtractableResponse<Response> tableGroupCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		TableGroup createdTableGroup = tableGroupCreatedResponse.as(TableGroup.class);
		// Then
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdTableGroup.getOrderTables().size()).isEqualTo(2);

		// When
		ExtractableResponse<Response> ungroupedTableGroupResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)))
			.when().delete("/api/table-groups/" + createdTableGroup.getId())
			.then().log().all()
			.extract();
		// Then
		assertThat(ungroupedTableGroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("메뉴 그룹 오류 시나리오")
	@Test
	void tableGroupErrorScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(5, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTable.class);

		// Scenario
		// When
		ExtractableResponse<Response> createTableGroupWithOneOrderTableResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople)))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		// Then
		assertThat(createTableGroupWithOneOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> tableGroupWithNotExistsOrderTableCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(LocalDateTime.now(), Arrays.asList(new OrderTable(0L, 5, true))))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		// Then
		assertThat(tableGroupWithNotExistsOrderTableCreatedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 기 단체 지정
		ExtractableResponse<Response> tableWithSevenPeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(7, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithSevenPeople = tableWithSevenPeopleCreatedResponse.as(OrderTable.class);

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
				LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithSevenPeople)))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		TableGroup createdTableGroup = tableGroupCreatedResponse.as(TableGroup.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		// When
		ExtractableResponse<Response> createTableGroupWithAlreadyTableGroupedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(
				LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithSevenPeople)))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		// Then
		assertThat(createTableGroupWithAlreadyTableGroupedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given
		ExtractableResponse<Response> tableWithElevenPeopleAndNotEmptyCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(11, false))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithElevenPeopleAndNotEmpty = tableWithTenPeopleCreatedResponse.as(OrderTable.class);
		// When
		ExtractableResponse<Response> createTableGroupWithNotEmptyTableResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(
				LocalDateTime.now(), Arrays.asList(createdOrderTableWithSevenPeople, createdOrderTableWithElevenPeopleAndNotEmpty)))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		// Then
		assertThat(tableGroupWithNotExistsOrderTableCreatedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

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
		// ExtractableResponse<Response> tableCreatedResponse = RestAssured
		// 	.given().log().all()
		// 	.contentType(MediaType.APPLICATION_JSON_VALUE)
		// 	.body(new OrderTable(2, false))
		// 	.when().post("/api/tables/")
		// 	.then().log().all()
		// 	.extract();
		// OrderTable createdOrderTable = tableCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableWithTwoPeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(2, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithTwoePeople = tableWithTwoPeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableWithTreePeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(3, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithThreePeople = tableWithTreePeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> newTableGroupCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithTwoePeople, createdOrderTableWithThreePeople)))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		TableGroup newCreatedTableGroup = newTableGroupCreatedResponse.as(TableGroup.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// And : 채운다.
		// ExtractableResponse<Response> changeNumberResponse = RestAssured
		// 	.given().log().all()
		// 	.contentType(MediaType.APPLICATION_JSON_VALUE)
		// 	.body(new OrderTable(false))
		// 	.when().put("/api/tables/"+ createdOrderTableWithTwoePeople.getId() +"/empty")
		// 	.then().log().all()
		// 	.extract();
		// OrderTable changeNumberOrderTable = changeNumberResponse.as(OrderTable.class);

		Order cookingOrder = new Order(createdOrderTableWithTwoePeople.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(createdMenu.getId(), 2)));

		ExtractableResponse<Response> orderCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(cookingOrder)
			.when().post("/api/orders")
			.then().log().all()
			.extract();
		Order createdOrder = orderCreatedResponse.as(Order.class);

		assertThat(orderCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When
		ExtractableResponse<Response> ungroupWithCookingOrderResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/api/table-groups/" + createdOrderTableWithTwoePeople.getTableGroupId())
			.then().log().all()
			.extract();
		// Then
		assertThat(ungroupWithCookingOrderResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
