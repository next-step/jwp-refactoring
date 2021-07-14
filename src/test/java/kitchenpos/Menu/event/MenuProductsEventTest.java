package kitchenpos.Menu.event;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.event.MenuCreatedEvent;
import kitchenpos.menu.event.MenuProductsEventHandler;
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

@DisplayName("메뉴상품 관련 이벤트 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuProductsEventTest {
    private Product 오리지널콤보;
    private Product 레드콤보;

    private Menu 콤보세트;

    private MenuProductRequest 콤보세트오리지널요청;
    private MenuProductRequest 콤보세트레드요청;

    private MenuCreatedEvent menuCreatedEvent;
    private MenuProductsEventHandler menuProductEventHandler;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @BeforeEach
    void setUp() {
        상품_설정();

        상품_요청값_설정();

        menuCreatedEvent = 메뉴_생성_이벤트();
        menuProductEventHandler = 메뉴_상품_이벤트_핸들러();
    }

    @Test
    @DisplayName("메뉴 가격보다 메뉴에 속한 상품 가격의 합이 작으면 예외 발생한다.")
    void createMenu_메뉴상품_가격합_예외() {
        given(productRepository.findById(오리지널콤보.getId())).willReturn(Optional.of(오리지널콤보));
        given(productRepository.findById(레드콤보.getId())).willReturn(Optional.of(레드콤보));

        메뉴_가격이_비쌀경우_예외_발생();
    }

    @DisplayName("존재하지 않는 메뉴 그룹을 메뉴에 등록시 예외 발생한다.")
    @Test
    void create_메뉴그룹_예외() {
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        존재하지_않는_메뉴그룹으로_메뉴_생성_요청시_예외_발생함();
    }

    private void 상품_요청값_설정() {
        콤보세트오리지널요청 = new MenuProductRequest(1L, 오리지널콤보.getId(), 1L);
        콤보세트레드요청 = new MenuProductRequest(2L, 레드콤보.getId(), 1L);
    }

    private void 상품_설정() {
        콤보세트 = new Menu(1L, "반반세트", new BigDecimal(44000));
        오리지널콤보 = new Product(1L, "후라이드치킨", new BigDecimal(18000));
        레드콤보 = new Product(2L, "양념치킨", new BigDecimal(19000));
    }

    private MenuCreatedEvent 메뉴_생성_이벤트() {
        return new MenuCreatedEvent(콤보세트, Arrays.asList(콤보세트오리지널요청, 콤보세트레드요청));
    }

    private MenuProductsEventHandler 메뉴_상품_이벤트_핸들러() {
        return new MenuProductsEventHandler(productRepository, menuProductRepository);
    }

    private void 메뉴_가격이_비쌀경우_예외_발생() {
        assertThatThrownBy(() -> menuProductEventHandler.saveMenuProduct(menuCreatedEvent))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 존재하지_않는_메뉴그룹으로_메뉴_생성_요청시_예외_발생함() {
        assertThatThrownBy(() -> menuProductEventHandler.saveMenuProduct(menuCreatedEvent))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
