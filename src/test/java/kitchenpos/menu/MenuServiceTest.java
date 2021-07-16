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
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.exception.PriceException;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@InjectMocks
	private MenuService menuService;

	MenuGroup 치킨;
	Menu 양념반_후라이드반;
	Product 양념치킨;
	Product 후라이드치킨;
	MenuProduct 양념_반_치킨;
	MenuProduct 후라이드_반_치킨;

	@BeforeEach
	void setUp() {
		치킨 = new MenuGroup(1L, "치킨");
		양념치킨 = new Product(1L, "양념치킨", new Price(BigDecimal.valueOf(1000)));
		후라이드치킨 = new Product(2L, "후라이드치킨", new Price(BigDecimal.valueOf(1000)));
		양념_반_치킨 = new MenuProduct(1L, 양념치킨, new Quantity(1));
		후라이드_반_치킨 = new MenuProduct(2L, 후라이드치킨, new Quantity(1));
		양념반_후라이드반 = new Menu(1L, "양념반 후라이드반", new Price(BigDecimal.valueOf(2000)), 치킨);
	}

	@DisplayName("메뉴를 생성한다")
	@Test
	void 메뉴를_생성한다() {
		MenuProductRequest 양념_반_치킨_요청 = new MenuProductRequest(1L, 1L, 1);
		MenuProductRequest 후라이드_반_치킨_요청 = new MenuProductRequest(2L, 1L, 1);
		MenuRequest 양념반_후라이드_반_요청 = new MenuRequest("치킨", 2000, 1L, Arrays.asList(양념_반_치킨_요청, 후라이드_반_치킨_요청));

		given(menuGroupRepository.findById(치킨.getId())).willReturn(Optional.of(치킨));
		given(menuRepository.save(any())).willReturn(양념반_후라이드반);

		MenuResponse created = menuService.create(양념반_후라이드_반_요청);

		메뉴_생성_확인(created, 양념반_후라이드반);
	}

	@DisplayName("메뉴 생성 - 메뉴의 가격은 0보다 커야 한다.")
	@Test
	void 메뉴_생성_메뉴의_가격은_0보다_커야_한다() {

		MenuProductRequest 양념_반_치킨_요청 = new MenuProductRequest(1L, 1L, 1);
		MenuProductRequest 후라이드_반_치킨_요청 = new MenuProductRequest(2L, 1L, 1);
		MenuRequest 양념반_후라이드_반_요청 = new MenuRequest("치킨", -1000, 1L, Arrays.asList(양념_반_치킨_요청, 후라이드_반_치킨_요청));

		given(menuGroupRepository.findById(치킨.getId())).willReturn(Optional.of(치킨));

		assertThatThrownBy(() -> {
			menuService.create(양념반_후라이드_반_요청);
		}).isInstanceOf(PriceException.class);

	}

	@DisplayName("메뉴 생성 - 메뉴의 메뉴 그룹이 존재하지 않으면 에러 발생.")
	@Test
	void 메뉴_생성_메뉴의_메뉴_그룹이_존재하지_않으면_에러_발생() {
		MenuProductRequest 양념_반_치킨_요청 = new MenuProductRequest(1L, 1L, 1);
		MenuProductRequest 후라이드_반_치킨_요청 = new MenuProductRequest(2L, 1L, 1);
		MenuRequest 양념반_후라이드_반_요청 = new MenuRequest("치킨", 2000, 1L, Arrays.asList(양념_반_치킨_요청, 후라이드_반_치킨_요청));

		given(menuGroupRepository.findById(치킨.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			menuService.create(양념반_후라이드_반_요청);
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
		assertThat(created.getPrice()).isEqualTo(expected.getPrice().value());
		assertThat(created.getName()).isEqualTo(expected.getName());
	}

}
