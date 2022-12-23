package kitchenpos.unit;

import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.port.MenuPort;
import kitchenpos.product.port.ProductPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.constants.ErrorCodeType.NOT_FOUND_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuPort menuPort;
    @Mock
    private ProductPort productPort;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    private Product 스테이크;
    private Product 파스타;
    private MenuProduct 스테이크_이인분;
    private MenuProduct 파스타_삼인분;

    private Menu 스테이크_파스타_빅세트;

    private MenuGroup 스파빅그륩;


    @BeforeEach
    void setUp() {
        스테이크 = new Product(1L, new ProductPrice(BigDecimal.valueOf(30_000)), "스테이크");
        파스타 = new Product(2L, new ProductPrice(BigDecimal.valueOf(15_000)), "파스타");
        스파빅그륩 = new MenuGroup(1L, "피자");
        스테이크_파스타_빅세트 = new Menu("스테이크_파스타_빅세트", new MenuPrice(BigDecimal.valueOf(45_000)), 스파빅그륩.getId(), new MenuProducts(Arrays.asList()));

        스테이크_이인분 = new MenuProduct(파스타.getId(), 2L);
        파스타_삼인분 = new MenuProduct(파스타.getId(), 3L);
    }


    @Test
    @DisplayName("메뉴를 생성할 수 있다")
    void createMenu() {
        given(productPort.findById(any())).willReturn(스테이크);
        given(productPort.findById(any())).willReturn(파스타);
        given(menuPort.save(any())).willReturn(스테이크_파스타_빅세트);

        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.valueOf(3_000), 스파빅그륩.getId(), productRequest);

        MenuResponse result = menuService.create(request);

        assertThat(result.getId()).isEqualTo(스테이크_파스타_빅세트.getId());
        assertThat(result.getName()).isEqualTo(스테이크_파스타_빅세트.getName());
        assertThat(result.getMenuGroupId()).isEqualTo(스테이크_파스타_빅세트.getMenuGroupId());
        assertThat(result.getPrice()).isEqualTo(스테이크_파스타_빅세트.getPrice().getPrice());
    }

    @Test
    @DisplayName("메뉴의 가격은 0원 이상이다")
    void createMenuPriceZeroException() {
        List<MenuProductRequest> productRequest = Arrays.asList(
                new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L)
        );
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", new BigDecimal(-1), 스파빅그륩.getId(), productRequest);

        given(productPort.findById(any())).willReturn(스테이크);
        given(productPort.findById(any())).willReturn(파스타);


        assertThatThrownBy(() ->
                menuService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격은 모든 상품의 합 이하야야한다")
    void createMenuIsAllProductSumMin() {
        스테이크_파스타_빅세트 = new Menu("스테이크_파스타_빅세트", new MenuPrice(BigDecimal.valueOf(200_000)), 스파빅그륩.getId(), new MenuProducts(Arrays.asList()));
        given(productPort.findById(any())).willReturn(스테이크);

        doThrow(new IllegalArgumentException(NOT_FOUND_ORDER_TABLE.getMessage()))
                .when(menuValidator).validCheckMakeMenu(any());

        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.valueOf(3_000), 스파빅그륩.getId(), productRequest);

        assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 리스트를 받아 올 수 있다")
    void getMenuList() {
        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 2L), new MenuProductRequest(파스타.getId(), 3L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.valueOf(90_000), 스파빅그륩.getId(), productRequest);

        Menu 스테이크_파스타_빅세트 = new Menu("스테이크_파스타_빅세트", new MenuPrice(BigDecimal.valueOf(90_000)), 스파빅그륩.getId(), new MenuProducts(Arrays.asList()));
        Menu 스테이크_세트 = new Menu("스테이크_세트", new MenuPrice(BigDecimal.valueOf(90_000)), 스파빅그륩.getId(), new MenuProducts(Arrays.asList()));

        given(menuPort.findAll()).willReturn(Arrays.asList(스테이크_파스타_빅세트, 스테이크_세트));

        List<MenuResponse> result = menuService.list();

        Assertions.assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo(스테이크_파스타_빅세트.getName());
        assertThat(result.get(1).getName()).isEqualTo(스테이크_세트.getName());
    }
}
