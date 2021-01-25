package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.utils.IntegrationTest;

/**
 * @author : byungkyu
 * @date : 2021/01/20
 * @description :
 **/
@DisplayName("테이블")
class TableServiceTest extends IntegrationTest {

	@Autowired
	private TableService tableService;

	@DisplayName("테이블을 생성할 수 있다.")
	@Test
	void create(){
		// given
		OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
		// when
		OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);

		// then
		assertThat(createdOrderTable.getId()).isNotNull();
	}

	@DisplayName("테이블 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given

		OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
		OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);

		// when
		List<OrderTableResponse> orderTables = tableService.list();
		List<Long> actualOrderTableIds = orderTables.stream()
			.map(OrderTableResponse::getId)
			.collect(Collectors.toList());
		// then
		assertThat(actualOrderTableIds).contains(createdOrderTable.getId());
	}

	@DisplayName("테이블을 비울 수 있다.")
	@Test
	void changeEmpty(){
		// given
		OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
		OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);

		OrderTableRequest orderTableEmptyRequest = new OrderTableRequest(true);
		// when
		OrderTable finalSavedOrderTable = tableService.changeEmpty(createdOrderTable.getId(), orderTableEmptyRequest);

		// then
		assertThat(finalSavedOrderTable.isEmpty()).isTrue();
	}


	@DisplayName("존재하지 않는 테이블은 비울 수 없다.")
	@Test
	void notExistOrderTableCannotChangeEmpty(){
		// given
		Long invalidOrderTableId = 1000L;
		OrderTableRequest orderTableEmptyRequest = new OrderTableRequest(true);
		// when - then
		assertThatThrownBy(() -> {
			tableService.changeEmpty(invalidOrderTableId, orderTableEmptyRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}


	@DisplayName("요리중이거나 식사중인 테이블이 있는 경우 테이블을 비울 수 없다.")
	@Test
	void mealOrCookingOrderTableCannotNotChangeEmpty(){
		// given
		Long cookingOrderId = 6L; //V2_Insert_default_data.sql
		Long mealOrderId = 7L; //V2_Insert_default_data.sql

		OrderTableRequest orderTableEmptyRequest = new OrderTableRequest(true);

		// when - then
		assertThatThrownBy(() -> {
			tableService.changeEmpty(cookingOrderId, orderTableEmptyRequest);
		}).isInstanceOf(IllegalArgumentException.class);

		assertThatThrownBy(() -> {
			tableService.changeEmpty(mealOrderId, orderTableEmptyRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}


	@DisplayName("테이블의 손님 수를 변경할 수 있다.")
	@Test
	void changeNumberOfGuests(){
		// given
		Long mealAndThreeNumberOfGuestOrderId = 3L; //V2_Insert_default_data.sql

		OrderTableRequest orderTableRequest = new OrderTableRequest(5);
		// when
		OrderTable finalSavedOrderTable = tableService.changeNumberOfGuests(mealAndThreeNumberOfGuestOrderId, orderTableRequest);

		// then
		assertThat(finalSavedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
	}

	@DisplayName("손님은 항상 존재해야 한다.")
	@Test
	void numberOfGuestMustExist(){
		// given
		Long mealAndThreeNumberOfGuestOrderId = 3L; //V2_Insert_default_data.sql
		OrderTableRequest orderTableRequest = new OrderTableRequest(-5);

		// when - then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(mealAndThreeNumberOfGuestOrderId, orderTableRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블은 항상 존재해야 한다.")
	@Test
	void orderTableMustExist(){
		Long orderTableId = 10000L;
		OrderTableRequest orderTableRequest = new OrderTableRequest(5);

		// when - then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(orderTableId, orderTableRequest);
		}).isInstanceOf(IllegalArgumentException.class);

	}

}