package kitchenpos;

import java.util.Arrays;

import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.Product;
import kitchenpos.table.OrderTable;

public class TestInstances {
	private static MenuGroup createMenuGroupByReflection(Long id, String name) {
		MenuGroup menuGroup = MenuGroup.builder().name(name).build();
		ReflectionTestUtils.setField(menuGroup, "id", id);
		return menuGroup;
	}

	public static MenuGroup 두마리메뉴 = createMenuGroupByReflection(1L, "두마리메뉴");
	public static MenuGroup 한마리메뉴 = createMenuGroupByReflection(2L, "한마리메뉴");
	public static MenuGroup 순살파닭두마리메뉴 = createMenuGroupByReflection(3L, "순살파닭두마리메뉴");
	public static MenuGroup 신메뉴 = createMenuGroupByReflection(4L, "신메뉴");

	private static Product createProductByReflection(Long id, String name, int price) {
		Product product = Product.builder().name(name).price(price).build();
		ReflectionTestUtils.setField(product, "id", id);
		return product;
	}

	public static Product 후라이드 = createProductByReflection(1L, "후라이드", 16000);
	public static Product 양념치킨 = createProductByReflection(2L, "양념치킨", 16000);
	public static Product 반반치킨 = createProductByReflection(3L, "반반치킨", 16000);
	public static Product 통구이 = createProductByReflection(4L, "통구이", 16000);
	public static Product 간장치킨 = createProductByReflection(5L, "간장치킨", 17000);
	public static Product 순살치킨 = createProductByReflection(6L, "순살치킨", 17000);

	private static MenuProduct createMenuProductByReflection(Long id, Product product, long quantity) {
		MenuProduct menuProduct = MenuProduct.builder().product(product).quantity(quantity).build();
		ReflectionTestUtils.setField(menuProduct, "seq", id);
		return menuProduct;
	}

	public static MenuProduct 메뉴상품1 = createMenuProductByReflection(1L, 후라이드, 1);
	public static MenuProduct 메뉴상품2 = createMenuProductByReflection(2L, 양념치킨, 1);
	public static MenuProduct 메뉴상품3 = createMenuProductByReflection(3L, 반반치킨, 1);
	public static MenuProduct 메뉴상품4 = createMenuProductByReflection(4L, 통구이, 1);
	public static MenuProduct 메뉴상품5 = createMenuProductByReflection(5L, 간장치킨, 1);
	public static MenuProduct 메뉴상품6 = createMenuProductByReflection(6L, 순살치킨, 1);

	private static Menu createMenuByReflection(Long id, String name, int price, MenuGroup menuGroup,
		MenuProduct menuProduct) {
		Menu menu = Menu.builder()
			.name(name)
			.price(price)
			.menuGroup(menuGroup)
			.build();
		menu.setMenuProducts(Arrays.asList(menuProduct));
		ReflectionTestUtils.setField(menu, "id", id);
		return menu;
	}

	public static Menu 후라이드치킨메뉴 = createMenuByReflection(1L, "후라이드치킨", 16000, 한마리메뉴, 메뉴상품1);
	public static Menu 양념치킨메뉴 = createMenuByReflection(2L, "양념치킨", 16000, 한마리메뉴, 메뉴상품2);
	public static Menu 반반치킨메뉴 = createMenuByReflection(3L, "반반치킨", 16000, 한마리메뉴, 메뉴상품3);
	public static Menu 통구이메뉴 = createMenuByReflection(4L, "통구이", 16000, 한마리메뉴, 메뉴상품4);
	public static Menu 간장치킨메뉴 = createMenuByReflection(5L, "간장치킨", 17000, 한마리메뉴, 메뉴상품5);
	public static Menu 순살치킨메뉴 = createMenuByReflection(6L, "순살치킨", 17000, 한마리메뉴, 메뉴상품6);

	private static OrderTable createOrderTableByReflection(Long id, int numberOfGuests, boolean empty) {
		OrderTable orderTable = OrderTable.builder().numberOfGuests(numberOfGuests).empty(empty).build();
		ReflectionTestUtils.setField(orderTable, "id", id);
		return orderTable;
	}

	public static OrderTable 테이블1 = createOrderTableByReflection(1L, 0, true);
	public static OrderTable 테이블2 = createOrderTableByReflection(2L, 0, true);
	public static OrderTable 테이블3 = createOrderTableByReflection(3L, 0, true);
	public static OrderTable 테이블4 = createOrderTableByReflection(4L, 0, true);
	public static OrderTable 테이블5 = createOrderTableByReflection(5L, 0, true);
	public static OrderTable 테이블6 = createOrderTableByReflection(6L, 0, true);
	public static OrderTable 테이블7 = createOrderTableByReflection(7L, 0, true);
	public static OrderTable 테이블8 = createOrderTableByReflection(8L, 0, true);

	public static void init() {
		두마리메뉴 = createMenuGroupByReflection(1L, "두마리메뉴");
		한마리메뉴 = createMenuGroupByReflection(2L, "한마리메뉴");
		순살파닭두마리메뉴 = createMenuGroupByReflection(3L, "순살파닭두마리메뉴");
		신메뉴 = createMenuGroupByReflection(4L, "신메뉴");

		후라이드 = createProductByReflection(1L, "후라이드", 16000);
		양념치킨 = createProductByReflection(2L, "양념치킨", 16000);
		반반치킨 = createProductByReflection(3L, "반반치킨", 16000);
		통구이 = createProductByReflection(4L, "통구이", 16000);
		간장치킨 = createProductByReflection(5L, "간장치킨", 17000);
		순살치킨 = createProductByReflection(6L, "순살치킨", 17000);

		메뉴상품1 = createMenuProductByReflection(1L, 후라이드, 1);
		메뉴상품2 = createMenuProductByReflection(2L, 양념치킨, 1);
		메뉴상품3 = createMenuProductByReflection(3L, 반반치킨, 1);
		메뉴상품4 = createMenuProductByReflection(4L, 통구이, 1);
		메뉴상품5 = createMenuProductByReflection(5L, 간장치킨, 1);
		메뉴상품6 = createMenuProductByReflection(6L, 순살치킨, 1);

		후라이드치킨메뉴 = createMenuByReflection(1L, "후라이드치킨", 16000, 한마리메뉴, 메뉴상품1);
		양념치킨메뉴 = createMenuByReflection(2L, "양념치킨", 16000, 한마리메뉴, 메뉴상품2);
		반반치킨메뉴 = createMenuByReflection(3L, "반반치킨", 16000, 한마리메뉴, 메뉴상품3);
		통구이메뉴 = createMenuByReflection(4L, "통구이", 16000, 한마리메뉴, 메뉴상품4);
		간장치킨메뉴 = createMenuByReflection(5L, "간장치킨", 17000, 한마리메뉴, 메뉴상품5);
		순살치킨메뉴 = createMenuByReflection(6L, "순살치킨", 17000, 한마리메뉴, 메뉴상품6);

		테이블1 = createOrderTableByReflection(1L, 0, true);
		테이블2 = createOrderTableByReflection(2L, 0, true);
		테이블3 = createOrderTableByReflection(3L, 0, true);
		테이블4 = createOrderTableByReflection(4L, 0, true);
		테이블5 = createOrderTableByReflection(5L, 0, true);
		테이블6 = createOrderTableByReflection(6L, 0, true);
		테이블7 = createOrderTableByReflection(7L, 0, true);
		테이블8 = createOrderTableByReflection(8L, 0, true);
	}
}
