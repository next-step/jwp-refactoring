package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

public class MenuServiceTest extends AcceptanceTest {
	@Autowired
	private MenuService menuService;
	@Autowired
	private MenuGroupService menuGroupService;
	@Autowired
	private ProductService productService;

	@DisplayName("메뉴 생성")
	@Test
	void create() {
		MenuGroupResponse menuGroupResponse = menuGroupService.create(new MenuGroupRequest("추천메뉴"));
		ProductResponse productResponse = productService.create(new ProductRequest("후라이드", 11000));
		MenuProductRequest menuProductRequest = new MenuProductRequest(productResponse.getId(), 2);

		MenuResponse menuResponse = menuService.create(new MenuRequest("후라이드", 19000, menuGroupResponse.getId(),
			Arrays.asList(menuProductRequest)));

		assertThat(menuResponse.getId()).isNotNull();
	}

	@DisplayName("메뉴 조회")
	@Test
	void list() {
		MenuGroupResponse menuGroupResponse = menuGroupService.create(new MenuGroupRequest("추천메뉴"));
		ProductResponse productResponse = productService.create(new ProductRequest("후라이드", 11000));
		MenuProductRequest menuProductRequest = new MenuProductRequest(productResponse.getId(), 2);

		menuService.create(
			new MenuRequest("후라이드", 19000, menuGroupResponse.getId(), Arrays.asList(menuProductRequest)));

		List<MenuResponse> menuResponses = menuService.list();

		assertThat(menuResponses).hasSize(1);
		assertThat(menuResponses.get(0).getName()).isEqualTo("후라이드");
		assertThat(menuResponses.get(0).getPrice()).isEqualTo(19000);
	}

	@DisplayName("상품이 없을 경우 오류 발생")
	@Test
	void findAllMenuByIds() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.findAllMenuByIds(Arrays.asList(10L)));
	}
}
