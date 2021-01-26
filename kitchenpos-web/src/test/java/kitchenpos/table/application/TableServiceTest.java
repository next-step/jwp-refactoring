package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.orders.domain.OrderStatus;
import kitchenpos.orders.domain.Orders;
import kitchenpos.orders.repository.OrderRepository;
import kitchenpos.orders.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.TableGroupRepository;
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
	@Autowired
	private TableGroupRepository tableGroupRepository;
	@Autowired
	private OrderTableRepository orderTableRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private MenuGroupRepository menuGroupRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;

	@AfterEach
	void cleanUp() {
		menuRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		orderTableRepository.deleteAllInBatch();
		tableGroupRepository.deleteAllInBatch();
		menuGroupRepository.deleteAllInBatch();
		menuRepository.deleteAllInBatch();
	}

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
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

		OrderTable orderTable = new OrderTable(savedTableGroup, 3, true);
		OrderTable savedOrderTable = orderTableRepository.save(orderTable);

		// when
		List<OrderTableResponse> orderTables = tableService.list();
		List<Long> actualOrderTableIds = orderTables.stream()
			.map(OrderTableResponse::getId)
			.collect(Collectors.toList());
		// then
		assertThat(actualOrderTableIds).contains(savedOrderTable.getId());
	}

	@DisplayName("테이블을 비울 수 있다.")
	@Test
	void changeEmpty(){
		// given
		OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
		OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);

		OrderTable orderTable = new OrderTable( 3, true);
		OrderTable savedOrderTable = orderTableRepository.save(orderTable);

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
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

		OrderTable orderTable = new OrderTable(savedTableGroup, 3, true);
		OrderTable savedOrderTable = orderTableRepository.save(orderTable);

		MenuGroup menuGroup = new MenuGroup("마늘메뉴");
		menuGroupRepository.save(menuGroup);

		Product product = new Product("마늘닭", BigDecimal.valueOf(16000));
		productRepository.save(product);

		Orders cookingOrder = new Orders(savedOrderTable, OrderStatus.MEAL.name(), LocalDateTime.now());
		Orders savedCookingOrder = orderRepository.save(cookingOrder);

		Orders mealOrder = new Orders(savedOrderTable, OrderStatus.MEAL.name(), LocalDateTime.now());
		Orders savedMealOrder = orderRepository.save(mealOrder);

		OrderTableRequest orderTableEmptyRequest = new OrderTableRequest(true);

		// when - then
		assertThatThrownBy(() -> {
			tableService.changeEmpty(savedCookingOrder.getId(), orderTableEmptyRequest);
		}).isInstanceOf(IllegalArgumentException.class);

		assertThatThrownBy(() -> {
			tableService.changeEmpty(savedMealOrder.getId(), orderTableEmptyRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}


	@DisplayName("테이블의 손님 수를 변경할 수 있다.")
	@Test
	void changeNumberOfGuests(){
		// given
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

		OrderTable orderTable = new OrderTable(savedTableGroup, 3, false);
		OrderTable savedOrderTable = orderTableRepository.save(orderTable);

		OrderTableRequest orderTableRequest = new OrderTableRequest(5);
		// when
		OrderTable finalSavedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest);

		// then
		assertThat(finalSavedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
	}

	@DisplayName("손님은 항상 존재해야 한다.")
	@Test
	void numberOfGuestMustExist(){
		// given
		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

		OrderTable orderTable = new OrderTable(savedTableGroup, 3, false);
		OrderTable savedOrderTable = orderTableRepository.save(orderTable);

		OrderTableRequest orderTableRequest = new OrderTableRequest(-5);

		// when - then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableRequest);
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