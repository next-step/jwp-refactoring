package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

@DisplayName("메뉴 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private MenuService menuService;

	@DisplayName("메뉴 그룹이 존재하지 않는 메뉴는 등록할 수 없다.")
	@Test
	void createMenuNoGroupMenuTest() {
		assertThatThrownBy(() -> menuService.create(new MenuRequest()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 그룹이 존재하지 않는 메뉴는 등록할 수 없습니다.");
	}

	@DisplayName("상품으로 등록된 메뉴만 등록할 수 있다.")
	@Test
	void createMenuNotMatchedProductMenuTest() {
		// given
		List<MenuProductRequest> menuProductRequests = asList(new MenuProductRequest(1L, 1L));
		MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.ZERO, 1L, menuProductRequests);
		when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(mock(MenuGroup.class)));
		when(productRepository.findAllById(anyList())).thenReturn(new ArrayList<>());

		// when
		// than
		assertThatThrownBy(() -> menuService.create(menuRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품으로 등록되지 않은 메뉴는 등록할 수 없습니다.");
	}

	@DisplayName("메뉴 목록 조회를 할 수 있다.")
	@Test
	void listTest() {
		// when
		menuService.list();

		// then
		verify(menuRepository).findAll();
	}
}
