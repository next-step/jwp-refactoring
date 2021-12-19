package kitchenpos.application;

import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 통합 테스트")
class MenuServiceTest extends IntegrationTest {
	@Autowired
	private MenuService menuService;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private MenuGroupDao menuGroupDao;
	@Autowired
	private ProductDao productDao;

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void create() {
		// given
		MenuGroup 추천_메뉴_그룹 = menuGroupDao.save(추천_메뉴_그룹().toMenuGroup());
		Product 후라이드치킨_상품 = productDao.save(후라이드치킨_상품().toProduct());
		Menu request = 후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu();

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
		MenuGroup 추천_메뉴_그룹 = menuGroupDao.save(추천_메뉴_그룹().toMenuGroup());
		Product 후라이드치킨_상품 = productDao.save(후라이드치킨_상품().toProduct());
		Menu request = 이름없는_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴 가격이 음수이면 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnNegativePrice() {
		// given
		MenuGroup 추천_메뉴_그룹 = menuGroupDao.save(추천_메뉴_그룹().toMenuGroup());
		Product 후라이드치킨_상품 = productDao.save(후라이드치킨_상품().toProduct());
		Menu request = 음수가격_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("특정 메뉴 그룹에 속하지 않으면 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnNotFoundMenuGroup() {
		// given
		Long unknownMenuGroupId = 0L;
		Product 후라이드치킨_상품 = productDao.save(후라이드치킨_상품().toProduct());
		Menu request = 후라이드후라이드_메뉴(unknownMenuGroupId, 후라이드치킨_상품.getId()).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴에 속한 상품들이 기등록된 상품이 아닐 경우 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnNotFoundProduct() {
		// given
		MenuGroup 추천_메뉴_그룹 = menuGroupDao.save(추천_메뉴_그룹().toMenuGroup());
		Long unknownProductId = 0L;
		Menu request = 후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), unknownProductId).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴의 가격이 메뉴에 속한 상품들의 (가격 * 수량) 합을 넘은 경우 메뉴를 등록할 수 없다.")
	@Test
	void createFailOnInvalidPrice() {
		// given
		MenuGroup 추천_메뉴_그룹 = menuGroupDao.save(추천_메뉴_그룹().toMenuGroup());
		Product 후라이드치킨_상품 = productDao.save(후라이드치킨_상품().toProduct());
		Menu request = 너무비싼_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu();

		// when
		ThrowingCallable throwingCallable = () -> menuService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		MenuGroup 추천_메뉴_그룹 = menuGroupDao.save(추천_메뉴_그룹().toMenuGroup());
		Product 후라이드치킨_상품 = productDao.save(후라이드치킨_상품().toProduct());
		Product 양념치킨_상품 = productDao.save(양념치킨_상품().toProduct());
		Menu 후라이드후라이드_메뉴 = menuDao.save(후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu());
		Menu 양념양념_메뉴 = menuDao.save(양념양념_메뉴(추천_메뉴_그룹.getId(), 양념치킨_상품.getId()).toMenu());

		// when
		List<Menu> actual = menuService.list();

		// then
		List<Long> actualIds = actual.stream().map(Menu::getId).collect(Collectors.toList());

		assertAll(
			() -> assertThat(actual).isNotEmpty(),
			() -> assertThat(actualIds).containsAll(Arrays.asList(후라이드후라이드_메뉴.getId(), 양념양념_메뉴.getId())));
	}
}

