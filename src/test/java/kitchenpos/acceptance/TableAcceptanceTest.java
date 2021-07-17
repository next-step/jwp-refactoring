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
import kitchenpos.domain.MenuRequest;
import kitchenpos.domain.MenuGroupRequest;
import kitchenpos.domain.MenuProductRequest;
import kitchenpos.domain.OrderRequest;
import kitchenpos.domain.OrderLineItemRequest;
import kitchenpos.domain.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.domain.TableGroupRequest;

public class TableAcceptanceTest extends AcceptanceTest {
	@DisplayName("주문 테이블 등록, 인원수 조정, 빈테이블로 변경 및 조회 시나리오")
	@Test
	void createOrderTableAndChangeNumberOfGuestAndChangeEmptyAndFindScenario() {
		// Scenario
		// When : 주문 테이블 등록
		ExtractableResponse<Response> tableCreatedResponse = createOrderTable(new OrderTableRequest(10, false));
		OrderTableRequest createdOrderTable = tableCreatedResponse.as(OrderTableRequest.class);
		// Then
		assertThat(tableCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(10);

		// When : 인원수 조정
		ExtractableResponse<Response> changeNumberResponse = changeNumber(createdOrderTable.getId(), new OrderTableRequest(5));
		OrderTableRequest changeNumberOrderTable = changeNumberResponse.as(OrderTableRequest.class);
		// Then
		assertThat(changeNumberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeNumberOrderTable.getNumberOfGuests()).isEqualTo(5);

		// When : 빈 테이블로 변경
		ExtractableResponse<Response> changeEmptyResponse = changeEmpty(createdOrderTable.getId(), new OrderTableRequest(true));
		OrderTableRequest changeEmptyOrderTable = changeEmptyResponse.as(OrderTableRequest.class);

		// Then
		assertThat(changeEmptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeEmptyOrderTable.isEmpty()).isEqualTo(true);

		// When : 주문 테이블 조회
		ExtractableResponse<Response> findOrderTableResponse = findOrderTable();
		// Then
		boolean idOrderTableEmpty = findOrderTableResponse.jsonPath().getList(".", OrderTableRequest.class).stream()
			.filter(orderTable -> orderTable.getId() == createdOrderTable.getId())
			.map(OrderTableRequest::isEmpty)
			.findFirst()
			.get();
		assertThat(findOrderTableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(idOrderTableEmpty).isEqualTo(true);
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
		OrderTableRequest createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTableRequest.class);

		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTableRequest(10, true));
		OrderTableRequest createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTableRequest.class);

		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroupRequest(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)));
		TableGroupRequest createdTableGroup = tableGroupCreatedResponse.as(TableGroupRequest.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When : 테이블 비움
		ExtractableResponse<Response> changeEmptyWithTableGroupAlreadyExistsResponse = changeEmpty(createdOrderTableWithFivePeople.getId(), new OrderTableRequest(true));
		// Then
		assertThat(changeEmptyWithTableGroupAlreadyExistsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 등록
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroupRequest("인기 메뉴"));
		MenuGroupRequest menuGroup = menuGroupResponse.as(MenuGroupRequest.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new ProductRequest("매운 라면", new BigDecimal(8000)));
		ProductRequest product = productResponse.as(ProductRequest.class);
		// And
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(product.getId(), 2L))));
		MenuRequest createdMenu = menuCreatedResponse.as(MenuRequest.class);
		// And
		ExtractableResponse<Response> tableCreatedResponse = createOrderTable(new OrderTableRequest(2, false));
		OrderTableRequest createdOrderTable = tableCreatedResponse.as(OrderTableRequest.class);
		// And
		ExtractableResponse<Response> orderCreatedResponse = createOrder(new OrderRequest(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest(createdMenu.getId(), 2))));
		OrderRequest createdOrder = orderCreatedResponse.as(OrderRequest.class);
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
		OrderTableRequest createdOrderTableWithOnePeople = tableWithOnePeopleCreatedResponse.as(OrderTableRequest.class);
		// When : 비어 있는 테이블에 대한 인원 변경 요청
		ExtractableResponse<Response> changeNumberWithEmptyTableResponse = changeNumber(createdOrderTableWithOnePeople.getId(), new OrderTableRequest(8));
		// Then
		assertThat(changeNumberWithEmptyTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
