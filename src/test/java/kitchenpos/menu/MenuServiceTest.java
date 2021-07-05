package kitchenpos.menu;

import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	private MenuDao menuDao;
	@Mock
	private MenuGroupDao menuGroupDao;
	@Mock
	private MenuProductDao menuProductDao;
	@Mock
	private ProductDao productDao;

	@InjectMocks
	private MenuService menuService;

	private final Long menuId = 1L;
	private final Long menuGroupId = 1L;
	private final Long menuProductSeq = 1L;
	private final Long productId = 1L;

	MenuProduct menuProduct;
	Product product;

	@BeforeEach
	void setUp() {
		menuProduct = new MenuProduct(menuProductSeq, menuId, productId, 1);
		product = new Product(productId, "빵", BigDecimal.valueOf(1_000));
	}

	@DisplayName("메뉴 생성")
	@Test
	void 메뉴_생성() {
		Menu menu = new Menu(menuId, "빵", BigDecimal.valueOf(1_000), menuGroupId, Arrays.asList(menuProduct));

		given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
		given(productDao.findById(anyLong())).willReturn(Optional.of(product));
		given(menuDao.save(menu)).willReturn(menu);
		given(menuProductDao.save(menuProduct)).willReturn(menuProduct);

		Assertions.assertThat(menuService.create(menu)).isEqualTo(menu);
	}

	@DisplayName("메뉴 생성 > 메뉴의 가격이 비어있거나 0보다 작으면 안됨.")
	@Test
	void 메뉴_생성_메뉴의_가격이_비어있거나_0보다_작으면_안됨() {
		Menu menu1 = new Menu(menuId, "빵", null, menuGroupId, Arrays.asList(menuProduct));
		Menu menu2 = new Menu(menuId, "빵", BigDecimal.valueOf(-1), menuGroupId, Arrays.asList(menuProduct));

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu1));
		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu2));
	}

	@DisplayName("메뉴 생성 > 메뉴 그룹이 존재해야 함.")
	@Test
	void 메뉴_생성_메뉴_그룹이_존재해야_함() {
		Menu menu = new Menu(menuId, "빵", BigDecimal.valueOf(1_000), menuGroupId, Arrays.asList(menuProduct));

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
	}

	@DisplayName("메뉴 생성 > 상품 목록 중 찾을 수 없는 것이 있으면 안됨.")
	@Test
	void 메뉴_생성_상품_목록_중_찾을_수_없는_것이_있으면_안됨() {
		Menu menu = new Menu(menuId, "빵", BigDecimal.valueOf(1_000), menuGroupId, Arrays.asList(menuProduct));

		given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
	}

	@DisplayName("메뉴 생성 > 가격이 상품 목록 가격의 합보다 크면 안됨.")
	@Test
	void 메뉴_생성_가격이_상품_목록_가격의_합보다_크면_안됨() {
		Menu menu = new Menu(menuId, "빵", BigDecimal.valueOf(1_200), menuGroupId, Arrays.asList(menuProduct));

		given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
		given(productDao.findById(anyLong())).willReturn(Optional.of(product));

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
	}

	@DisplayName("모든 메뉴 목록 조회")
	@Test
	void 모든_메뉴_목록_조회() {
		MenuProduct menuProduct1 = new MenuProduct(1L, 1L, 1L, 1L);
		List<MenuProduct> menuProducts1 = Arrays.asList(menuProduct1);
		Menu menu1 = new Menu(1L, "빵", BigDecimal.valueOf(1_000), menuGroupId, menuProducts1);

		MenuProduct menuProduct2 = new MenuProduct(2L, 2L, 1L, 1L);
		List<MenuProduct> menuProducts2 = Arrays.asList(menuProduct2);
		Menu menu2 = new Menu(2L, "고기", BigDecimal.valueOf(2_000), menuGroupId, menuProducts2);

		given(menuDao.findAll()).willReturn(Arrays.asList(menu1, menu2));
		given(menuProductDao.findAllByMenuId(1L)).willReturn(menuProducts1);
		given(menuProductDao.findAllByMenuId(2L)).willReturn(menuProducts2);

		Assertions.assertThat(menuService.list()).containsExactly(menu1, menu2);
	}
}
