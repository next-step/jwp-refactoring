package kitchenpos.orders.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.orders.domain.OrderLineItem;
import kitchenpos.orders.domain.OrderStatus;
import kitchenpos.orders.domain.Orders;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.utils.IntegrationTest;

/**
 * @author : byungkyu
 * @date : 2021/01/20
 * @description :
 **/
@DisplayName("주문")
class OrdersServiceTest extends IntegrationTest {
	/*@Mock
	MenuDao menuDao;
	@Mock
	OrderDao orderDao;
	@Mock
	OrderLineItemDao orderLineItemDao;
	@Mock
	OrderTableDao orderTableDao;*/

	@Autowired
	private OrderService orderService;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private MenuGroupDao menuGroupDao;
	@Autowired
	private TableGroupDao tableGroupDao;
	@Autowired
	private OrderTableDao orderTableDao;
	@Autowired
	private OrderLineItemDao orderLineItemDao;
	@Autowired
	private MenuProductDao menuProductDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private OrderDao orderDao;

	@DisplayName("주문을 등록할 수 있다.")
	@Test
	void create(){
		// given
		TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
		OrderTable savedOrderTable = orderTableDao.save(new OrderTable(savedTableGroup, 3, false));
		MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("사이드메뉴"));
		Product savedProduct = productDao.save(new Product("샐러드", BigDecimal.valueOf(9000)));
		Menu savedMenu = menuDao.save(new Menu("닭가슴살샐러드", BigDecimal.valueOf(9000), savedMenuGroup.getId()));
		MenuProduct savedMenuProduct = menuProductDao.save(new MenuProduct(savedMenu, savedProduct, 1));
		savedMenu.addMenuProduct(savedMenuProduct);

		OrderLineItem orderLineItem = new OrderLineItem(savedMenu,1);
		Orders order = new Orders(savedOrderTable.getId(), Arrays.asList(orderLineItem));

		// when
		Orders finalSavedOrder = orderService.create(order);

