package kitchenpos.event;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.event.MenuCreatedEvent;
import kitchenpos.menu.ui.exception.IllegalMenuPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menuproduct.domain.MenuProduct;
import kitchenpos.menuproduct.domain.MenuProductRepository;
import kitchenpos.menuproduct.dto.MenuProductRequest;
import kitchenpos.menuproduct.event.MenuProductEventHandler;
import kitchenpos.menuproduct.exception.NoMenuProductException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuProductEventTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    MenuProductRepository menuProductRepository;

    Product 후라이드치킨;
    Product 양념치킨;

    Menu 반반세트;

    MenuProductRequest 반반세트후라이드요청;
    MenuProductRequest 반반세트양념요청;

    MenuCreatedEvent menuCreatedEvent;
    MenuProductEventHandler menuProductEventHandler;

    @BeforeEach
    void setUp() {
        후라이드치킨 = new Product(1L, "후라이드치킨", BigDecimal.valueOf(18000L));
        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(19000L));
        반반세트 = new Menu(1L, "반반세트", BigDecimal.valueOf(50000L), 1L);
        반반세트후라이드요청 = new MenuProductRequest(후라이드치킨.getId(), 1L);
        반반세트양념요청 = new MenuProductRequest(양념치킨.getId(), 1L);

        menuCreatedEvent =
                new MenuCreatedEvent(반반세트, Arrays.asList(반반세트후라이드요청, 반반세트양념요청));

        menuProductEventHandler =
                new MenuProductEventHandler(productRepository, menuProductRepository);
    }


    @Test
    @DisplayName("세트메뉴 가격이 단일 메뉴 합한 가격보다 큰 경우")
    void 세트_메뉴_가격이_단일_메뉴_합한_가격보다_큰_경우() {
        //given
        given(productRepository.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
        given(productRepository.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));

        //when, then
        assertThatThrownBy(() -> menuProductEventHandler.saveMenuProduct(menuCreatedEvent))
                .isInstanceOf(IllegalMenuPriceException.class);
    }

    @DisplayName("등록시 메뉴의 상품이 등록이 하나도 안되어 있어서 메뉴상품을 구성할 수 없음")
    @Test
    void 상품_등록이_하나도_안되어_있어_메뉴상품_구성안됨() {
        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> menuProductEventHandler.saveMenuProduct(menuCreatedEvent))
                .isInstanceOf(NoMenuProductException.class);
    }
}
