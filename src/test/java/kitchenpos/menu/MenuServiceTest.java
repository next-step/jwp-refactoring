package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Price;
import kitchenpos.menugroup.MenuGroupServiceTest;
import kitchenpos.product.ProductServiceTest;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private MenuService menuService;

	MenuGroup 치킨;
	Menu 양념반_후라이드반;
	Product 양념치킨;
	Product 후라이드치킨;
	MenuProduct 양념_반_치킨;
	MenuProduct 후라이드_반_치킨;

	@BeforeEach
	void setUp() {
		치킨 = MenuGroupServiceTest.메뉴그룹_생성(1L, "치킨");
		양념치킨 = ProductServiceTest.상품생성(1L, new BigDecimal(10000), "양념 치킨");
		후라이드치킨 = ProductServiceTest.상품생성(2L, new BigDecimal(9000), "후라이드 치킨");
		양념_반_치킨 = 메뉴상품생성(1L, 양념치킨, 1);
		후라이드_반_치킨 = 메뉴상품생성(2L, 후라이드치킨, 1);
		양념반_후라이드반 = 메뉴생성(1L, "양념 반 후라이드 반", new Price(new BigDecimal(19000)), 치킨, new MenuProducts(Arrays.asList(양념_반_치킨, 후라이드_반_치킨)));
	}

	@DisplayName("메뉴를 생성한다")
	@Test
	void 메뉴를_생성한다() {
		// given(menuGroupDao.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(true);
		// given(productDao.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
		// given(productDao.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
		// given(menuProductDao.save(양념_반_치킨)).willReturn(양념_반_치킨);
		// given(menuProductDao.save(후라이드_반_치킨)).willReturn(후라이드_반_치킨);
		given(menuRepository.save(양념반_후라이드반)).willReturn(양념반_후라이드반);

		Menu created = menuService.create(양념반_후라이드반);

		메뉴_생성_확인(created, 양념반_후라이드반);
	}

	@DisplayName("메뉴 생성 - 메뉴의 가격이 Null일 경우 에러 발생")
	@Test
	void 메뉴_생성_메뉴의_가격이_Null_일_경우_에러_발생() {
		양념반_후라이드반.setPrice(null);

		assertThatThrownBy(() -> {
			menuService.create(양념반_후라이드반);
		}).isInstanceOf(IllegalArgumentException.class);

	}

	@DisplayName("메뉴 생성 - 메뉴의 가격은 0보다 커야 한다.")
	@Test
	void 메뉴_생성_메뉴의_가격은_0보다_커야_한다() {
		양념반_후라이드반.setPrice(new Price(new BigDecimal(0)));

		assertThatThrownBy(() -> {
			menuService.create(양념반_후라이드반);
		}).isInstanceOf(IllegalArgumentException.class);

	}

	@DisplayName("메뉴 생성 - 메뉴의 메뉴 그룹이 존재하지 않으면 에러 발생.")
	@Test
	void 메뉴_생성_메뉴의_메뉴_그룹이_존재하지_않으면_에러_발생() {
		// given(menuGroupDao.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(false);

		assertThatThrownBy(() -> {
			menuService.create(양념반_후라이드반);
		}).isInstanceOf(IllegalArgumentException.class);

	}

	@DisplayName("메뉴 생성 - 메뉴 가격보다 메뉴의 메뉴 상품 리스트의 가격의 합이 크면 안된다.")
	@Test
	void 메뉴_생성_메뉴_가격보다_메뉴의_메뉴_상품_리스트의_가격의_합이_크면_안된다() {
		양념반_후라이드반.setPrice(new Price(new BigDecimal(200000)));

		assertThatThrownBy(() -> {
			menuService.create(양념반_후라이드반);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 생성 - 메뉴 리스트를 조회한다")
	@Test
	void 메뉴_리스트를_조회한다() {
		given(menuRepository.findAll()).willReturn(Arrays.asList(양념반_후라이드반));
		// given(menuProductDao.findAllByMenuId(1L)).willReturn(Arrays.asList(양념_반_치킨, 후라이드_반_치킨));

		List<Menu> selectedMenus = menuService.list();

		메뉴_리스트_조회_확인(selectedMenus, Arrays.asList(양념반_후라이드반));
	}

	private void 메뉴_리스트_조회_확인(List<Menu> selectedMenus, List<Menu> expected) {
		assertThat(selectedMenus).containsAll(expected);
	}

	private void 메뉴_생성_확인(Menu created, Menu expected) {
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.getMenuGroup().getId()).isEqualTo(expected.getMenuGroup().getId());
		assertThat(created.getMenuProducts().getSumMenuProductPrice()).isEqualTo(
			expected.getMenuProducts().getSumMenuProductPrice());
		assertThat(created.getPrice()).isEqualTo(expected.getPrice());
		assertThat(created.getName()).isEqualTo(expected.getName());
	}

	public static Menu 메뉴생성(Long id, String name, Price price, MenuGroup menuGroupId, MenuProducts menuProducts) {
		Menu 메뉴 = new Menu(id, name, price, menuGroupId, menuProducts);
		return 메뉴;
	}

	public static MenuProduct 메뉴상품생성(Long seq, Product product, int quantity) {
		MenuProduct menuProduct = new MenuProduct(seq, product, quantity);
		return menuProduct;
	}

}
