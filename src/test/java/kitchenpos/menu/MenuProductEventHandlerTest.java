package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.event.MenuCreateEvent;
import kitchenpos.menu.event.MenuProductEventHandler;
import kitchenpos.menu.exception.MenuException;

@DisplayName("메뉴 상품 이벤트 핸들러 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuProductEventHandlerTest {

	@Mock
	private MenuProductRepository menuProductRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private MenuProductEventHandler menuProductEventHandler;

	@Test
	@DisplayName("메뉴 생성 시 메뉴 상품 생성 테스트")
	void 메뉴_생성_시_메뉴_상품_생성_테스트() {
		MenuGroup 치킨 = new MenuGroup(1L, "치킨");
		Menu 양념반_후라이드반 = new Menu(1L, "양념반 후라이드반", new Price(BigDecimal.valueOf(2000)), 치킨);
		Product 양념치킨 = new Product(1L, "양념치킨", new Price(BigDecimal.valueOf(1000)));
		Product 후라이드치킨 = new Product(2L, "후라이드치킨", new Price(BigDecimal.valueOf(1000)));
		MenuProductRequest 양념_반_치킨_요청 = new MenuProductRequest(양념치킨.getId(), 양념반_후라이드반.getId(), 1);
		MenuProductRequest 후라이드_반_치킨_요청 = new MenuProductRequest(후라이드치킨.getId(), 양념반_후라이드반.getId(), 1);
		MenuProduct 양념_반_치킨 = 양념_반_치킨_요청.toMenuProduct(양념치킨);
		MenuProduct 후라이드_반_치킨 = 후라이드_반_치킨_요청.toMenuProduct(후라이드치킨);
		List<MenuProductRequest> 메뉴상품들_요청 = Arrays.asList(양념_반_치킨_요청, 후라이드_반_치킨_요청);

		given(productRepository.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
		given(productRepository.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
		given(menuProductRepository.save(any())).willReturn(양념_반_치킨);
		given(menuProductRepository.save(any())).willReturn(후라이드_반_치킨);

		menuProductEventHandler.createMenuProducts(new MenuCreateEvent(양념반_후라이드반, 메뉴상품들_요청));
	}

	@DisplayName("메뉴 생성 시 메뉴 가격보다 메뉴의 메뉴 상품 리스트의 가격의 합이 크면 안된다.")
	@Test
	void 메뉴_생성_메뉴_가격보다_메뉴의_메뉴_상품_리스트의_가격의_합이_크면_안된다() {
		MenuGroup 치킨 = new MenuGroup(1L, "치킨");
		Menu 양념반_후라이드반 = new Menu(1L, "양념반 후라이드반", new Price(BigDecimal.valueOf(3000)), 치킨);
		Product 양념치킨 = new Product(1L, "양념치킨", new Price(BigDecimal.valueOf(1000)));
		Product 후라이드치킨 = new Product(2L, "후라이드치킨", new Price(BigDecimal.valueOf(1000)));
		MenuProductRequest 양념_반_치킨_요청 = new MenuProductRequest(양념치킨.getId(), 양념반_후라이드반.getId(), 1);
		MenuProductRequest 후라이드_반_치킨_요청 = new MenuProductRequest(후라이드치킨.getId(), 양념반_후라이드반.getId(), 1);
		MenuProduct 양념_반_치킨 = 양념_반_치킨_요청.toMenuProduct(양념치킨);
		MenuProduct 후라이드_반_치킨 = 후라이드_반_치킨_요청.toMenuProduct(후라이드치킨);
		List<MenuProductRequest> 메뉴상품들_요청 = Arrays.asList(양념_반_치킨_요청, 후라이드_반_치킨_요청);

		given(productRepository.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
		given(productRepository.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));

		assertThatThrownBy(() -> {
			menuProductEventHandler.createMenuProducts(new MenuCreateEvent(양념반_후라이드반, 메뉴상품들_요청));
		}).isInstanceOf(MenuException.class);
	}

	@DisplayName("메뉴 생성 시 메뉴 상품의 상품이 존재하지 않는 경우 에러 발생")
	@Test
	void 메뉴_생성_시_메뉴_상품의_상품이_존재하지않는_경우_에러_발생() {
		MenuGroup 치킨 = new MenuGroup(1L, "치킨");
		Menu 양념반_후라이드반 = new Menu(1L, "양념반 후라이드반", new Price(BigDecimal.valueOf(3000)), 치킨);
		Product 양념치킨 = new Product(1L, "양념치킨", new Price(BigDecimal.valueOf(1000)));
		Product 후라이드치킨 = new Product(2L, "후라이드치킨", new Price(BigDecimal.valueOf(1000)));
		MenuProductRequest 양념_반_치킨_요청 = new MenuProductRequest(양념치킨.getId(), 양념반_후라이드반.getId(), 1);
		MenuProductRequest 후라이드_반_치킨_요청 = new MenuProductRequest(후라이드치킨.getId(), 양념반_후라이드반.getId(), 1);
		MenuProduct 양념_반_치킨 = 양념_반_치킨_요청.toMenuProduct(양념치킨);
		MenuProduct 후라이드_반_치킨 = 후라이드_반_치킨_요청.toMenuProduct(후라이드치킨);
		List<MenuProductRequest> 메뉴상품들_요청 = Arrays.asList(양념_반_치킨_요청, 후라이드_반_치킨_요청);

		given(productRepository.findById(양념치킨.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			menuProductEventHandler.createMenuProducts(new MenuCreateEvent(양념반_후라이드반, 메뉴상품들_요청));
		}).isInstanceOf(MenuException.class);
	}
}
