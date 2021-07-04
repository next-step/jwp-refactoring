package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {
	@Autowired
	private TableService tableService;

	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuGroupDao menuGroupDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private OrderService orderService;

	@Test
	public void 주문_생성_성공() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		OrderLineItem orderLineItem = new OrderLineItem();
		Menu menu = 메뉴생성();
		orderLineItem.setMenuId(menu.getId());
		orderLineItem.setQuantity(1);
		orderLineItems.add(orderLineItem);

		Order order = new Order();
		order.setOrderLineItems(orderLineItems);

		OrderTable orderTable = 주문테이블생성(2, false);
		order.setOrderTableId(orderTable.getId());

		Order savedOrder = orderService.create(order);

		assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId());
		assertThat(savedOrder.getOrderLineItems())
				.extracting("orderId", "menuId", "quantity")
				.contains(tuple(savedOrder.getId(), menu.getId(), orderLineItem.getQuantity()));
	}

	@Test
	public void 주문_생성_실패_주문아이템없음() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();

		Order order = new Order();
		order.setOrderLineItems(orderLineItems);

		OrderTable orderTable = 주문테이블생성(2, true);
		order.setOrderTableId(orderTable.getId());

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 주문_생성_실패_메뉴정보없음() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setQuantity(1);
		orderLineItems.add(orderLineItem);

		Order order = new Order();
		order.setOrderLineItems(orderLineItems);

		OrderTable orderTable = 주문테이블생성(2, true);
		order.setOrderTableId(orderTable.getId());

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 주문_생성_실패_주문테이블정보없음() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		OrderLineItem orderLineItem = new OrderLineItem();
		Menu menu = 메뉴생성();
		orderLineItem.setMenuId(menu.getId());
		orderLineItem.setQuantity(1);
		orderLineItems.add(orderLineItem);

		Order order = new Order();
		order.setOrderLineItems(orderLineItems);

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 주문목록_조회() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		OrderLineItem orderLineItem = new OrderLineItem();
		Menu menu = 메뉴생성();
		orderLineItem.setMenuId(menu.getId());
		orderLineItem.setQuantity(1);
		orderLineItems.add(orderLineItem);

		Order order = new Order();
		order.setOrderLineItems(orderLineItems);

		OrderTable orderTable = 주문테이블생성(2, false);
		order.setOrderTableId(orderTable.getId());

		orderService.create(order);

		List<Order> orders = orderService.list();

		assertThat(orders)
				.extracting("orderTableId", "orderStatus")
				.contains(tuple(orderTable.getId(), OrderStatus.COOKING.name()));
	}

	@Test
	public void 주문테이블_주문없음_처리_실패_주문상태가_COOKING_이면_변경안됨() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		OrderLineItem orderLineItem = new OrderLineItem();
		Menu menu = 메뉴생성();
		orderLineItem.setMenuId(menu.getId());
		orderLineItem.setQuantity(1);
		orderLineItems.add(orderLineItem);

		Order order = new Order();
		order.setOrderLineItems(orderLineItems);

		OrderTable orderTable = 주문테이블생성(2, false);
		order.setOrderTableId(orderTable.getId());

		Order savedOrder = orderService.create(order);

		assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

		assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 주문테이블_주문없음_처리_실패_주문상태가_MEAL_이면_변경안됨() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		OrderLineItem orderLineItem = new OrderLineItem();
		Menu menu = 메뉴생성();
		orderLineItem.setMenuId(menu.getId());
		orderLineItem.setQuantity(1);
		orderLineItems.add(orderLineItem);

		Order order = new Order();
		order.setOrderLineItems(orderLineItems);

		OrderTable orderTable = 주문테이블생성(2, false);
		order.setOrderTableId(orderTable.getId());

		Order savedOrder = orderService.create(order);

		savedOrder.setOrderStatus(OrderStatus.MEAL.name());

		assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
		assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
				.isInstanceOf(IllegalArgumentException.class);
	}

	private OrderTable 주문테이블생성(Integer numberOfGuest, Boolean bool) {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(numberOfGuest);
		orderTable.setEmpty(bool);

		return tableService.create(orderTable);
	}

	private Menu 메뉴생성() {
		final List<MenuProduct> menuProducts = new ArrayList<>();

		MenuProduct menuProduct = new MenuProduct();
		Product product = 상품생성();

		menuProduct.setProductId(product.getId());
		menuProduct.setQuantity(3);

		menuProducts.add(menuProduct);

		Menu menu = new Menu();
		MenuGroup menuGroup = 메뉴그룹생성();

		menu.setMenuGroupId(menuGroup.getId());
		menu.setMenuProducts(menuProducts);
		menu.setPrice(product.getPrice().multiply(new BigDecimal(menuProduct.getQuantity())));
		menu.setName("신상치킨");

		return menuService.create(menu);
	}

	private MenuGroup 메뉴그룹생성() {
		MenuGroup menuGroup = new MenuGroup();
		String menuGroupname = "추천메뉴";
		menuGroup.setName(menuGroupname);

		return menuGroupDao.save(menuGroup);
	}

	private Product 상품생성() {
		Product product = new Product();

		String productName = "강정치킨";
		BigDecimal productPrice = new BigDecimal("17000.00");

		product.setName(productName);
		product.setPrice(productPrice);

		return productDao.save(product);
	}
}