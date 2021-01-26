package kitchenpos;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;

public class TestFixtures {

	public static MenuGroup 메뉴_그룹1 = new MenuGroup.Builder().id(1L).name("두마리메뉴").build();
	public static MenuGroup 메뉴_그룹2 = new MenuGroup.Builder().id(2L).name("한마리메뉴").build();
	public static MenuGroup 메뉴_그룹3 = new MenuGroup.Builder().id(3L).name("순살파닭두마리메뉴").build();
	public static MenuGroup 메뉴_그룹4 = new MenuGroup.Builder().id(4L).name("신메뉴").build();
	public static Product 상품1 = new Product.Builder().id(1L).price(BigDecimal.valueOf(16000L)).name("후라이드").build();
	public static Product 상품2 = new Product.Builder().id(2L).price(BigDecimal.valueOf(16000L)).name("양념치킨").build();
	public static Product 상품3 = new Product.Builder().id(3L).price(BigDecimal.valueOf(16000L)).name("반반치킨").build();
	public static Product 상품4 = new Product.Builder().id(4L).price(BigDecimal.valueOf(16000L)).name("통구이").build();
	public static Product 상품5 = new Product.Builder().id(5L).price(BigDecimal.valueOf(17000L)).name("간장치킨").build();
	public static Product 상품6 = new Product.Builder().id(6L).price(BigDecimal.valueOf(17000L)).name("순살치킨").build();
	public static MenuProduct 메뉴_상품1 = new MenuProduct.Builder().seq(1L).product(상품1).quantity(1L).build();
	public static MenuProduct 메뉴_상품2 = new MenuProduct.Builder().seq(2L).product(상품2).quantity(1L).build();
	public static MenuProduct 메뉴_상품3 = new MenuProduct.Builder().seq(3L).product(상품3).quantity(1L).build();
	public static MenuProduct 메뉴_상품4 = new MenuProduct.Builder().seq(4L).product(상품4).quantity(1L).build();
	public static MenuProduct 메뉴_상품5 = new MenuProduct.Builder().seq(5L).product(상품5).quantity(1L).build();
	public static MenuProduct 메뉴_상품6 = new MenuProduct.Builder().seq(6L).product(상품6).quantity(1L).build();
	public static Menu 메뉴1 = new Menu.Builder().id(1L)
		.name("후라이드치킨")
		.price(BigDecimal.valueOf(16000L))
		.menuGroup(메뉴_그룹2)
		.menuProducts(메뉴_상품1)
		.build();
	public static Menu 메뉴2 = new Menu.Builder().id(2L)
		.name("양념치킨")
		.price(BigDecimal.valueOf(16000L))
		.menuGroup(메뉴_그룹2)
		.menuProducts(메뉴_상품2)
		.build();
	public static Menu 메뉴3 = new Menu.Builder().id(3L)
		.name("반반치킨")
		.price(BigDecimal.valueOf(16000L))
		.menuGroup(메뉴_그룹2)
		.menuProducts(메뉴_상품3)
		.build();
	public static Menu 메뉴4 = new Menu.Builder().id(4L)
		.name("통구이")
		.price(BigDecimal.valueOf(16000L))
		.menuGroup(메뉴_그룹2)
		.menuProducts(메뉴_상품4)
		.build();
	public static Menu 메뉴5 = new Menu.Builder().id(5L)
		.name("간장치킨")
		.price(BigDecimal.valueOf(17000L))
		.menuGroup(메뉴_그룹2)
		.menuProducts(메뉴_상품5)
		.build();
	public static Menu 메뉴6 = new Menu.Builder().id(6L)
		.name("순살치킨")
		.price(BigDecimal.valueOf(17000L))
		.menuGroup(메뉴_그룹2)
		.menuProducts(메뉴_상품6)
		.build();
	public static OrderTable 주문_테이블1 = new OrderTable.Builder().id(1L).numberOfGuests(0).empty(true).build();
	public static OrderTable 주문_테이블2 = new OrderTable.Builder().id(2L).numberOfGuests(0).empty(true).build();
	public static OrderTable 주문_테이블3 = new OrderTable.Builder().id(3L).numberOfGuests(0).empty(true).build();
	public static OrderTable 주문_테이블4 = new OrderTable.Builder().id(4L).numberOfGuests(0).empty(true).build();
	public static OrderTable 주문_테이블5 = new OrderTable.Builder().id(5L).numberOfGuests(0).empty(true).build();
	public static OrderTable 주문_테이블6 = new OrderTable.Builder().id(6L).numberOfGuests(0).empty(true).build();
	public static OrderTable 주문_테이블7 = new OrderTable.Builder().id(7L).numberOfGuests(0).empty(true).build();
	public static OrderTable 주문_테이블8 = new OrderTable.Builder().id(8L).numberOfGuests(0).empty(true).build();
}

