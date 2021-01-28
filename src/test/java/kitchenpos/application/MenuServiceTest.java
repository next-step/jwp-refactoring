package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;

@DisplayName("메뉴 BO 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuDao menuDao;
	@Mock
	private ProductService productService;
	@Mock
	private MenuGroupService menuGroupService;

	@InjectMocks
	private MenuService menuService;

	@DisplayName("메뉴 생성 : 메뉴 이름과 가격, 메뉴 그룹 번호, 상품 번호 및 가격 목록을 받는다")
	@Test
	void create_happyPath() {
		// given
		BigDecimal 메뉴_상품1_가격 = 상품1.getPrice().multiply(BigDecimal.valueOf(메뉴_상품1.getQuantity()));
		BigDecimal 메뉴_상품2_가격 = 상품2.getPrice().multiply(BigDecimal.valueOf(메뉴_상품2.getQuantity()));

		Menu 새_메뉴 = new Menu.Builder()
			.name("새_메뉴")
			.price(메뉴_상품1_가격.add(메뉴_상품2_가격))
			.menuGroup(메뉴_그룹1)
			.menuProducts(메뉴_상품1, 메뉴_상품2)
			.build();

		given(productService.findById(상품1.getId())).willReturn(상품1);
		given(productService.findById(상품2.getId())).willReturn(상품2);
		given(menuDao.save(any(Menu.class))).willAnswer(invocation -> {
			Menu mock = spy(invocation.getArgument(0, Menu.class));
			when(mock.getId()).thenReturn(1L);
			return mock;
		});
		given(menuGroupService.findById(메뉴_그룹1.getId())).willReturn(메뉴_그룹1);

		// when : 정상 케이스
		MenuResponse response = menuService.create(fromMenuToRequest(새_메뉴));

		// then : 메뉴 가격이 메뉴에 속한 상품들의 금액과 같거나 작음
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getPrice()).isEqualTo(새_메뉴.getPrice().longValue());
		assertThat(response.getMenuGroupId()).isEqualTo(새_메뉴.getMenuGroup().getId());
		assertThat(response.getMenuProducts())
			.map(MenuProductResponse::getProductId)
			.contains(상품1.getId(), 상품2.getId());
	}

	@DisplayName("메뉴 생성 : 메뉴 가격이 메뉴에 속한 상품들의 금액보다 큼")
	@Test
	void create_exceptionCase1() {
		// given
		BigDecimal 메뉴_상품1_가격 = 상품1.getPrice().multiply(BigDecimal.valueOf(메뉴_상품1.getQuantity()));
		BigDecimal 메뉴_상품2_가격 = 상품2.getPrice().multiply(BigDecimal.valueOf(메뉴_상품2.getQuantity()));
		given(productService.findById(상품1.getId())).willReturn(상품1);
		given(productService.findById(상품2.getId())).willReturn(상품2);

		// when : 예외 케이스
		MenuRequest 새_메뉴_요청 = new MenuRequest(
			"새_메뉴",
			메뉴_상품1_가격.add(메뉴_상품2_가격).add(BigDecimal.ONE),
			메뉴_그룹1.getId(),
			Arrays.asList(fromMenuProductToRequest(메뉴_상품1), fromMenuProductToRequest(메뉴_상품2))
		);

		// then : 메뉴 가격은 메뉴에 속한 상품들의 금액보다 클 수 없음.
		assertThatThrownBy(() -> menuService.create(새_메뉴_요청)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@DisplayName("메뉴 생성 : 메뉴가 그룹에 속해있지 않음")
	@Test
	void create_exceptionCase2() {
		// given
		given(productService.findById(상품1.getId())).willReturn(상품1);
		given(productService.findById(상품2.getId())).willReturn(상품2);

		// when : 예외 케이스
		MenuRequest 새_메뉴_요청 = new MenuRequest(
			"새_메뉴",
			BigDecimal.valueOf(10000L),
			null,
			Arrays.asList(fromMenuProductToRequest(메뉴_상품1), fromMenuProductToRequest(메뉴_상품2))
		);

		// then : 메뉴 가격은 메뉴에 속한 상품들의 금액보다 클 수 없음.
		assertThatThrownBy(() -> menuService.create(새_메뉴_요청)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@DisplayName("메뉴 목록 : 조회 결과에는 메뉴 번호, 메뉴 이름, 가격, 메뉴 그룹 번호, 메뉴 상품 목록이 포함됨.")
	@Test
	void list() {
		// given
		Menu 새_메뉴 = new Menu.Builder()
			.id(-1L)
			.name("새_메뉴")
			.menuGroup(메뉴_그룹1)
			.menuProducts(메뉴_상품1, 메뉴_상품2)
			.price(BigDecimal.valueOf(10000L))
			.build();
		given(menuDao.findAll()).willReturn(Arrays.asList(새_메뉴));

		// when
		List<MenuResponse> listResponse = menuService.list();

		// then
		assertThat(listResponse).anySatisfy(menuResponse -> {
			assertThat(menuResponse.getId()).isEqualTo(-1L);
			assertThat(menuResponse.getPrice()).isEqualTo(새_메뉴.getPrice().longValue());
			assertThat(menuResponse.getMenuGroupId()).isEqualTo(새_메뉴.getMenuGroup().getId());
			assertThat(menuResponse.getMenuProducts())
				.map(MenuProductResponse::getProductId)
				.contains(상품1.getId(), 상품2.getId());
		});
	}

	public MenuRequest fromMenuToRequest(Menu menu) {
		return new MenuRequest(
			menu.getName(),
			menu.getPrice(),
			menu.getMenuGroup() == null ? 0 : menu.getMenuGroup().getId(),
			menu.getMenuProducts().stream()
				.map(this::fromMenuProductToRequest)
				.collect(Collectors.toList())
		);
	}

	public MenuProductRequest fromMenuProductToRequest(MenuProduct menuProduct) {
		return new MenuProductRequest(
			menuProduct.getProduct().getId(),
			menuProduct.getQuantity()
		);
	}

}
