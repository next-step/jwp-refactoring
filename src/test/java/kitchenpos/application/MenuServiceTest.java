package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private MenuProductDao menuProductDao;

	@Mock
	private ProductDao productDao;

	@Test
	@DisplayName("메뉴 생성 테스트")
	public void createMenuSuccessTest() {
		//given
		MenuProduct menuProduct = new MenuProduct(null, 1L, 1L, 2);
		Menu menu = new Menu(null, "후라이드+후라이드", new BigDecimal(19000), 1L, Lists.newArrayList(menuProduct));
		when(menuGroupDao.existsById(1L)).thenReturn(true);
		when(productDao.findById(1L)).thenReturn(Optional.of(new Product(1L, "후라이드", new BigDecimal(16000))));
		when(menuDao.save(menu)).thenReturn(
			new Menu(1L, "후라이드+후라이드", new BigDecimal(19000), 1L, Lists.newArrayList(menuProduct)));
		when(menuProductDao.save(menuProduct)).thenReturn(new MenuProduct(1L, 1L, 1L, 2));
		MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

		//when
		Menu save = menuService.create(menu);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(1L);
		assertThat(save.getName()).isEqualTo("후라이드+후라이드");
	}

	@Test
	@DisplayName("메뉴 가격이 0보다 작아서 실패 테스트")
	public void createMenuFailZeroLessThanFailTest() {
		//given
		MenuProduct menuProduct = new MenuProduct(null, 1L, 1L, 2);
		Menu menu = new Menu(null, "후라이드+후라이드", new BigDecimal(-1), 1L, Lists.newArrayList(menuProduct));
		MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

		//when
		//then
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메뉴의 가격은 0보다 작을 수 없습니다.");
	}

	@Test
	@DisplayName("메뉴가 메뉴그룹에 속하지 않아서 생성 실패")
	public void createMenuNotContainMenuGroupFailTest() {
		//given
		MenuProduct menuProduct = new MenuProduct(null, 1L, 1L, 2);
		Menu menu = new Menu(null, "후라이드+후라이드", new BigDecimal(19000), 1L, Lists.newArrayList(menuProduct));
		when(menuGroupDao.existsById(1L)).thenReturn(false);
		MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

		//when
		//then
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메뉴는 메뉴그룹에 속해야 합니다.");
	}

	@Test
	@DisplayName("메뉴의 가격이 상품들의 가격합보다 커서 생성 실패")
	public void createMenuPriceGreaterThanProductsPriceFailTest() {
		//given
		MenuProduct menuProduct = new MenuProduct(null, 1L, 1L, 2);
		Menu menu = new Menu(null, "후라이드+후라이드", new BigDecimal(19000), 1L, Lists.newArrayList(menuProduct));
		when(menuGroupDao.existsById(1L)).thenReturn(true);
		when(productDao.findById(1L)).thenReturn(Optional.of(new Product(1L, "후라이드", new BigDecimal(8000))));
		MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

		//when
		//then
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메뉴의 가격은 상품들의 가격합보다 작거나 같아야 합니다.");
	}

	@Test
	@DisplayName("메뉴 목록 조회 테스트")
	public void findMenuList() {
		//given
		Menu menu = new Menu(1L, "후라이드+후라이드", new BigDecimal(19000), 1L, null);
		MenuProduct menuProduct = new MenuProduct(null, 1L, 1L, 2);
		when(menuDao.findAll()).thenReturn(Lists.newArrayList(menu));
		when(menuProductDao.findAllByMenuId(1L)).thenReturn(Lists.newArrayList(menuProduct));

		//when
		MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
		List<Menu> menus = menuService.list();

		System.out.println(BigDecimal.valueOf(100).compareTo(BigDecimal.valueOf(99)));

		//then
		assertThat(menus).hasSizeGreaterThanOrEqualTo(1);
		assertThat(menus.get(0).getMenuProducts()).hasSize(1);
	}
}
