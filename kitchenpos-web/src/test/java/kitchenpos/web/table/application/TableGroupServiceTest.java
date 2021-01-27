package kitchenpos.web.table.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu.domain.MenuGroup;
import kitchenpos.domain.menu.domain.MenuProduct;
import kitchenpos.web.menu.repository.MenuGroupRepository;
import kitchenpos.web.menu.repository.MenuProductRepository;
import kitchenpos.web.menu.repository.MenuRepository;
import kitchenpos.web.orders.application.OrderService;
import kitchenpos.domain.orders.domain.OrderStatus;
import kitchenpos.web.orders.dto.OrderLineItemRequest;
import kitchenpos.web.orders.dto.OrderRequest;
import kitchenpos.web.orders.dto.OrderResponse;
import kitchenpos.web.orders.repository.OrderLineItemRepository;
import kitchenpos.web.orders.repository.OrderRepository;
import kitchenpos.web.orders.repository.OrderTableRepository;
import kitchenpos.domain.product.domain.Product;
import kitchenpos.web.product.repository.ProductRepository;
import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table.domain.TableGroup;
import kitchenpos.web.table.dto.OrderTableResponse;
import kitchenpos.web.table.dto.TableGroupRequest;
import kitchenpos.web.table.dto.TableGroupResponse;
import kitchenpos.web.table.repository.TableGroupRepository;
import kitchenpos.utils.IntegrationTest;

/**
 * @author : byungkyu
 * @date : 2021/01/20
 * @description :
 **/
@DisplayName("단체")
class TableGroupServiceTest extends IntegrationTest {

	@Autowired
	private TableGroupService tableGroupService;
	@Autowired
	private OrderTableRepository orderTableRepository;
	@Autowired
	private MenuGroupRepository menuGroupRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private MenuProductRepository menuProductRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private TableGroupRepository tableGroupRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderLineItemRepository orderLineItemRepository;

	@AfterEach
	void cleanUp() {
		menuProductRepository.deleteAllInBatch();
		orderLineItemRepository.deleteAllInBatch();
		menuRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		orderTableRepository.deleteAllInBatch();
		tableGroupRepository.deleteAllInBatch();
		menuGroupRepository.deleteAllInBatch();
		menuRepository.deleteAllInBatch();
		menuProductRepository.deleteAllInBatch();
	}

	@DisplayName("단체를 지정할 수 있다.")
	@Test
	void create() {
		// given
		OrderTable orderTable1 = new OrderTable(2, true);
		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);

		OrderTable orderTable2 = new OrderTable(2, true);
		OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

		// when
		TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
		TableGroupResponse finalSavedTableGroup = tableGroupService.create(request);

		// then
		assertThat(finalSavedTableGroup.getId()).isNotNull();

		List<Long> actualOrderTableIds = finalSavedTableGroup.getOrderTables().stream()
			.map(OrderTableResponse::getId)
			.collect(Collectors.toList());
		assertThat(actualOrderTableIds).containsExactly(orderTable1.getId(), orderTable2.getId());
	}

	@DisplayName("테이블은 2개 이상일 경우에만 지정할 수 있다.")
	@Test
	void tableCountMustOverTwice() {
		// given
		OrderTable orderTable = new OrderTable(0, true);
		OrderTable savedOrderTable = orderTableRepository.save(orderTable);

		TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable.getId()));
		// when
		assertThatThrownBy(() -> {
			tableGroupService.create(request);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문한 테이블들이 실제로 존재하지 않는 경우 단체로 지정할 수 없다.")
	@Test
	void requestedOrderTableMustExist() {
		// when - then
		TableGroupRequest request = new TableGroupRequest(Arrays.asList(22222L, 333333L));
		assertThatThrownBy(() -> {
			tableGroupService.create(request);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요청된 테이블들이 빈 테이블이 아니면 단체로 지정할 수 없다.")
	@Test
	void requestedOrderTableMustEmpty() {
		// given
		OrderTable orderTable1 = new OrderTable(0, false);
		OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);

		OrderTable orderTable2 = new OrderTable(0, false);
		OrderTable saveOrderTable2 = orderTableRepository.save(orderTable2);

		// when

		TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.getId(), saveOrderTable2.getId()));
		// when - then
		assertThatThrownBy(() -> {
			tableGroupService.create(request);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체를 해제할 수 있다.")
	@Test
	void ungroup(){
		// given
		TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
		OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(savedTableGroup, 3, false));

		MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("사이드메뉴"));
		Product savedProduct = productRepository.save(new Product("샐러드", BigDecimal.valueOf(9000)));
		Menu savedMenu = menuRepository.save(new Menu("닭가슴살샐러드", BigDecimal.valueOf(9000), savedMenuGroup.getId()));
		MenuProduct savedMenuProduct = menuProductRepository.save(new MenuProduct(savedMenu, savedProduct, 1));
		//savedMenu.addMenuProduct(savedMenuProduct);

		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 1L);
		OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), Arrays.asList(orderLineItemRequest));

		// when
		OrderResponse finalSavedOrder = orderService.create(orderRequest);

		OrderRequest changeOrder = new OrderRequest(OrderStatus.COMPLETION.name());
		orderService.changeOrderStatus(finalSavedOrder.getId(), changeOrder);

		// when
		tableGroupService.ungroup(savedTableGroup.getId());

		// then
		List<OrderTable> actualOrderTables = orderTableRepository.findAllByTableGroupId(savedTableGroup.getId());

		List<Long> actualOrderTableIds = actualOrderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		assertThat(actualOrderTableIds).doesNotContain(savedTableGroup.getId());

	}
	@DisplayName("테이블중 요리중이거나 식사중인 상태인 경우 단체를 해제할 수 없다.")
	@Test
	void cookingOrMealCannotCreateTableGroup() {
		// given
		TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
		OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(savedTableGroup, 3, false));

		MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("사이드메뉴"));
		Product savedProduct = productRepository.save(new Product("샐러드", BigDecimal.valueOf(9000)));
		Menu savedMenu = menuRepository.save(new Menu("닭가슴살샐러드", BigDecimal.valueOf(9000), savedMenuGroup.getId()));
		MenuProduct savedMenuProduct = menuProductRepository.save(new MenuProduct(savedMenu, savedProduct, 1));
		//savedMenu.addMenuProduct(savedMenuProduct);

		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 1L);
		OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), Arrays.asList(orderLineItemRequest));

		// when
		OrderResponse finalSavedOrder = orderService.create(orderRequest);

		OrderRequest changeOrder = new OrderRequest(OrderStatus.MEAL.name());
		orderService.changeOrderStatus(finalSavedOrder.getId(), changeOrder);

		// when - then
		assertThatThrownBy(() -> {
			tableGroupService.ungroup(savedTableGroup.getId());
		}).isInstanceOf(IllegalArgumentException.class);
	}
}