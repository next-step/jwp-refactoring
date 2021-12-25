package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuAddRequest;
import kitchenpos.menu.dto.MenuProductAddRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotFoundMenuProductException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.NotFoundMenuGroupException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@InjectMocks
	private MenuService menuService;

	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuGroupRepository menuGroupRepository;
	@Mock
	private ProductRepository productRepository;

	@DisplayName("메뉴생성")
	@Test
	void create() {
		final MenuGroup 메뉴그룹 = MenuGroup.of(1L, "메뉴그룹");
		final Product 콜라 = Product.of(1L, "콜라", 500);
		final List<MenuProduct> 메뉴상품목록 = Arrays.asList(MenuProduct.of(1L, null, 콜라, 2));
		final Menu 메뉴 = Menu.of(1L, "메뉴", 1_000, 메뉴그룹, 메뉴상품목록);

		given(menuGroupRepository.findById(any())).willReturn(Optional.of(메뉴그룹));
		given(productRepository.findAllById(any())).willReturn(Arrays.asList(콜라));
		given(menuRepository.save(any())).willReturn(메뉴);

		final MenuResponse createdMenu = menuService.create(
			MenuAddRequest.of("메뉴", BigDecimal.valueOf(1_000), 메뉴그룹.getId(),
				Arrays.asList(MenuProductAddRequest.of(콜라.getId(), 2L)))
		);

		assertAll(
			() -> assertThat(createdMenu.getId()).isNotNull(),
			() -> {
				assertThat(createdMenu.getMenuProducts().size()).isEqualTo(1);
				assertThat(createdMenu.getMenuProducts().get(0).getId()).isNotNull();
			}
		);
	}

	@DisplayName("메뉴생성: 메뉴그룹이 존재하지 않으면 예외발생")
	@Test
	void create_not_found_menu_group() {
		final Product 콜라 = Product.of(1L, "콜라", 50);
		final List<MenuProductAddRequest> 메뉴상품목록 = Arrays.asList(
			MenuProductAddRequest.of(콜라.getId(), 2L)
		);
		given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

		assertThatExceptionOfType(NotFoundMenuGroupException.class)
			.isThrownBy(() -> menuService.create(
				MenuAddRequest.of("메뉴", BigDecimal.valueOf(100), null, 메뉴상품목록)
			));
	}

	@DisplayName("메뉴생성: 상품이 존재하지 않으면 예외발생")
	@Test
	void create_not_found_menu_product() {
		final MenuGroup 메뉴그룹 = MenuGroup.of(1L, "메뉴그룹");
		final List<MenuProductAddRequest> 메뉴상품목록 = Arrays.asList(
			MenuProductAddRequest.of( null, 3L)
		);

		given(menuGroupRepository.findById(any())).willReturn(Optional.of(메뉴그룹));
		given(productRepository.findAllById(any())).willReturn(Arrays.asList());

		assertThatExceptionOfType(NotFoundMenuProductException.class)
			.isThrownBy(() -> menuService.create(
				MenuAddRequest.of("메뉴", BigDecimal.valueOf(300), 메뉴그룹.getId(), 메뉴상품목록)
			));
	}

	@DisplayName("메뉴목록조회")
	@Test
	void list() {
		final MenuGroup 메뉴그룹1 = MenuGroup.of(1L, "메뉴그룹1");
		final Product 사이다 = Product.of(1L, "사이다", 300);
		final List<MenuProduct> 메뉴상품목록 = Arrays.asList(MenuProduct.of(1L, null, 사이다, 3L));
		final Menu 메뉴1 = Menu.of(1L, "menu1", 900, 메뉴그룹1, 메뉴상품목록);
		final MenuGroup 메뉴그룹2 = MenuGroup.of(2L, "메뉴그룹2");
		final Menu 메뉴2 = Menu.of(2L, "menu2", 900, 메뉴그룹2, 메뉴상품목록);

		given(menuRepository.findAll()).willReturn(Arrays.asList(메뉴1, 메뉴2));

		assertThat(menuService.list().size()).isEqualTo(2);
	}
}
