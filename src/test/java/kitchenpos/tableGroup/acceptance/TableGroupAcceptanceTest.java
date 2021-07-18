package kitchenpos.tableGroup.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTestMethod.*;
import static kitchenpos.table.acceptance.TableAcceptanceTestMethod.*;
import static kitchenpos.tableGroup.acceptance.TableGroupAcceptanceTestMethod.*;
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
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;

public class TableGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 그룹 등록 및 그룹 해제 시나리오")
	@Test
	void createTableGroupAndUngroupScenario() {
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
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = createOrderTable(new OrderTableRequest(5, true));
		OrderTableResponse createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTableResponse.class);
		// And
		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTableRequest(10, true));
		OrderTableResponse createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTableResponse.class);

		// Scenario
		// Given
		OrderTableRequest orderTableWithFivePeople = new OrderTableRequest(createdOrderTableWithFivePeople.getId(), null, createdOrderTableWithFivePeople.getNumberOfGuests().getNumberOfGuests(), createdOrderTableWithFivePeople.isEmpty());
		OrderTableRequest orderTableWithTenPeople = new OrderTableRequest(createdOrderTableWithTenPeople.getId(), null, createdOrderTableWithTenPeople.getNumberOfGuests().getNumberOfGuests(), createdOrderTableWithTenPeople.isEmpty());
		// When
		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		TableGroupResponse createdTableGroup = tableGroupCreatedResponse.as(TableGroupResponse.class);
		// Then
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	@DisplayName("메뉴 그룹 오류 시나리오")
	@Test
	void tableGroupErrorScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = createOrderTable(new OrderTableRequest(5, true));
		OrderTableResponse createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTableResponse.class);
		OrderTableRequest orderTableWithFivePeople = new OrderTableRequest(createdOrderTableWithFivePeople.getId(), null, createdOrderTableWithFivePeople.getNumberOfGuests().getNumberOfGuests(), createdOrderTableWithFivePeople.isEmpty());

		// Scenario
		// When
		ExtractableResponse<Response> createTableGroupWithOneOrderTableResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(new OrderTableRequest(5, true))));
		// Then
		assertThat(createTableGroupWithOneOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> tableGroupWithNotExistsOrderTableCreatedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(new OrderTableRequest(0L, 5, true))));
		// Then
		assertThat(tableGroupWithNotExistsOrderTableCreatedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 기 단체 지정
		ExtractableResponse<Response> tableWithSevenPeopleCreatedResponse = createOrderTable(new OrderTableRequest(7, true));
		OrderTableResponse createdOrderTableWithSevenPeople = tableWithSevenPeopleCreatedResponse.as(OrderTableResponse.class);
		OrderTableRequest orderTableWithSevenPeople = new OrderTableRequest(createdOrderTableWithSevenPeople.getId(), null, createdOrderTableWithSevenPeople.getNumberOfGuests().getNumberOfGuests(), createdOrderTableWithSevenPeople.isEmpty());

		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTableRequest(10, true));
		OrderTableResponse createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTableResponse.class);
		OrderTableRequest orderTableWithTenPeople = new OrderTableRequest(createdOrderTableWithTenPeople.getId(), null, createdOrderTableWithTenPeople.getNumberOfGuests().getNumberOfGuests(), createdOrderTableWithTenPeople.isEmpty());

		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithSevenPeople)));
		TableGroupResponse createdTableGroup = tableGroupCreatedResponse.as(TableGroupResponse.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		// When
		ExtractableResponse<Response> createTableGroupWithAlreadyTableGroupedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithSevenPeople)));
		// Then
		assertThat(createTableGroupWithAlreadyTableGroupedResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given
		ExtractableResponse<Response> tableWithElevenPeopleAndNotEmptyCreatedResponse = createOrderTable(new OrderTableRequest(11, false));
		OrderTableResponse createdOrderTableWithElevenPeopleAndNotEmpty = tableWithElevenPeopleAndNotEmptyCreatedResponse.as(OrderTableResponse.class);
		OrderTableRequest orderTableWithElevenPeopleWithNotEmpty = new OrderTableRequest(createdOrderTableWithElevenPeopleAndNotEmpty.getId(), null, createdOrderTableWithElevenPeopleAndNotEmpty.getNumberOfGuests().getNumberOfGuests(), createdOrderTableWithElevenPeopleAndNotEmpty.isEmpty());
		// When
		ExtractableResponse<Response> createTableGroupWithNotEmptyTableResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(orderTableWithSevenPeople, orderTableWithElevenPeopleWithNotEmpty)));
		// Then
		assertThat(createTableGroupWithNotEmptyTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
