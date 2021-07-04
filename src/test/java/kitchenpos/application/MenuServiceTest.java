package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MenuServiceTest {
	@Autowired
	private MenuDao menuDao;

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuGroupDao menuGroupDao;

	@Autowired
	private MenuProductDao menuProductDao;

	@Autowired
	private ProductDao productDao;

	@Test
	public void 메뉴등록_성공() {
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
		menu.setName("신상치킨x3");

		Menu savedMenu = menuService.create(menu);
		List<MenuProduct> menuProductList = savedMenu.getMenuProducts();

		assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId());
		assertThat(menuProductList.size()).isEqualTo(1);
		assertThat(menuProductList).extracting("menuId")
				.containsOnly(savedMenu.getId());
		assertThat(savedMenu.getName()).isEqualTo("신상치킨x3");
		assertThat(savedMenu.getPrice()).isEqualByComparingTo(product.getPrice().multiply(new BigDecimal(menuProduct.getQuantity())));
	}

	@Test
	public void 메뉴등록_실패_가격은_Null이면안됨() {
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
		menu.setPrice(null);
		menu.setName("신상치킨x3");

		assertThatThrownBy(() -> menuService.create(menu))
		.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 메뉴등록_실패_가격은_0이면안됨() {
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
		menu.setPrice(new BigDecimal("-1"));
		menu.setName("신상치킨x3");

		assertThatThrownBy(() -> menuService.create(menu))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 메뉴등록_실패_없는메뉴그룹() {
		final List<MenuProduct> menuProducts = new ArrayList<>();

		MenuProduct menuProduct = new MenuProduct();
		Product product = 상품생성();

		menuProduct.setProductId(product.getId());
		menuProduct.setQuantity(3);

		menuProducts.add(menuProduct);

		Menu menu = new Menu();

		menu.setMenuGroupId(9999L);
		menu.setMenuProducts(menuProducts);
		menu.setPrice(product.getPrice().multiply(new BigDecimal(menuProduct.getQuantity())));
		menu.setName("신상치킨x3");

		assertThatThrownBy(() -> menuService.create(menu))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 메뉴등록_실패_없는상품정보() {
		final List<MenuProduct> menuProducts = new ArrayList<>();

		MenuProduct menuProduct = new MenuProduct();

		menuProduct.setProductId(9999L);
		menuProduct.setQuantity(3);

		menuProducts.add(menuProduct);

		Menu menu = new Menu();
		MenuGroup menuGroup = 메뉴그룹생성();

		menu.setMenuGroupId(menuGroup.getId());
		menu.setMenuProducts(menuProducts);
		menu.setName("신상치킨x3");

		assertThatThrownBy(() -> menuService.create(menu))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 메뉴등록_실패_상품리스트_가격_총합은_메뉴가격과_같아야한다() {
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
		menu.setPrice(new BigDecimal("9999999999"));
		menu.setName("신상치킨x3");

		assertThatThrownBy(() -> menuService.create(menu))
				.isInstanceOf(IllegalArgumentException.class);
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