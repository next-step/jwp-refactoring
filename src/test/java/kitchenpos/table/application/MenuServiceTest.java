package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuGroupRepository menuGroupRepository;
	@Mock
	private ProductRepository productRepository;
	@InjectMocks
	private MenuService menuService;

	private MenuGroup 양식;
	private Menu 피자파스타세트;
	private Product 피자;
	private Product 토마토파스타;
	private MenuProduct 피자한판;
	private MenuProduct 토마토파스타2개;

	@BeforeEach
	void setUp() {
		//메뉴 그룹
		양식 = new MenuGroup("양식");

		//상품
		피자 = new Product("피자", new Price(BigDecimal.valueOf(12000)));
		토마토파스타 = new Product("토마토파스타", new Price(BigDecimal.valueOf(10000)));

		//메뉴상품
		피자한판 = new MenuProduct(피자, 1);
		토마토파스타2개 = new MenuProduct(토마토파스타, 2);

		//메뉴, 메뉴상품목록 추가
		List<MenuProduct> menuProductList = new ArrayList<>();
		menuProductList.add(피자한판);
		menuProductList.add(토마토파스타2개);
		피자파스타세트 = new Menu("피자파스타세트", new Price(BigDecimal.valueOf(30000)), 양식, menuProductList);
	}

	@DisplayName("Menu 생성을 테스트 - happy path")
	@Test
	void testCreateMenu() {
		MenuProductRequest 메뉴상품_토마토파스타 = new MenuProductRequest(1L, 2);
		MenuProductRequest 메뉴상품_피자 = new MenuProductRequest(2L, 1);
		List<MenuProductRequest> menuProducts = 메뉴상품요청목록_생성(메뉴상품_토마토파스타, 메뉴상품_피자);

		BigDecimal price = BigDecimal.valueOf(30000);
		long menuGroupId = 1L;
		MenuRequest menuRequest = new MenuRequest("피자파스타세트", price, menuGroupId, menuProducts);

		when(menuGroupRepository.findById(menuGroupId)).thenReturn(Optional.of(양식));
		when(productRepository.findById(메뉴상품_피자.getProductId())).thenReturn(Optional.of(피자));
		when(productRepository.findById(메뉴상품_토마토파스타.getProductId())).thenReturn(Optional.of(토마토파스타));
		when(menuRepository.save(any())).thenReturn(피자파스타세트);

		MenuResponse actual = menuService.create(menuRequest);

		assertThat(actual.getName()).isEqualTo(피자파스타세트.getName());
		List<MenuProductResponse> expectedMenuProducts = 피자파스타세트.getMenuProducts()
			.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());
		assertThat(actual.getMenuProducts()).containsExactlyElementsOf(expectedMenuProducts);
	}

	@DisplayName("메뉴 가격이 0보다 작은경우 오류발생")
	@Test
	void testCreateErrorPriceZero() {
		MenuProductRequest 메뉴상품_토마토파스타 = new MenuProductRequest(1L, 2);
		MenuProductRequest 메뉴상품_피자 = new MenuProductRequest(2L, 1);
		List<MenuProductRequest> menuProducts = 메뉴상품요청목록_생성(메뉴상품_토마토파스타, 메뉴상품_피자);

		BigDecimal price = BigDecimal.valueOf(-1);
		MenuRequest menuRequest = new MenuRequest("신규메뉴", price, 1L, menuProducts);

		when(menuGroupRepository.findById(menuRequest.getMenuGroupId())).thenReturn(Optional.of(양식));
		when(productRepository.findById(메뉴상품_피자.getProductId())).thenReturn(Optional.of(피자));
		when(productRepository.findById(메뉴상품_토마토파스타.getProductId())).thenReturn(Optional.of(토마토파스타));

		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품 가격은 0보다 작을 수 없습니다.");
	}

	private List<MenuProductRequest> 메뉴상품요청목록_생성(MenuProductRequest... menuProductRequests) {
		List<MenuProductRequest> menuProducts = new ArrayList<>();
		Collections.addAll(menuProducts, menuProductRequests);
		return menuProducts;
	}

	@DisplayName("메뉴가 메뉴 그룹에 포함되어있지 않은경우 오류 발생")
	@Test
	void testMenuNotContainsInMenuGroup() {
		BigDecimal price = BigDecimal.valueOf(20000);
		MenuRequest menuRequest = new MenuRequest("신규메뉴", price, 1L, null);

		when(menuGroupRepository.findById(menuRequest.getMenuGroupId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 메뉴 그룹을 찾을 수 없습니다.");
	}

	@DisplayName("메뉴의 메뉴상품이 상품에 등록되어 있지 않은경우 오류 발생")
	@Test
	void testMenuProductNotSavedProduct() {
		List<MenuProductRequest> menuProducts = new ArrayList<>();
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 3);
		menuProducts.add(menuProductRequest);

		BigDecimal price = BigDecimal.valueOf(20000);
		MenuRequest menuRequest = new MenuRequest("신규메뉴", price, 1L, menuProducts);

		when(menuGroupRepository.findById(menuRequest.getMenuGroupId())).thenReturn(Optional.of(양식));
		when(productRepository.findById(menuProductRequest.getProductId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 상품을 찾을 수 없습니다");
	}

	@DisplayName("메뉴 가격이 메뉴 상품의 가격의 합보다 크면 오류 발생")
	@Test
	void testMenuPriceBiggerThanTotalMenuProductPrice() {
		long productId = 1L;
		List<MenuProductRequest> menuProducts = new ArrayList<>();
		MenuProductRequest menuProductRequest = new MenuProductRequest(productId, 1);
		menuProducts.add(menuProductRequest);

		BigDecimal price = BigDecimal.valueOf(20000);
		long menuGroupId = 1L;
		MenuRequest menuRequest = new MenuRequest("신규메뉴", price, menuGroupId, menuProducts);

		when(menuGroupRepository.findById(menuGroupId)).thenReturn(Optional.of(양식));
		when(productRepository.findById(productId)).thenReturn(Optional.of(토마토파스타));

		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 가격은 메뉴 상품 가격의 합보다 작아야합니다.");
	}

	@DisplayName("메뉴 목록 반환을 테스트")
	@Test
	void testList() {
		List<Menu> menus = new ArrayList<>();
		menus.add(피자파스타세트);

		when(menuRepository.findAll()).thenReturn(menus);

		List<MenuResponse> actual = menuService.list();

		List<MenuResponse> expectedMenus = menus.stream().map(MenuResponse::of).collect(Collectors.toList());
		assertThat(actual).containsExactlyElementsOf(expectedMenus);
	}
}