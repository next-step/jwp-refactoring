package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.port.MenuGroupPort;
import kitchenpos.port.MenuPort;
import kitchenpos.port.ProductPort;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuPort menuPort;
    @Mock
    private MenuGroupPort menuGroupPort;
    @Mock
    private ProductPort productPort;

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
        스테이크 = new Product(1L, new Price(BigDecimal.valueOf(30_000)), "스테이크");
        파스타 = new Product(2L, new Price(BigDecimal.valueOf(15_000)), "파스타");
        스파빅그륩 = new MenuGroup("피자");
        스테이크_파스타_빅세트 = new Menu("스테이크_파스타_빅세트", new Price(BigDecimal.valueOf(45_000)), 스파빅그륩);

        스테이크_이인분 = new MenuProduct(스테이크_파스타_빅세트, 파스타, 2);
        파스타_삼인분 = new MenuProduct(스테이크_파스타_빅세트, 파스타, 3);
    }


    @Test
    @DisplayName("메뉴를 생성할 수 있다")
    void createMenu() {
        given(productPort.findAllByIdIn(any())).willReturn(Arrays.asList(스테이크, 파스타));
        given(menuPort.save(any())).willReturn(스테이크_파스타_빅세트);

        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.valueOf(3_000), 스파빅그륩.getId(), productRequest);

        MenuResponse result = menuService.create(request);

        assertThat(result.getId()).isEqualTo(스테이크_파스타_빅세트.getId());
        assertThat(result.getName()).isEqualTo(스테이크_파스타_빅세트.getName());
        assertThat(result.getMenuGroupId()).isEqualTo(스테이크_파스타_빅세트.getMenuGroup().getId());
        assertThat(result.getPrice()).isEqualTo(스테이크_파스타_빅세트.getPrice().getPrice());
    }

    @Test
    @DisplayName("메뉴의 가격은 0원 이상이다")
    void createMenuPriceZeroException() {
        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.ZERO, 스파빅그륩.getId(), productRequest);

        assertThatThrownBy(() ->
                menuService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그륩에 메뉴는 존재해야 한다")
    void createMenuPresentMenuGroup() {
        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.valueOf(45_000), 스파빅그륩.getId(), productRequest);

        assertThatThrownBy(() ->
                menuService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품은 모두 등록해 있어야 한다")
    void createMenuAllPresent() {
        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.valueOf(45_000), 스파빅그륩.getId(), productRequest);

        given(productPort.findAllByIdIn(any())).willReturn(Arrays.asList());

        assertThatThrownBy(() ->
                menuService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격은 모든 상품의 합 이하야야한다")
    void createMenuIsAllProductSumMin() {
        스테이크_파스타_빅세트 = new Menu("스테이크_파스타_빅세트", new Price(BigDecimal.valueOf(200_000)), 스파빅그륩);
        given(menuGroupPort.findById(스테이크_파스타_빅세트.getMenuGroup().getId())).willReturn(스파빅그륩);
        given(productPort.findAllByIdIn(any())).willReturn(Arrays.asList(스테이크));
        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.valueOf(3_000), 스파빅그륩.getId(), productRequest);

        assertThatThrownBy(() ->
                menuService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 리스트를 받아 올 수 있다")
    void getMenuList() {
        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 2L), new MenuProductRequest(파스타.getId(), 3L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", BigDecimal.valueOf(90_000), 스파빅그륩.getId(), productRequest);

        Menu 스테이크_파스타_빅세트 = new Menu("스테이크_파스타_빅세트", new Price(BigDecimal.valueOf(90_000)), 스파빅그륩);
        Menu 스테이크_세트 = new Menu("스테이크_세트", new Price(BigDecimal.valueOf(90_000)), 스파빅그륩);

        given(menuPort.findAll()).willReturn(Arrays.asList(스테이크_파스타_빅세트, 스테이크_세트));

        List<MenuResponse> result = menuService.list();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo(스테이크_파스타_빅세트.getName());
        assertThat(result.get(1).getName()).isEqualTo(스테이크_세트.getName());
    }
}
