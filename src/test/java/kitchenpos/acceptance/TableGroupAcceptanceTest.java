package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestMethod.*;
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
import kitchenpos.domain.MenuRequest;
import kitchenpos.domain.MenuGroupRequest;
import kitchenpos.domain.MenuProductRequest;
import kitchenpos.domain.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.domain.TableGroupRequest;

public class TableGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 그룹 등록 및 그룹 해제 시나리오")
	@Test
	void createTableGroupAndUngroupScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroupRequest("인기 메뉴"));
		MenuGroupRequest menuGroup = menuGroupResponse.as(MenuGroupRequest.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new ProductRequest("매운 라면", new BigDecimal(8000)));
		ProductRequest product = productResponse.as(ProductRequest.class);
		// And
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(product.getId(), 2L))));
		MenuRequest createdMenu = menuCreatedResponse.as(MenuRequest.class);
		// And
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = createOrderTable(new OrderTableRequest(5, true));
		OrderTableRequest createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTableRequest.class);
		// And
		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTableRequest(10, true));
		OrderTableRequest createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTableRequest.class);

		// Scenario
		// When
		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)));
		TableGroupRequest createdTableGroup = tableGroupCreatedResponse.as(TableGroupRequest.class);
		// Then
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdTableGroup.getOrderTables().size()).isEqualTo(2);
	}

	@DisplayName("메뉴 그룹 오류 시나리오")
	@Test
	void tableGroupErrorScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = createOrderTable(new OrderTableRequest(5, true));
		OrderTableRequest createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTableRequest.class);

		// Scenario
		// When
		ExtractableResponse<Response> createTableGroupWithOneOrderTableResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople)));
		// Then
		assertThat(createTableGroupWithOneOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> tableGroupWithNotExistsOrderTableCreatedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(new OrderTableRequest(0L, 5, true))));
		// Then
		assertThat(tableGroupWithNotExistsOrderTableCreatedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 기 단체 지정
		ExtractableResponse<Response> tableWithSevenPeopleCreatedResponse = createOrderTable(new OrderTableRequest(7, true));
		OrderTableRequest createdOrderTableWithSevenPeople = tableWithSevenPeopleCreatedResponse.as(OrderTableRequest.class);

		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTableRequest(10, true));
		OrderTableRequest createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTableRequest.class);

		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithSevenPeople)));
		TableGroupRequest createdTableGroup = tableGroupCreatedResponse.as(TableGroupRequest.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		// When
		ExtractableResponse<Response> createTableGroupWithAlreadyTableGroupedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithSevenPeople)));
		// Then
		assertThat(createTableGroupWithAlreadyTableGroupedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given
		ExtractableResponse<Response> tableWithElevenPeopleAndNotEmptyCreatedResponse = createOrderTable(new OrderTableRequest(11, false));
		OrderTableRequest createdOrderTableWithElevenPeopleAndNotEmpty = tableWithElevenPeopleAndNotEmptyCreatedResponse.as(
			OrderTableRequest.class);
		// When
		ExtractableResponse<Response> createTableGroupWithNotEmptyTableResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(createdOrderTableWithSevenPeople, createdOrderTableWithElevenPeopleAndNotEmpty)));
		// Then
		assertThat(createTableGroupWithNotEmptyTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
