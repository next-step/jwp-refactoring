package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.utils.IntegrationTest;

/**
 * @author : byungkyu
 * @date : 2021/01/19
 * @description :
 **/
@DisplayName("메뉴")
class MenuServiceTest extends IntegrationTest {

	@Autowired
	private MenuService menuService;
	@Autowired
	MenuDao menuDao;
	@Autowired
	MenuGroupDao menuGroupDao;
	@Autowired
	MenuProductDao menuProductDao;
	@Autowired
	ProductDao productDao;

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void create() {
		// given
		MenuGroup menuGroup = new MenuGroup("마늘메뉴");
		MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
		Product product = new Product("마늘닭", BigDecimal.valueOf(16000));
		Product savedProduct = productDao.save(product);

		MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1);
		List<MenuProduct> menuProducts = Arrays.asList(menuProduct);
		MenuRequest menuRequest = new MenuRequest("마늘치킨",  BigDecimal.valueOf(16000), savedMenuGroup.getId(), menuProducts);

		// when
		MenuResponse menuResponse = menuService.create(menuRequest);

		// then
		assertThat(menuResponse.getId()).isNotNull();
		assertThat(menuResponse.getName()).isEqualTo(menuRequest.getName());
		assertThat(menuResponse.getPrice()).isEqualByComparingTo(menuRequest.getPrice());
		assertThat(menuResponse.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId());
	}


	@DisplayName("메뉴의 가격은 0원 이하일 수 없다")
	@Test
	void priceMustOverZero() {
		// given
		MenuGroup menuGroup = new MenuGroup("마늘메뉴");
		MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
		Product product = new Product("마늘닭", BigDecimal.valueOf(16000));
		Product savedProduct = productDao.save(product);

		MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1);
		List<MenuProduct> menuProducts = Arrays.asList(menuProduct);
		MenuRequest menuRequest = new MenuRequest("마늘치킨",  BigDecimal.valueOf(-16000), savedMenuGroup.getId(), menuProducts);

		// when - then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(IllegalArgumentException.class);

	}

	@DisplayName("메뉴는 존재하는 메뉴그룹에만 등록할 수 있다.")
	@Test
	void menuGroupMustExist() {
		// given
		Long invalidMenuGroupId = 999999L;
		Product product = new Product("마늘닭", BigDecimal.valueOf(16000));
		Product savedProduct = productDao.save(product);

		MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1);
		List<MenuProduct> menuProducts = Arrays.asList(menuProduct);
		MenuRequest menuRequest = new MenuRequest("마늘치킨",  BigDecimal.valueOf(-16000), invalidMenuGroupId, menuProducts);

		// when - then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 가격은 메뉴내 상품들의 가격 합보다 클 수 없다.")
	@Test
	void menuPriceCannotOverProduct() {
		// given
		MenuGroup menuGroup = new MenuGroup("마늘메뉴");
		MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
		Product product = new Product("마늘닭", BigDecimal.valueOf(16000));
		Product savedProduct = productDao.save(product);

		MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1);
		List<MenuProduct> menuProducts = Arrays.asList(menuProduct);
		MenuRequest menuRequest = new MenuRequest("마늘치킨",  BigDecimal.valueOf(50000000), savedMenuGroup.getId(), menuProducts);

		// when - then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		List<Menu> findAll = menuDao.findAll();

		// when
		List<MenuResponse> actualMenus = menuService.list();

		List<Long> expectedIds = findAll.stream()
			.map(menu -> menu.getId())
			.collect(Collectors.toList());

		List<Long> actualMenuIds = actualMenus.stream()
			.map(menu -> menu.getId())
			.collect(Collectors.toList());

		// then
		assertThat(actualMenuIds).containsAll(expectedIds);
	}

}