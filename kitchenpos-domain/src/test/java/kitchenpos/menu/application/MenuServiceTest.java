package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupTest;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductTest;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@InjectMocks
	private MenuService menuService;
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuGroupService menuGroupService;
	@Mock
	private ProductService productService;
	@Mock
	private MenuValidator menuValidator;

	@DisplayName("메뉴 목록을 조회한다")
	@Test
	void listTest() {
		// given
		List<Menu> persist = new ArrayList<>();
		Menu menu1 = Menu.of(1L, "반반치킨", BigDecimal.valueOf(1000), MenuGroupTest.추천메뉴);
		persist.add(menu1);

		given(menuRepository.findAll()).willReturn(persist);

		// when
		List<MenuResponse> result = menuService.list();

		// then
		assertThat(result.size()).isEqualTo(1);
	}

	@DisplayName("메뉴를 등록한다")
	@Test
	void createTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(ProductTest.후라이드.getId(), 2L);
		MenuRequest request = new MenuRequest(
			MenuTest.후라이드둘.getName().toText(),
			MenuTest.후라이드둘.getPrice().toBigDecimal(),
			MenuTest.후라이드둘.getMenuGroup().getId(),
			Collections.singletonList(menuProductRequest));

		given(menuRepository.save(any())).willReturn(MenuTest.후라이드둘);
		given(menuGroupService.getById(any())).willReturn(MenuGroupTest.추천메뉴);
		given(productService.getById(any())).willReturn(ProductTest.후라이드);

		// when
		MenuResponse result = menuService.create(request);

		// then
		assertThat(result.getName()).isEqualTo(request.getName());

	}

	@DisplayName("메뉴 가격이 0 미만이면 등록할 수 없다")
	@Test
	void createTest2() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(ProductTest.후라이드.getId(), 2L);
		MenuRequest request = new MenuRequest(
			MenuTest.후라이드둘.getName().toText(),
			BigDecimal.valueOf(-1),
			MenuTest.후라이드둘.getMenuGroup().getId(),
			Collections.singletonList(menuProductRequest));
		given(menuGroupService.getById(MenuGroupTest.추천메뉴.getId())).willReturn(MenuGroupTest.추천메뉴);

		// when, then
		assertThatThrownBy(() -> menuService.create(request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("메뉴 그룹을 지정하지 않으면 등록할 수 없다")
	@Test
	void createTest3() {
		// given
		MenuRequest request = new MenuRequest(
			MenuTest.후라이드둘.getName().toText(),
			BigDecimal.valueOf(30_000),
			MenuTest.후라이드둘.getMenuGroup().getId(),
			new ArrayList<>());

		// when, then
		assertThatThrownBy(() -> menuService.create(request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("메뉴의 상품이 모두 등록되어 있어야 등록할 수 있다")
	@Test
	void createTest4() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(ProductTest.후라이드.getId(), 2L);
		MenuRequest request = new MenuRequest(
			MenuTest.후라이드둘.getName().toText(),
			MenuTest.후라이드둘.getPrice().toBigDecimal(),
			MenuTest.후라이드둘.getMenuGroup().getId(),
			Collections.singletonList(menuProductRequest));

		given(productService.getById(any())).willThrow(new AppException(ErrorCode.NOT_FOUND, ""));
		given(menuGroupService.getById(any())).willReturn(MenuGroupTest.추천메뉴);

		// when, then
		assertThatThrownBy(() -> menuService.create(request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.NOT_FOUND.getMessage());
	}

}
