package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.menu.exception.MenuPriceGreaterThanAmountException;
import kitchenpos.menu.application.MenuService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 두마리메뉴;
    private Product 후라이드;
    private MenuProduct 후라이드치킨상품;
    private Menu 후라이드치킨;
    private MenuRequest 메뉴요청;
    private MenuRequest 가격_없는_메뉴요청;
    private MenuRequest 음수_가격_메뉴요청;
    private MenuRequest 메뉴그룹_없는_메뉴요청;
    private MenuRequest 메뉴상품_가격합을_넘는_메뉴요청;

    @BeforeEach
    void setUp() {
        두마리메뉴 = MenuGroup.of(1L, "두마리메뉴");
        후라이드 = Product.of(1L, "후라이드", BigDecimal.valueOf(16_000));
        후라이드치킨상품 = MenuProduct.of( 후라이드, 2);
        후라이드치킨 = Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 두마리메뉴, Arrays.asList(후라이드치킨상품));

        List<MenuProductRequest> 메뉴상품요청목록 = Arrays.asList(MenuProductRequest.of(1L, 2));
        메뉴요청 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(16_000), 1L, 메뉴상품요청목록);
        가격_없는_메뉴요청 = MenuRequest.of("후라이드치킨", null, 1L, 메뉴상품요청목록);
        음수_가격_메뉴요청 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(-1), 1L, 메뉴상품요청목록);
        메뉴그룹_없는_메뉴요청 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(16_000), null, 메뉴상품요청목록);
        메뉴상품_가격합을_넘는_메뉴요청 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(50_000), null, 메뉴상품요청목록);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(두마리메뉴));
        when(productRepository.findById(any())).thenReturn(Optional.of(후라이드));
        when(menuRepository.save(any())).thenReturn(후라이드치킨);

        MenuResponse result = menuService.create(메뉴요청);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getMenuProducts()).hasSize(1),
                () -> assertThat(result.getMenuProducts().get(0).getSeq()).isEqualTo(후라이드치킨상품.getSeq())
        );
    }

    @DisplayName("메뉴 가격이 없으면(null) 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(두마리메뉴));
        when(productRepository.findById(any())).thenReturn(Optional.of(후라이드));

        Assertions.assertThatThrownBy(() -> menuService.create(가격_없는_메뉴요청))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_MENU_PRICE);
    }

    @DisplayName("메뉴 가격이 음수면 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException2() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(두마리메뉴));
        when(productRepository.findById(any())).thenReturn(Optional.of(후라이드));

        Assertions.assertThatThrownBy(() -> menuService.create(음수_가격_메뉴요청))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_MENU_PRICE);
    }

    @DisplayName("메뉴의 메뉴그룹이 존재하지 않으면 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException3() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴그룹_없는_메뉴요청))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.MENU_GROUP_NOT_FOUND);
    }

    @DisplayName("상품에 등록되지 않은 메뉴 상품으로 메뉴를 생성 시 예외가 발생한다.")
    @Test
    void createException4() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(두마리메뉴));
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴요청))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.PRODUCT_NOT_FOUND);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성 시 예외가 발생한다.")
    @Test
    void createException5() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(두마리메뉴));
        when(productRepository.findById(any())).thenReturn(Optional.of(후라이드));

        Assertions.assertThatThrownBy(() -> menuService.create(메뉴상품_가격합을_넘는_메뉴요청))
                .isInstanceOf(MenuPriceGreaterThanAmountException.class)
                .hasMessageStartingWith(ExceptionMessage.MENU_PRICE_GREATER_THAN_AMOUNT);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        when(menuRepository.findAll()).thenReturn(Arrays.asList(후라이드치킨));

        List<MenuResponse> results = menuService.list();

        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getMenuProducts())
                        .containsExactly(MenuProductResponse.from(후라이드치킨상품))
        );
    }
}
