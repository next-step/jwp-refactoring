package kitchenpos.menu.application;

import static kitchenpos.menu.MenuTestFixture.감자튀김_FIXTURE;
import static kitchenpos.menu.MenuTestFixture.메뉴_상품_FIXTURE;
import static kitchenpos.menu.MenuTestFixture.메뉴_상품_FIXTURE2;
import static kitchenpos.menu.MenuTestFixture.치킨_메뉴_FIXTURE;
import static kitchenpos.menu.MenuTestFixture.치킨_메뉴_FIXTURE2;
import static kitchenpos.menu.MenuTestFixture.후라이드_치킨_FIXTURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.menu.application.validator.MenuCreationValidator;
import kitchenpos.menu.application.validator.MenuPriceValidator;
import kitchenpos.menu.application.validator.MenuValidator;
import kitchenpos.menu.application.validator.MenuValidatorGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    private MenuGroupRepository menuGroupRepository;
    private ProductService productService;
    private MenuRepository menuRepository;
    private MenuValidator menuCreationValidator;
    private MenuValidator menuPriceValidator;
    private MenuValidatorGroup validatorGroup;

    private MenuService menuService;

    private MenuRequest 치킨_메뉴;

    private Product 후라이드_치킨;
    private Product 감자튀김;
    private MenuProduct 테스트_메뉴_항목;

    private Menu 치킨_메뉴_entity;

    @BeforeEach
    void setUp() {
        후라이드_치킨 = Product.of(1L, 후라이드_치킨_FIXTURE.getName(), 후라이드_치킨_FIXTURE.getPrice());
        감자튀김 = Product.of(2L, 감자튀김_FIXTURE.getName(), 감자튀김_FIXTURE.getPrice());

        치킨_메뉴 = 치킨_메뉴_FIXTURE;
        테스트_메뉴_항목 = MenuProduct.of(1L, 1);
        치킨_메뉴_entity = Menu.of(치킨_메뉴.getName(), 치킨_메뉴.getPrice(), null, Collections.singletonList(테스트_메뉴_항목));

        menuGroupRepository = mock(MenuGroupRepository.class);
        productService = mock(ProductService.class);
        menuRepository = mock(MenuRepository.class);
        menuCreationValidator = new MenuCreationValidator(menuGroupRepository, productService);
        menuPriceValidator = new MenuPriceValidator(productService);

        validatorGroup = new MenuValidatorGroup(menuCreationValidator, menuPriceValidator);
        menuService = new MenuService(menuRepository, validatorGroup);
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create_test() {
        // given
        치킨_메뉴 = 치킨_메뉴_FIXTURE;
        when(menuGroupRepository.existsById(치킨_메뉴.getMenuGroupId()))
            .thenReturn(true);
        when(productService.findByIdIn(Arrays.asList(후라이드_치킨.getId(), 감자튀김.getId())))
            .thenReturn(Arrays.asList(후라이드_치킨, 감자튀김));
        when(productService.findById(메뉴_상품_FIXTURE.getProductId()))
            .thenReturn(후라이드_치킨);
        when(productService.findById(메뉴_상품_FIXTURE2.getProductId()))
            .thenReturn(감자튀김);
        when(menuRepository.save(any()))
            .thenReturn(치킨_메뉴_entity);

        // when
        MenuResponse result = menuService.create(치킨_메뉴);

        // then
        assertAll(
            () -> assertThat(result.getName()).isEqualTo(치킨_메뉴.getName()),
            () -> assertThat(result.getPrice()).isEqualTo(치킨_메뉴.getPrice())
        );
    }

    @DisplayName("메뉴 등록시 메뉴의 그룹이 존재하지 없으면 예외가 발생한다")
    @Test
    void create_exception_test() {
        // given
        치킨_메뉴 = new MenuRequest("test", BigDecimal.valueOf(500L), 1L, null);
        when(menuGroupRepository.existsById(치킨_메뉴.getMenuGroupId()))
            .thenReturn(false);

        // then
        assertThatThrownBy(() -> {
            menuService.create(치킨_메뉴);
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_EXIST_MENU.getMessage());
    }

    @DisplayName("메뉴 등록시 메뉴의 금액이 없으면 예외가 발생한다")
    @Test
    void create_exception_test2() {
        치킨_메뉴 = new MenuRequest("test", null, 1L, Collections.emptyList());
        when(menuGroupRepository.existsById(치킨_메뉴.getMenuGroupId()))
            .thenReturn(true);
        when(productService.findByIdIn(anyList()))
            .thenReturn(Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            menuService.create(치킨_메뉴);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.INVALID_PRICE.getMessage());
    }

    @DisplayName("메뉴 등록시 메뉴의 금액이 0 미만이면 예외가 발생한다")
    @Test
    void create_exception_test3() {
        치킨_메뉴 = new MenuRequest("test", BigDecimal.valueOf(-300L), 1L, Collections.emptyList());
        when(menuGroupRepository.existsById(치킨_메뉴.getMenuGroupId()))
            .thenReturn(true);
        when(productService.findByIdIn(anyList()))
            .thenReturn(Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            menuService.create(치킨_메뉴);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.INVALID_PRICE.getMessage());
    }

    @DisplayName("메뉴 등록시 등록할 메뉴의 상품이 존재하지 않으면 예외가 발생한다")
    @Test
    void create_exception_test4() {
        // given
        치킨_메뉴 = new MenuRequest("test", BigDecimal.valueOf(300L), 1L, Arrays.asList(메뉴_상품_FIXTURE, 메뉴_상품_FIXTURE2));
        when(menuGroupRepository.existsById(치킨_메뉴.getMenuGroupId()))
            .thenReturn(true);
        when(productService.findByIdIn(Arrays.asList(메뉴_상품_FIXTURE.getProductId(), 메뉴_상품_FIXTURE2.getProductId())))
            .thenReturn(Collections.singletonList(후라이드_치킨));

        // then
        assertThatThrownBy(() -> {
            menuService.create(치킨_메뉴);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.CONTAINS_NOT_EXIST_PRODUCT.getMessage());
    }

    @DisplayName("등록할 메뉴의 상품들의 금액의 합보다 메뉴의 금액이 더 크면 예외가 발생한다")
    @Test
    void create_exception_test5() {
        // given
        치킨_메뉴 = 치킨_메뉴_FIXTURE2;
        when(menuGroupRepository.existsById(치킨_메뉴.getMenuGroupId()))
            .thenReturn(true);
        when(productService.findByIdIn(Arrays.asList(후라이드_치킨.getId(), 감자튀김.getId())))
            .thenReturn(Arrays.asList(후라이드_치킨, 감자튀김));
        when(productService.findById(메뉴_상품_FIXTURE.getProductId()))
            .thenReturn(후라이드_치킨);
        when(productService.findById(메뉴_상품_FIXTURE2.getProductId()))
            .thenReturn(감자튀김);

        // then
        assertThatThrownBy(() -> {
            menuService.create(치킨_메뉴);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.IS_NOT_OVER_THAN_MENU_PRICE.getMessage());
    }

    @DisplayName("모든 메뉴 목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(menuRepository.findAllMenuAndProducts())
            .thenReturn(Collections.singletonList(치킨_메뉴_entity));

        // when
        List<MenuResponse> result = menuService.list();

        // then
        assertThat(result).hasSize(1);
    }
}
