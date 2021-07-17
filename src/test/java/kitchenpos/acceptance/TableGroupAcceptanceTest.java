package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.acceptance.OrderAcceptanceTestMethod.*;
import static kitchenpos.acceptance.ProductAcceptanceTestMethod.*;
import static kitchenpos.acceptance.TableAcceptanceTestMethod.*;
import static kitchenpos.acceptance.TableGroupAcceptanceTestMethod.*;
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
import kitchenpos.domain.TableGroup;

public class TableGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 그룹 등록 및 그룹 해제 시나리오")
	@Test
	void createTableGroupAndUngroupScenario() {
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
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = createOrderTable(new OrderTable(5, true));
		OrderTable createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTable.class);
		// And
		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTable(10, true));
		OrderTable createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTable.class);

		// Scenario
		// When
		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)));
		TableGroup createdTableGroup = tableGroupCreatedResponse.as(TableGroup.class);
		// Then
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdTableGroup.getOrderTables().size()).isEqualTo(2);
	}

	@DisplayName("메뉴 그룹 오류 시나리오")
	@Test
	void tableGroupErrorScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = createOrderTable(new OrderTable(5, true));
		OrderTable createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTable.class);

		// Scenario
		// When
		ExtractableResponse<Response> createTableGroupWithOneOrderTableResponse = createTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople)));
		// Then
		assertThat(createTableGroupWithOneOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> tableGroupWithNotExistsOrderTableCreatedResponse = createTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(new OrderTable(0L, 5, true))));
		// Then
		assertThat(tableGroupWithNotExistsOrderTableCreatedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 기 단체 지정
		ExtractableResponse<Response> tableWithSevenPeopleCreatedResponse = createOrderTable(new OrderTable(7, true));
		OrderTable createdOrderTableWithSevenPeople = tableWithSevenPeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTable(10, true));
		OrderTable createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithSevenPeople)));
		TableGroup createdTableGroup = tableGroupCreatedResponse.as(TableGroup.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		// When
		ExtractableResponse<Response> createTableGroupWithAlreadyTableGroupedResponse = createTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithSevenPeople)));
		// Then
		assertThat(createTableGroupWithAlreadyTableGroupedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given
		ExtractableResponse<Response> tableWithElevenPeopleAndNotEmptyCreatedResponse = createOrderTable(new OrderTable(11, false));
		OrderTable createdOrderTableWithElevenPeopleAndNotEmpty = tableWithElevenPeopleAndNotEmptyCreatedResponse.as(OrderTable.class);
		// When
		ExtractableResponse<Response> createTableGroupWithNotEmptyTableResponse = createTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithSevenPeople, createdOrderTableWithElevenPeopleAndNotEmpty)));
		// Then
		assertThat(createTableGroupWithNotEmptyTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
