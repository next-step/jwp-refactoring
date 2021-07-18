package kitchenpos.table.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.order.acceptance.OrderAcceptanceTestMethod.*;
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
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.NumberOfGuestsTest;
import kitchenpos.table.dto.OrderLineItemRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;

public class TableAcceptanceTest extends AcceptanceTest {
	@DisplayName("주문 테이블 등록, 인원수 조정, 빈테이블로 변경 및 조회 시나리오")
	@Test
	void createOrderTableAndChangeNumberOfGuestAndChangeEmptyAndFindScenario() {
		// Scenario
		// When : 주문 테이블 등록
		ExtractableResponse<Response> tableCreatedResponse = createOrderTable(new OrderTableRequest(10, false));
		OrderTableResponse createdOrderTable = tableCreatedResponse.as(OrderTableResponse.class);
		// Then
		assertThat(tableCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(10));

		// When : 인원수 조정
		ExtractableResponse<Response> changeNumberResponse = changeNumber(createdOrderTable.getId(), new OrderTableRequest(5));
		OrderTableResponse changeNumberOrderTable = changeNumberResponse.as(OrderTableResponse.class);
		// Then
		assertThat(changeNumberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeNumberOrderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(5));

		// When : 빈 테이블로 변경
		ExtractableResponse<Response> changeEmptyResponse = changeEmpty(createdOrderTable.getId(), new OrderTableRequest(true));
		OrderTableResponse changeEmptyOrderTable = changeEmptyResponse.as(OrderTableResponse.class);

		// Then
		assertThat(changeEmptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeEmptyOrderTable.isEmpty()).isEqualTo(true);

		// When : 주문 테이블 조회
		ExtractableResponse<Response> findOrderTableResponse = findOrderTable();
		// Then
		boolean isOrderTableEmpty = findOrderTableResponse.jsonPath().getList(".", OrderTableResponse.class).stream()
			.filter(orderTable -> orderTable.getId() == createdOrderTable.getId())
			.map(OrderTableResponse::isEmpty)
			.findFirst()
			.get();
		assertThat(findOrderTableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(isOrderTableEmpty).isEqualTo(true);
	}

	@DisplayName("주문 테이블 오류 시나리오")
	@Test
	void orderTableErrorScenario() {
		// Scenario
		// When : 빈 테이블 empty
		ExtractableResponse<Response> changeNumberWithNotExistsOrderTableResponse = changeEmpty(0L, new OrderTableRequest(true));
		// Then
		assertThat(changeNumberWithNotExistsOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 테이블 등록
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = createOrderTable(new OrderTableRequest(5, true));
		OrderTableResponse createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTableResponse.class);

		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTableRequest(10, true));
		OrderTableResponse createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTableResponse.class);

		OrderTableRequest orderTableWithFivePeople = new OrderTableRequest(createdOrderTableWithFivePeople.getId(), null, createdOrderTableWithFivePeople.getNumberOfGuests().getNumberOfGuests(), createdOrderTableWithFivePeople.isEmpty());
		OrderTableRequest orderTableWithTenPeople = new OrderTableRequest(createdOrderTableWithTenPeople.getId(), null, createdOrderTableWithTenPeople.getNumberOfGuests().getNumberOfGuests(), createdOrderTableWithTenPeople.isEmpty());

		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		TableGroupResponse createdTableGroup = tableGroupCreatedResponse.as(TableGroupResponse.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When : 테이블 비움
		ExtractableResponse<Response> changeEmptyWithTableGroupAlreadyExistsResponse = changeEmpty(createdOrderTableWithFivePeople.getId(), new OrderTableRequest(true));
		// Then
		assertThat(changeEmptyWithTableGroupAlreadyExistsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 등록
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
		// And
		ExtractableResponse<Response> orderCreatedResponse = createOrder(new OrderRequest(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(createdMenu.getId(), 2))));
		OrderResponse createdOrder = orderCreatedResponse.as(OrderResponse.class);
		assertThat(orderCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When : 빈 테이블로 변경
		ExtractableResponse<Response> changeEmptyWithCookingOrderResponse = changeEmpty(createdOrderTable.getId(), new OrderTableRequest(true));
		// Then
		assertThat(changeEmptyWithCookingOrderResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 음수로 테이블 인원 변경
		ExtractableResponse<Response> changeNumberWithMinusNumberResponse = changeNumber(createdOrderTable.getId(), new OrderTableRequest(-1));
		// Then
		assertThat(changeNumberWithMinusNumberResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 미존재 테이블에 대한 변경 요청
		ExtractableResponse<Response> changeNumberWithNotExistsTableResponse = changeNumber(0L, new OrderTableRequest(8));
		// Then
		assertThat(changeNumberWithNotExistsTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 테이블 등록
		ExtractableResponse<Response> tableWithOnePeopleCreatedResponse =  createOrderTable(new OrderTableRequest(1, true));
		OrderTableResponse createdOrderTableWithOnePeople = tableWithOnePeopleCreatedResponse.as(OrderTableResponse.class);
		// When : 비어 있는 테이블에 대한 인원 변경 요청
		ExtractableResponse<Response> changeNumberWithEmptyTableResponse = changeNumber(createdOrderTableWithOnePeople.getId(), new OrderTableRequest(8));
		// Then
		assertThat(changeNumberWithEmptyTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
