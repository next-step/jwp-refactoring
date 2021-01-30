package kitchenpos;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class TestFixtures {
	public static MenuGroup 메뉴_그룹1 = newMenuGroup(1L, "두마리메뉴");
	public static MenuGroup 메뉴_그룹2 = newMenuGroup(2L, "한마리메뉴");
	public static MenuGroup 메뉴_그룹3 = newMenuGroup(3L, "순살파닭두마리메뉴");
	public static MenuGroup 메뉴_그룹4 = newMenuGroup(4L, "신메뉴");

	public static Product 상품1 = newProduct(1L, 16000L, "후라이드");
	public static Product 상품2 = newProduct(2L, 16000L, "양념치킨");
	public static Product 상품3 = newProduct(3L, 16000L, "반반치킨");
	public static Product 상품4 = newProduct(4L, 16000L, "통구이");
	public static Product 상품5 = newProduct(5L, 17000L, "간장치킨");
	public static Product 상품6 = newProduct(6L, 17000L, "순살치킨");

	public static MenuProduct 메뉴_상품1 = newMenuProduct(상품1, 1L);
	public static MenuProduct 메뉴_상품2 = newMenuProduct(상품2, 1L);
	public static MenuProduct 메뉴_상품3 = newMenuProduct(상품3, 1L);
	public static MenuProduct 메뉴_상품4 = newMenuProduct(상품4, 1L);
	public static MenuProduct 메뉴_상품5 = newMenuProduct(상품5, 1L);
	public static MenuProduct 메뉴_상품6 = newMenuProduct(상품6, 1L);

	public static Menu 메뉴1 = newMenu(1L, "후라이드치킨", 16000L, 메뉴_그룹2, 메뉴_상품1);
	public static Menu 메뉴2 = newMenu(2L, "양념치킨", 16000L, 메뉴_그룹2, 메뉴_상품2);
	public static Menu 메뉴3 = newMenu(3L, "반반치킨", 16000L, 메뉴_그룹2, 메뉴_상품3);
	public static Menu 메뉴4 = newMenu(4L, "통구이", 16000L, 메뉴_그룹2, 메뉴_상품4);
	public static Menu 메뉴5 = newMenu(5L, "간장치킨", 17000L, 메뉴_그룹2, 메뉴_상품5);
	public static Menu 메뉴6 = newMenu(6L, "순살치킨", 17000L, 메뉴_그룹2, 메뉴_상품6);

	private static MenuGroup newMenuGroup(Long id, String name){
		return new MenuGroup.Builder().id(id).name(name).build();
	}
	private static Product newProduct(Long id, Long price, String name){
		return new Product.Builder().id(id).price(BigDecimal.valueOf(price)).name(name).build();
	}
	private static MenuProduct newMenuProduct(Product product, Long quantity){
		return new MenuProduct.Builder().product(product).quantity(quantity).build();
	}
	private static Menu newMenu(Long id, String name, Long price, MenuGroup menuGroup, MenuProduct menuProduct){
		return new Menu.Builder().id(id).name(name).price(BigDecimal.valueOf(price)).menuGroup(menuGroup).menuProducts(menuProduct).build();
	}
}
