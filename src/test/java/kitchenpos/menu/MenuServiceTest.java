package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.manugroup.domain.MenuGroupRepository;
import kitchenpos.manugroup.dto.MenuGroupRequest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.exception.PriceException;
import kitchenpos.menugroup.MenuGroupServiceTest;
import kitchenpos.product.ProductServiceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@Mock
	private MenuProductRepository menuProductRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private MenuService menuService;

	MenuGroup 치킨;
	MenuGroupRequest 치킨_요청;
	MenuRequest 양념반_후라이드반_요청;
	ProductRequest 양념치킨_요청;
	ProductRequest 후라이드치킨_요청;
	MenuProductRequest 양념_반_치킨_요청;
	MenuProductRequest 후라이드_반_치킨_요청;

	Menu 양념반_후라이드반;
	Product 양념치킨;
	Product 후라이드치킨;
	MenuProduct 양념_반_치킨;
	MenuProduct 후라이드_반_치킨;

	@BeforeEach
	void setUp() {
		치킨_요청 = MenuGroupServiceTest.메뉴그룹_생성_요청("치킨");
		양념치킨_요청 = ProductServiceTest.상품생성_요청(new BigDecimal(10000), "양념 치킨");
		후라이드치킨_요청 = ProductServiceTest.상품생성_요청(new BigDecimal(9000), "후라이드 치킨");
		양념_반_치킨_요청 = 메뉴상품생성(양념치킨_요청.toProduct().getId(), 1);
		후라이드_반_치킨_요청 = 메뉴상품생성(후라이드치킨_요청.toProduct().getId(), 1);

		치킨 = 치킨_요청.toMenuGroup();
		양념반_후라이드반_요청 = 메뉴생성("양념 반 후라이드 반", 19000, 치킨.getId(), Arrays.asList(양념_반_치킨_요청, 후라이드_반_치킨_요청));

		양념치킨 = 양념치킨_요청.toProduct();
		후라이드치킨 = 후라이드치킨_요청.toProduct();
		양념_반_치킨 = 양념_반_치킨_요청.toMenuProduct(양념치킨);
		후라이드_반_치킨 = 양념_반_치킨_요청.toMenuProduct(후라이드치킨);
		양념반_후라이드반 = 양념반_후라이드반_요청.toMenu(치킨, new MenuProducts(Arrays.asList(양념_반_치킨, 후라이드_반_치킨)));
	}

	@DisplayName("메뉴를 생성한다")
	@Test
	void 메뉴를_생성한다() {
		given(menuGroupRepository.findById(치킨.getId())).willReturn(Optional.of(치킨));
		given(menuProductRepository.findAllById(any())).willReturn(Arrays.asList(양념_반_치킨, 후라이드_반_치킨));
		given(productRepository.findById(anyLong())).willReturn(Optional.of(양념치킨_요청.toProduct()));
		given(productRepository.findById(anyLong())).willReturn(Optional.of(후라이드치킨_요청.toProduct()));
		given(menuRepository.save(any())).willReturn(양념반_후라이드반);

		MenuResponse created = menuService.create(양념반_후라이드반_요청);

		메뉴_생성_확인(created, 양념반_후라이드반);
	}

	@DisplayName("메뉴 생성 - 메뉴의 가격은 0보다 커야 한다.")
	@Test
	void 메뉴_생성_메뉴의_가격은_0보다_커야_한다() {
		given(menuGroupRepository.findById(양념반_후라이드반_요청.getMenuGroupId())).willReturn(Optional.of(치킨));
		given(menuProductRepository.findAllById(any())).willReturn(
			Arrays.asList(양념_반_치킨, 후라이드_반_치킨));

		assertThatThrownBy(() -> {
			양념반_후라이드반_요청 = 메뉴생성("양념 반 후라이드 반", -1000, 치킨.getId(),
				Arrays.asList(양념_반_치킨_요청, 후라이드_반_치킨_요청));
			menuService.create(양념반_후라이드반_요청);
		}).isInstanceOf(PriceException.class);

	}

	@DisplayName("메뉴 생성 - 메뉴의 메뉴 그룹이 존재하지 않으면 에러 발생.")
	@Test
	void 메뉴_생성_메뉴의_메뉴_그룹이_존재하지_않으면_에러_발생() {
		given(menuGroupRepository.findById(양념반_후라이드반_요청.getMenuGroupId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			양념반_후라이드반_요청 = 메뉴생성("양념 반 후라이드 반", 19000, 치킨.getId(), null);
			menuService.create(양념반_후라이드반_요청);
		}).isInstanceOf(MenuException.class);

	}

	@DisplayName("메뉴 생성 - 메뉴 가격보다 메뉴의 메뉴 상품 리스트의 가격의 합이 크면 안된다.")
	@Test
	void 메뉴_생성_메뉴_가격보다_메뉴의_메뉴_상품_리스트의_가격의_합이_크면_안된다() {
		assertThatThrownBy(() -> {
			양념반_후라이드반_요청 = 메뉴생성("양념 반 후라이드 반", 20000, 치킨.getId(), null);
			menuService.create(양념반_후라이드반_요청);
		}).isInstanceOf(MenuException.class);
	}

	@DisplayName("메뉴 생성 - 메뉴 리스트를 조회한다")
	@Test
	void 메뉴_리스트를_조회한다() {
		given(menuRepository.findAll()).willReturn(Arrays.asList(양념반_후라이드반));

		List<MenuResponse> selectedMenus = menuService.list();

		메뉴_리스트_조회_확인(selectedMenus);
	}

	private void 메뉴_리스트_조회_확인(List<MenuResponse> selectedMenus) {
		assertThat(selectedMenus).isNotNull();
		assertThat(selectedMenus).isNotEmpty();
	}

	private void 메뉴_생성_확인(MenuResponse created, Menu expected) {
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.getMenuGroupResponse().getId()).isEqualTo(expected.getMenuGroup().getId());
		assertThat(created.getMenuProductResponses()).isNotNull();
		assertThat(created.getPrice()).isEqualTo(expected.getPrice().value());
		assertThat(created.getName()).isEqualTo(expected.getName());
	}

	public static MenuRequest 메뉴생성(String name, int price, Long menuGroupId,
		List<MenuProductRequest> menuProductRequests) {
		MenuRequest menuRequest = new MenuRequest(name, price, menuGroupId, menuProductRequests);
		return menuRequest;
	}

	public static MenuProductRequest 메뉴상품생성(Long id, long quantity) {
		MenuProductRequest menuProductRequest = new MenuProductRequest(id, quantity);
		return menuProductRequest;
	}

}
