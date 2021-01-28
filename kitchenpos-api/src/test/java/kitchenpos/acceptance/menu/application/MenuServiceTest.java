package kitchenpos.acceptance.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuProductRepository menuProductRepository;
	@Mock
	private MenuGroupService menuGroupService;
	@Mock
	private ProductService productService;
	@InjectMocks
	private MenuService menuService;

	private MenuGroup menuGroup;
	private Products products;
	private List<MenuProductRequest> menuProductRequests;

	private Long 치킨ID = 1L;
	private Long 피자ID = 2L;

	@BeforeEach
	void setUp() {
		menuGroup = MenuGroup.of("세트");
		products = Products.of(Arrays.asList(Product.of(치킨ID, "치킨", 16000), Product.of(피자ID, "피자", 20000)));
		menuProductRequests = Arrays.asList(MenuProductRequest.of(1L, 2), MenuProductRequest.of(2L, 1));
	}

	@DisplayName("메뉴: 메뉴 생성 테스트")
	@Test
	void createTest() {
		// given
		MenuRequest request = MenuRequest.of("치피세트", 50000, 1L, menuProductRequests);
		MenuProducts menuProducts = request.toMenuProducts(products, menuGroup);
		given(menuGroupService.findById(request.getMenuGroupId())).willReturn(menuGroup);
		given(productService.findAllByIds(any())).willReturn(products);
		given(menuProductRepository.saveAll(any())).willReturn(menuProducts.getMenuProducts());

		// when
		MenuResponse actual = menuService.create(request);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(request.getName())
		);
	}

	@DisplayName("메뉴[예외]: 메뉴 생성 테스트(1. 메뉴 가격은 null이 아니고, 0보다 커야한다.)")
	@ParameterizedTest
	@ValueSource(longs = {-500L})
	void errorMenuPriceTest(Long price) {
		// given // when
		MenuRequest request = MenuRequest.of("치피세트", price, 1L, menuProductRequests);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> menuService.create(request)
		);
	}

	@DisplayName("메뉴[예외]: 메뉴 생성 테스트(2. 메뉴 그룹이 존재해야 한다.)")
	@Test
	void errorNotFoundMenuGroupTest() {
		// given
		MenuRequest request = MenuRequest.of("치피세트", 50000, 1L, menuProductRequests);

		// when
		when(menuGroupService.findById(1L)).thenThrow(EntityNotFoundException.class);

		// then
		assertThatThrownBy(
			() -> menuService.create(request)
		).isInstanceOf(EntityNotFoundException.class);
	}

	@DisplayName("메뉴[예외]: 메뉴 생성 테스트(3. 상품이 존재해야 한다.)")
	@Test
	void errorNotFoundProductTest() {
		// given
		MenuRequest request = MenuRequest.of("치피세트", 50000, 1L, menuProductRequests);
		given(menuGroupService.findById(request.getMenuGroupId())).willReturn(menuGroup);

		// when
		when(productService.findAllByIds(any())).thenThrow(EntityNotFoundException.class);

		// then
		assertThatThrownBy(
			() -> menuService.create(request)
		).isInstanceOf(EntityNotFoundException.class);
	}

	@DisplayName("메뉴[예외]: 메뉴 생성 테스트(4. 메뉴 가격이 상품들의 총 합보다 작거나 같아야 한다.)")
	@Test
	void errorTotalPriceTest() {
		// given
		MenuRequest request = MenuRequest.of("치피세트", 53000, 1L, menuProductRequests);

		// when
		when(menuGroupService.findById(request.getMenuGroupId())).thenReturn(menuGroup);
		when(productService.findAllByIds(any())).thenReturn(products);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> menuService.create(request)
		);
	}
}
