package kitchenpos.unit;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
public class MenuServiceTest {

	@Autowired
	private MenuService menuService;

	@BeforeEach
	void setUp() {

	}

	@Test
	@DisplayName("메뉴를 등록한다")
	void create() {
		List<MenuProductRequest> menuProductRequests = new ArrayList<>();
		MenuProductRequest menuProductRequest = new MenuProductRequest(1, 2);
		menuProductRequests.add(menuProductRequest);
		MenuRequest menuRequest = new MenuRequest("양념치킨", BigDecimal.valueOf(16_000), 1L, menuProductRequests);

		MenuResponse menuResponse = menuService.create(menuRequest);
		assertThat(menuResponse.getName()).isEqualTo("양념치킨");
	}

	@Test
	@DisplayName("메뉴 등록 시 가격이 null 또는 0 미만이면 에러")
	void givenPriceUnderZeroOrNullWhenCreateMenuThenError() {
		List<MenuProductRequest> menuProductRequests = new ArrayList<>();
		MenuProductRequest menuProductRequest = new MenuProductRequest(1, 2);
		menuProductRequests.add(menuProductRequest);
		MenuRequest menuRequest = new MenuRequest("양념치킨", BigDecimal.valueOf(-1), 1L, menuProductRequests);
		assertThrows(IllegalArgumentException.class, () -> menuService.create(menuRequest));
	}

	@Test
	@DisplayName("메뉴 목록을 조회한다")
	void list() {
		List<MenuResponse> menuResponses = menuService.list();
		assertThat(menuResponses.size()).isEqualTo(6);
	}
}
