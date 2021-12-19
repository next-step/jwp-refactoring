package kitchenpos.application;

import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@DisplayName("메뉴 단위 테스트")
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
	@InjectMocks
	private MenuService menuService;

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void create() {
		// given
		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.of(후라이드치킨_상품()));
		given(menuDao.save(any())).willReturn(후라이드후라이드_메뉴());
		given(menuProductDao.save(any())).willReturn(후라이드후라이드_메뉴_상품());
		Menu request = 후라이드후라이드_메뉴_요청(추천_메뉴_그룹().getId(), 후라이드치킨_상품().getId()).toMenu();

		// when
		Menu actual = menuService.create(request);

		// then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(request.getName()),
			() -> assertThat(actual.getPrice().compareTo(request.getPrice())).isEqualTo(0),
			() -> assertThat(actual.getMenuGroupId()).isEqualTo(request.getMenuGroupId()),
			() -> {
				MenuProduct actualMenuProduct = actual.getMenuProducts().get(0);
				MenuProduct expectedMenuProduct = request.getMenuProducts().get(0);

				assertAll(
					() -> assertThat(actualMenuProduct.getProductId()).isEqualTo(expectedMenuProduct.getProductId()),
					() -> assertThat(actualMenuProduct.getQuantity()).isEqualTo(expectedMenuProduct.getQuantity())
				);
			}
		);
	}

	@DisplayName("메뉴 이름은 빈 값이면 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnEmptyName() {
		// given
		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.of(후라이드치킨_상품()));
		given(menuDao.save(any())).willThrow(RuntimeException.class);
		Menu request = 이름없는_메뉴_요청(추천_메뉴_그룹().getId(), 후라이드치킨_상품().getId()).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴 가격이 음수이면 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnNegativePrice() {
		// given
		Menu request = 음수가격_메뉴_요청(추천_메뉴_그룹().getId(), 후라이드치킨_상품().getId()).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("특정 메뉴 그룹에 속하지 않으면 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnNotFoundMenuGroup() {
		// given
		given(menuGroupDao.existsById(any())).willReturn(false);
		Long unknownMenuGroupId = 0L;
		Menu request = 후라이드후라이드_메뉴_요청(unknownMenuGroupId, 후라이드치킨_상품().getId()).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴에 속한 상품들이 기등록된 상품이 아닐 경우 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnNotFoundProduct() {
		// given
		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.empty());
		Long unknownProductId = 0L;
		Menu request = 후라이드후라이드_메뉴_요청(추천_메뉴_그룹().getId(), unknownProductId).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴의 가격이 메뉴에 속한 상품들의 (가격 * 수량) 합을 넘은 경우 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnInvalidPrice() {
		// given
		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.of(후라이드치킨_상품()));
		Menu request = 너무비싼_메뉴_요청(추천_메뉴_그룹().getId(), 후라이드치킨_상품().getId()).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		given(menuDao.findAll()).willReturn(Collections.singletonList(후라이드후라이드_메뉴()));

		// when
		List<Menu> actual = menuService.list();

		// then
		List<Long> actualIds = actual.stream().map(Menu::getId).collect(Collectors.toList());

		assertAll(
			() -> assertThat(actual).isNotEmpty(),
			() -> assertThat(actualIds).containsAll(Collections.singletonList(후라이드후라이드_메뉴().getId())));
	}
}

