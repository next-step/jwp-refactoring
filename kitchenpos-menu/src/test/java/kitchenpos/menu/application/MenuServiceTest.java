package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.exception.BadRequestException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.utils.Message.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    private Product honeycombo;
    private MenuProduct honeycomboChickenProduct;
    private Menu honeycomboChicken;
    private MenuRequest menuRequest;
    private MenuRequest emptyPriceRequest;
    private MenuRequest underZeroPriceMenuRequest;
    private MenuRequest noMenuGroupRequest;
    private MenuRequest overPriceMenuRequest;

    @BeforeEach
    void setUp() {
        honeycombo = Product.of(1L, "honeycombo", BigDecimal.valueOf(18000));
        honeycomboChickenProduct = MenuProduct.of(honeycombo.getId(), 2);
        honeycomboChicken = Menu.of(1L, "honeycomboChicken", BigDecimal.valueOf(18000), 1L, Arrays.asList(honeycomboChickenProduct));

        List<MenuProductRequest> menuProductRequest = Arrays.asList(MenuProductRequest.of(1L, 2));
        menuRequest = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(16_000), 1L, menuProductRequest);
        emptyPriceRequest = MenuRequest.of("honeycomboChicken", null, 1L, menuProductRequest);
        underZeroPriceMenuRequest = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(-1), 1L, menuProductRequest);
        noMenuGroupRequest = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(16_000), null, menuProductRequest);
        overPriceMenuRequest = MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(50_000), null, menuProductRequest);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        doNothing().when(menuValidator).validate(any());
        when(productRepository.findById(any())).thenReturn(Optional.of(honeycombo));
        when(menuRepository.save(any())).thenReturn(honeycomboChicken);

        MenuResponse result = menuService.create(menuRequest);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getMenuProducts()).hasSize(1),
                () -> assertThat(result.getMenuProducts().get(0).getSeq()).isEqualTo(honeycomboChickenProduct.getSeq())
        );
    }

    @DisplayName("메뉴 가격이 없으면(null) 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException() {
        doNothing().when(menuValidator).validate(any());
        when(productRepository.findById(any())).thenReturn(Optional.of(honeycombo));

        Assertions.assertThatThrownBy(() -> menuService.create(emptyPriceRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_PRICE);
    }

    @DisplayName("메뉴 가격이 음수면 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException2() {
        doNothing().when(menuValidator).validate(any());
        when(productRepository.findById(any())).thenReturn(Optional.of(honeycombo));

        Assertions.assertThatThrownBy(() -> menuService.create(underZeroPriceMenuRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_PRICE);
    }

    @DisplayName("메뉴의 메뉴그룹이 존재하지 않으면 메뉴 생성 시 예외가 발생한다.")
    @Test
    void createException3() {
        doThrow(new EntityNotFoundException(NOT_EXISTS_MENU))
                .when(menuValidator).validate(any());

        Assertions.assertThatThrownBy(() -> menuService.create(noMenuGroupRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(NOT_EXISTS_MENU);
    }

    @DisplayName("상품에 등록되지 않은 메뉴 상품으로 메뉴를 생성 시 예외가 발생한다.")
    @Test
    void createException4() {
        doThrow(new EntityNotFoundException(NOT_EXISTS_PRODUCT))
                .when(menuValidator).validate(any());

        Assertions.assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(NOT_EXISTS_PRODUCT);

    }

    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성 시 예외가 발생한다.")
    @Test
    void createException5() {
        doThrow(new BadRequestException(INVALID_MENU_PRICE))
                .when(menuValidator).validate(any());

        Assertions.assertThatThrownBy(() -> menuService.create(overPriceMenuRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_MENU_PRICE);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        when(menuRepository.findAll()).thenReturn(Arrays.asList(honeycomboChicken));

        List<MenuResponse> results = menuService.list();

        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getMenuProducts())
                        .containsExactly(MenuProductResponse.from(honeycomboChickenProduct))
        );
    }
}