		// then
		assertThat(finalSavedOrder.getId()).isNotNull();

	}


	@DisplayName("주문항목이 존재하지 않으면 등록할 수 없다.")
	@Test
	void whenOrderLineItemsIsEmpty(){
		// given
		TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
		OrderTable savedOrderTable = orderTableDao.save(new OrderTable(savedTableGroup, 3, false));
		MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("사이드메뉴"));
		Product savedProduct = productDao.save(new Product("샐러드", BigDecimal.valueOf(9000)));
		Menu savedMenu = menuDao.save(new Menu("닭가슴살샐러드", BigDecimal.valueOf(9000), savedMenuGroup.getId()));
		MenuProduct savedMenuProduct = menuProductDao.save(new MenuProduct(savedMenu, savedProduct, 1));
		savedMenu.addMenuProduct(savedMenuProduct);

		Orders order = new Orders(savedOrderTable.getId(), Collections.emptyList());
		// when - then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);

	}

	@DisplayName("주문항목과 메뉴의 항목들이 일치해야 주문할 수 있다.")
	@Test
	void orderLineItemsSizeMustExistInMenu(){
		// given
		TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
		OrderTable savedOrderTable = orderTableDao.save(new OrderTable(savedTableGroup, 3, false));
		MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("사이드메뉴"));
		Product savedProduct = productDao.save(new Product("샐러드", BigDecimal.valueOf(9000)));
		Menu savedMenu = menuDao.save(new Menu("닭가슴살샐러드", BigDecimal.valueOf(9000), savedMenuGroup.getId()));
		MenuProduct savedMenuProduct = menuProductDao.save(new MenuProduct(savedMenu, savedProduct, 1));
		savedMenu.addMenuProduct(savedMenuProduct);

		OrderLineItem orderLineItem = new OrderLineItem(savedMenu,1);
		OrderLineItem inValidOrderLineItem = new OrderLineItem(savedMenu,2);
		Orders order = new Orders(savedOrderTable.getId(), Arrays.asList(orderLineItem,inValidOrderLineItem));

		// when - then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);
	}


	@DisplayName("빈 테이블은 주문을 할 수 없다.")
	@Test
	void empTyOrderTableCannotOrder(){
		// given
		TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
		OrderTable savedOrderTable = orderTableDao.save(new OrderTable(savedTableGroup, 3, true));
		MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("사이드메뉴"));
		Product savedProduct = productDao.save(new Product("샐러드", BigDecimal.valueOf(9000)));
		Menu savedMenu = menuDao.save(new Menu("닭가슴살샐러드", BigDecimal.valueOf(9000), savedMenuGroup.getId()));
		MenuProduct savedMenuProduct = menuProductDao.save(new MenuProduct(savedMenu, savedProduct, 1));
		savedMenu.addMenuProduct(savedMenuProduct);

		OrderLineItem orderLineItem = new OrderLineItem(savedMenu,1);
		Orders order = new Orders(savedOrderTable.getId(), Arrays.asList(orderLineItem));

		// when - then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);
	}


	@DisplayName("주문의 목록을 조회할 수 있다.")
	@Test
	void list(){
		// when
		List<Orders> orders = orderService.list();

		List<Orders> actualOrders = orderDao.findAll();

		List<Long> actualOrderIds = actualOrders.stream()
			.map(Orders::getId)
			.collect(Collectors.toList());

		// then
		assertThat(orders).isNotEmpty();

		assertThat(actualOrderIds).containsAll(actualOrderIds);
	}

	@DisplayName("주문의 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatus() {

		TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
		OrderTable savedOrderTable = orderTableDao.save(new OrderTable(savedTableGroup, 3, false));
		MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("사이드메뉴"));
		Product savedProduct = productDao.save(new Product("샐러드", BigDecimal.valueOf(9000)));
		Menu savedMenu = menuDao.save(new Menu("닭가슴살샐러드", BigDecimal.valueOf(9000), savedMenuGroup.getId()));
		MenuProduct savedMenuProduct = menuProductDao.save(new MenuProduct(savedMenu, savedProduct, 1));
		savedMenu.addMenuProduct(savedMenuProduct);

		OrderLineItem orderLineItem = new OrderLineItem(savedMenu,1);
		Orders order = new Orders(savedOrderTable.getId(), Arrays.asList(orderLineItem));

		Orders finalSavedOrder = orderService.create(order);

		Orders changeOrder = new Orders(OrderStatus.COMPLETION.name());
		// when
		Orders changedOrder = orderService.changeOrderStatus(finalSavedOrder.getId(), changeOrder);

		// then
		assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
	}


	@DisplayName("완료된 주문은 상태를 변경할 수 없다.")
	@Test
	void completedOrderCannotChange(){
		// given
		TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
		OrderTable savedOrderTable = orderTableDao.save(new OrderTable(savedTableGroup, 3, false));
		MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("사이드메뉴"));
		Product savedProduct = productDao.save(new Product("샐러드", BigDecimal.valueOf(9000)));
		Menu savedMenu = menuDao.save(new Menu("닭가슴살샐러드", BigDecimal.valueOf(9000), savedMenuGroup.getId()));
		MenuProduct savedMenuProduct = menuProductDao.save(new MenuProduct(savedMenu, savedProduct, 1));
		savedMenu.addMenuProduct(savedMenuProduct);

		OrderLineItem orderLineItem = new OrderLineItem(savedMenu,1);
		Orders order = new Orders(savedOrderTable.getId(), Arrays.asList(orderLineItem));

		Orders finalSavedOrder = orderService.create(order);

		Orders changeOrder = new Orders(OrderStatus.COMPLETION.name());
		orderService.changeOrderStatus(finalSavedOrder.getId(), changeOrder);

		// when - then
		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(finalSavedOrder.getId(), changeOrder);
		}).isInstanceOf(IllegalArgumentException.class);
	}
}