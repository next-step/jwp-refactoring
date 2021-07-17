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

public class TableAcceptanceTest extends AcceptanceTest {
	@DisplayName("주문 테이블 등록, 인원수 조정, 빈테이블로 변경 및 조회 시나리오")
	@Test
	void createOrderTableAndChangeNumberOfGuestAndChangeEmptyAndFindScenario() {
		// Scenario
		// When : 주문 테이블 등록
		ExtractableResponse<Response> tableCreatedResponse = createOrderTable(new OrderTable(10, false));
		OrderTable createdOrderTable = tableCreatedResponse.as(OrderTable.class);
		// Then
		assertThat(tableCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(10);

		// When : 인원수 조정
		ExtractableResponse<Response> changeNumberResponse = changeNumber(createdOrderTable.getId(), new OrderTable(5));
		OrderTable changeNumberOrderTable = changeNumberResponse.as(OrderTable.class);
		// Then
		assertThat(changeNumberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeNumberOrderTable.getNumberOfGuests()).isEqualTo(5);

		// When : 빈 테이블로 변경
		ExtractableResponse<Response> changeEmptyResponse = changeEmpty(createdOrderTable.getId(), new OrderTable(true));
		OrderTable changeEmptyOrderTable = changeEmptyResponse.as(OrderTable.class);

		// Then
		assertThat(changeEmptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeEmptyOrderTable.isEmpty()).isEqualTo(true);

		// When : 주문 테이블 조회
		ExtractableResponse<Response> findOrderTableResponse = findOrderTable();
		// Then
		boolean idOrderTableEmpty = findOrderTableResponse.jsonPath().getList(".", OrderTable.class).stream()
			.filter(orderTable -> orderTable.getId() == createdOrderTable.getId())
			.map(OrderTable::isEmpty)
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
		ExtractableResponse<Response> changeNumberWithNotExistsOrderTableResponse = changeEmpty(0L, new OrderTable(true));
		// Then
		assertThat(changeNumberWithNotExistsOrderTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 테이블 등록
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = createOrderTable(new OrderTable(5, true));
		OrderTable createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = createOrderTable(new OrderTable(10, true));
		OrderTable createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableGroupCreatedResponse = createTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)));
		TableGroup createdTableGroup = tableGroupCreatedResponse.as(TableGroup.class);
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When : 테이블 비움
		ExtractableResponse<Response> changeEmptyWithTableGroupAlreadyExistsResponse = changeEmpty(createdOrderTableWithFivePeople.getId(), new OrderTable(true));
		// Then
		assertThat(changeEmptyWithTableGroupAlreadyExistsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 등록
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
		// And
		ExtractableResponse<Response> orderCreatedResponse = createOrder(new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem(createdMenu.getId(), 2))));
		Order createdOrder = orderCreatedResponse.as(Order.class);
		assertThat(orderCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When : 빈 테이블로 변경
		ExtractableResponse<Response> changeEmptyWithCookingOrderResponse = changeEmpty(createdOrderTable.getId(), new OrderTable(true));
		// Then
		assertThat(changeEmptyWithCookingOrderResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 음수로 테이블 인원 변경
		ExtractableResponse<Response> changeNumberWithMinusNumberResponse = changeNumber(createdOrderTable.getId(), new OrderTable(-1));
		// Then
		assertThat(changeNumberWithMinusNumberResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When : 미존재 테이블에 대한 변경 요청
		ExtractableResponse<Response> changeNumberWithNotExistsTableResponse = changeNumber(0L, new OrderTable(8));
		// Then
		assertThat(changeNumberWithNotExistsTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// Given : 주문 테이블 등록
		ExtractableResponse<Response> tableWithOnePeopleCreatedResponse =  createOrderTable(new OrderTable(1, true));
		OrderTable createdOrderTableWithOnePeople = tableWithOnePeopleCreatedResponse.as(OrderTable.class);
		// When : 비어 있는 테이블에 대한 인원 변경 요청
		ExtractableResponse<Response> changeNumberWithEmptyTableResponse = changeNumber(createdOrderTableWithOnePeople.getId(), new OrderTable(8));
		// Then
		assertThat(changeNumberWithEmptyTableResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
