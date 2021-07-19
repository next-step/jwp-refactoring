package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuGroupRepository menuGroupRepository;
	@Mock
	private ProductService productService;

	@InjectMocks
	private MenuService menuService;

	@Test
	void menuCreateTest() {
		MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(20000), 1L, Arrays.asList(new MenuProductRequest(1L, 10L)));
		Menu menu = new Menu(1L, "치킨", BigDecimal.valueOf(20000), 1L);
		Product product = new Product(1L, "치킨무", BigDecimal.valueOf(10000));

		Mockito.when(menuGroupRepository.existsById(1L)).thenReturn(true);
		Mockito.when(menuRepository.save(menuRequest.toMenu())).thenReturn(menu);
		Mockito.when(productService.findById(1L)).thenReturn(product);

		assertThat(menuService.create(menuRequest)).isNotNull();
	}

	@Test
	@DisplayName("메뉴 생성 시 금액이 없거나 0원 이하면 익셉션 발생.")
	void menuCreateFailTest() {
		MenuRequest menuRequest = new MenuRequest("치킨", null, 1L, Arrays.asList(new MenuProductRequest()));
		Assertions.assertThatThrownBy(() -> menuService.create(menuRequest))
				.isInstanceOf(RuntimeException.class);

		MenuRequest menuRequest2 = new MenuRequest("치킨", BigDecimal.ZERO, 1L, Arrays.asList(new MenuProductRequest()));
		Assertions.assertThatThrownBy(() -> menuService.create(menuRequest2))
				.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("메뉴 생성 시 메뉴 그룹이 존재하지 않으면 익셉션 발생.")
	void menuCreateFailTest2() {
		MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(20000), 1L, Arrays.asList(new MenuProductRequest(1L, 10L)));

		Mockito.when(menuGroupRepository.existsById(1L)).thenReturn(false);

		Assertions.assertThatThrownBy(() -> menuService.create(menuRequest))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void getMenuListTest() {
		Mockito.when(menuRepository.findAll()).thenReturn(Lists.list(new Menu(), new Menu()));
		assertThat(menuService.list()).hasSize(2);
	}
}
