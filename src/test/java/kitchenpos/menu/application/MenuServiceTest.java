package kitchenpos.menu.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.application.validator.MenuCreationValidator;
import kitchenpos.menu.application.validator.MenuPriceValidator;
import kitchenpos.menu.application.validator.MenuValidator;
import kitchenpos.menu.application.validator.MenuValidatorGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductService productService;
    @Mock
    private MenuRepository menuRepository;

    private MenuValidator menuCreationValidator;
    private MenuValidator menuPriceValidator;
    private MenuValidatorGroup menuValidatorGroup;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuCreationValidator = new MenuCreationValidator(menuGroupRepository, productService);
        menuPriceValidator = new MenuPriceValidator(productService);
        menuValidatorGroup = new MenuValidatorGroup(Arrays.asList(menuCreationValidator, menuPriceValidator));
        menuService = new MenuService(menuRepository, menuValidatorGroup);
    }

    @DisplayName("상품 금액의 합보다 메뉴 금액이 더 크면 예외를 던진다.")
    @Test
    void create() {
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L ,1);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L ,1);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(200L), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        Product product1 = new Product(1L, "product-1", BigDecimal.valueOf(150L));
        Product product2 = new Product(2L, "product-2", BigDecimal.valueOf(150L));
        Menu menu = new Menu(1L, "치킨",  BigDecimal.valueOf(200L), 1L,new ArrayList<>());

        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productService.findByIdIn(anyList())).willReturn(Arrays.asList(product1, product2));
        given(productService.findByProductId(1L)).willReturn(product1);
        given(productService.findByProductId(2L)).willReturn(product2);
        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        MenuResponse response = menuService.create(menuRequest);

        assertThat(response.getName()).isEqualTo("치킨");
    }

    @DisplayName("메뉴의 가격이 0원 이상이 아니라면 예외를 던진다.")
    @Test
    void createWithInvalidPrice() {
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L ,1);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L ,1);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(-1L), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        Product product1 = new Product(1L, "product-1", BigDecimal.valueOf(50L));
        Product product2 = new Product(2L, "product-2", BigDecimal.valueOf(50L));

        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productService.findByIdIn(anyList())).willReturn(Arrays.asList(product1, product2));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_PRICE.getMessage());
    }

    @DisplayName("메뉴 그룹에 등록되지 않았다면 예외를 던진다.")
    @Test
    void createWihInvalidMeuGroupId() {
        MenuGroup menuGroup = new MenuGroup(1000L, "테스트");
        Menu menu = new Menu(1000L, null, BigDecimal.valueOf(1000L), menuGroup.getId(), null);

        MenuRequest menuRequest = new MenuRequest(menu.getName(), menu.getPrice().getValue(), menu.getMenuGroupId(), Arrays.asList());

        given(menuGroupRepository.existsById(menuGroup.getId())).willReturn(Boolean.FALSE);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.MENU_GROUP_NOT_FOUND.getMessage());
    }

    @DisplayName("상품이 존재하지 않는다면 예외를 던진다.")
    @Test
    void createWihInvalidProductId() {
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L ,1);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L ,1);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(-1L), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        given(menuGroupRepository.existsById(1L)).willReturn(Boolean.TRUE);
        given(productService.findByIdIn(anyList())).willReturn(new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @DisplayName("상품 금액의 합보다 메뉴 금액이 더 크면 예외를 던진다.")
    @Test
    void createWithInvalidSum() {
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(1L ,1);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(2L ,1);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(200L), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        Product product1 = new Product(1L, "product-1", BigDecimal.valueOf(50L));
        Product product2 = new Product(2L, "product-2", BigDecimal.valueOf(50L));

        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productService.findByIdIn(anyList())).willReturn(Arrays.asList(product1, product2));
        given(productService.findByProductId(1L)).willReturn(product1);
        given(productService.findByProductId(2L)).willReturn(product2);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.INVALID_MENU_PRICE.getMessage());
    }

    @Test
    void list() {
        Menu menu = new Menu(1L, "양념치킨", BigDecimal.valueOf(20000L), 1L, new ArrayList<>());
        given(menuRepository.findAllWithMenuProducts()).willReturn(Arrays.asList(menu));

        List<MenuResponse> response = menuService.list();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getName()).isEqualTo("양념치킨");
    }
}
