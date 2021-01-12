package kitchenpos.application;

import kitchenpos.MockitoTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

class MenuServiceTest extends MockitoTest {

	@InjectMocks
	private MenuService menuService;

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private MenuProductDao menuProductDao;

	@Mock
	private ProductDao productDao;

	private static final int 콜라가격 = 1000;
	private static final int 감튀가격 = 2000;
	private static final int 버거가격 = 7000;
	private Product 콜라;
	private Product 감튀;
	private Product 버거;
	private MenuProduct 메뉴_콜라;
	private MenuProduct 메뉴_감튀;
	private MenuProduct 메뉴_버거;

	@BeforeEach
	void setUp() {
		콜라 = MockFixture.product(1L, "콜라", 콜라가격);
		감튀 = MockFixture.product(2L, "감자튀김", 감튀가격);
		버거 = MockFixture.product(3L, "콰트로치즈와퍼", 버거가격);
		메뉴_콜라 = MockFixture.menuProduct(1L, 콜라.getId(), 1L, 1L);
		메뉴_감튀 = MockFixture.menuProduct(1L, 감튀.getId(), 2L, 1L);
		메뉴_버거 = MockFixture.menuProduct(1L, 버거.getId(), 3L, 1L);
		MockFixture.findByIdWillReturn(productDao, 메뉴_콜라, 콜라);
		MockFixture.findByIdWillReturn(productDao, 메뉴_감튀, 감튀);
		MockFixture.findByIdWillReturn(productDao, 메뉴_버거, 버거);
	}

	@DisplayName("메뉴를 생성한다.")
	@Test
	void create() {
		// given
		Menu 콰트로치즈와퍼세트 = MockFixture.menuForCreate("콰트로치즈와퍼세트",
				콜라가격 + 감튀가격 + 버거가격 - 2000L,
				null,
				메뉴_콜라, 메뉴_감튀, 메뉴_버거);
		given(menuDao.save(콰트로치즈와퍼세트)).willReturn(콰트로치즈와퍼세트);
		given(menuGroupDao.existsById(any())).willReturn(true);

		// when
		menuService.create(콰트로치즈와퍼세트);

		// then
		verify(menuDao).save(콰트로치즈와퍼세트);
	}

	@DisplayName("메뉴 가격이 메뉴 포함된 모든 상품의 가격합보다 높을때 예외발생.")
	@Test
	void create_ProductPriceGreaterThanMenuPrice() {
		// given
		final long 잘못된_추가가격 = 1000;
		Menu 콰트로치즈와퍼세트 = MockFixture.menuForCreate("콰트로치즈와퍼세트",
				콜라가격 + 감튀가격 + 버거가격 + 잘못된_추가가격,
				null,
				메뉴_콜라, 메뉴_감튀, 메뉴_버거);
		given(menuGroupDao.existsById(any())).willReturn(true);

		// when then
		assertThatThrownBy(() -> menuService.create(콰트로치즈와퍼세트))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 생성시 가격이 음수일경우 예외 발생.")
	@ParameterizedTest
	@ValueSource(longs = {-1, -9999})
	@NullSource
	void create_PriceWrong(Long price) {
		Menu menu = MockFixture.menuForCreate("아무메뉴", price, null);

		assertThatThrownBy(() -> menuService.create(menu))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 리스트를 반환한다.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 999})
	void list(int size) {
		// given
		List<Menu> anyMenus = MockFixture.anyMenus(size);
		given(menuDao.findAll()).willReturn(anyMenus);

		// when then
		assertThat(menuService.list()).hasSize(size);
	}
}
