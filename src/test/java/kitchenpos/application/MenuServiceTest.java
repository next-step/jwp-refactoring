package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.port.MenuPort;
import kitchenpos.port.MenuGroupPort;
import kitchenpos.port.MenuProductPort;
import kitchenpos.port.ProductPort;
import kitchenpos.domain.menu.Menu;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuPort menuDao;
    @Mock
    private MenuGroupPort menuGroupPort;
    @Mock
    private MenuProductPort menuProductPort;
    @Mock
    private ProductPort productPort;

    @InjectMocks
    private MenuService menuService;

    private Product 스테이크;
    private Product 파스타;
    private MenuProduct 스테이크_이인분;
    private MenuProduct 파스타_삼인분;

    private Menu 스테이크_파스타_빅세트;

    private MenuGroup 스피빅그륩;


    @BeforeEach
    void setUp() {

        스테이크 = Product.of((BigDecimal.valueOf(30_000)), "스테이크");
        파스타 = Product.of(BigDecimal.valueOf(15_000), "파스타");
        스피빅그륩 = MenuGroup.from("피자");
        스테이크_파스타_빅세트 = Menu.of("스테이크_파스타_빅세트", Price.from(BigDecimal.valueOf(45_000)), 스피빅그륩, Arrays.asList(스테이크_이인분, 파스타_삼인분));

        스테이크_이인분 = MenuProduct.of(스테이크_파스타_빅세트, 파스타, 2);
        파스타_삼인분 = MenuProduct.of(스테이크_파스타_빅세트, 파스타, 3);
    }


    @Test
    @DisplayName("메뉴를 생성할 수 있다")
    void createMenu() {
        when(menuGroupPort.existsById(any())).thenReturn(true);
        when(productPort.findById(any())).thenReturn(Optional.of(스테이크));
        when(productPort.findById(any())).thenReturn(Optional.of(파스타));
        when(menuDao.save(any())).thenReturn(스테이크_파스타_빅세트);
        when(menuProductPort.save(any())).thenReturn(스테이크_이인분);
        when(menuProductPort.save(any())).thenReturn(파스타_삼인분);

        List<MenuProductRequest> productRequest = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L), new MenuProductRequest(파스타.getId(), 1L));
        MenuRequest request = new MenuRequest("스테이크_파스타_빅세트", Price.from(BigDecimal.valueOf(3_000)), 스피빅그륩.getId(), productRequest);

        Menu result = menuService.create(request);

        assertThat(result.getId()).isEqualTo(스테이크_파스타_빅세트.getId());
        assertThat(result.getName()).isEqualTo(스테이크_파스타_빅세트.getName());
        assertThat(result.getMenuGroupId()).isEqualTo(스테이크_파스타_빅세트.getMenuGroupId());
        assertThat(result.getPrice()).isEqualTo(스테이크_파스타_빅세트.getPrice());
    }

    @Test
    @DisplayName("메뉴의 가격은 0원 이상이다")
    void createMenuPriceZeroException() {
        Menu 스테이크_파스타_빅세트 = new Menu(1L, "스테이크_파스타_빅세트", BigDecimal.ZERO, 1L, Arrays.asList(스테이크_이인분, 파스타_삼인분));

        assertThatThrownBy(() ->
                menuService.create(스테이크_파스타_빅세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그륩에 메뉴는 존재해야 한다")
    void createMenuPresentMenuGroup() {
        Menu 스테이크_파스타_빅세트 = new Menu(1L, "스테이크_파스타_빅세트", BigDecimal.valueOf(45_000), 1L, Arrays.asList(스테이크_이인분, 파스타_삼인분));

        when(menuGroupPort.existsById(any())).thenReturn(false);

        assertThatThrownBy(() ->
                menuService.create(스테이크_파스타_빅세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품은 모두 등록해 있어야 한다")
    void createMenuAllPresent() {
        Menu 스테이크_파스타_빅세트 = new Menu(1L, "스테이크_파스타_빅세트", BigDecimal.valueOf(45_000), 1L, Arrays.asList(스테이크_이인분, 파스타_삼인분));

        when(menuGroupPort.existsById(any())).thenReturn(true);
        when(productPort.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                menuService.create(스테이크_파스타_빅세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격은 모든 상품의 합 이하야야한다")
    void createMenuIsAllProductSumMin() {
        Menu 스테이크_파스타_빅세트 = new Menu(1L, "스테이크_파스타_빅세트", BigDecimal.valueOf(90_000), 1L, Arrays.asList(스테이크_이인분, 파스타_삼인분));

        when(menuGroupPort.existsById(any())).thenReturn(true);
        when(productPort.findById(any())).thenReturn(Optional.of(스테이크));
        when(productPort.findById(any())).thenReturn(Optional.of(파스타));

        assertThatThrownBy(() ->
                menuService.create(스테이크_파스타_빅세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 리스트를 받아 올 수 있다")
    void getMenuList() {
        Menu 스테이크_파스타_빅세트 = new Menu(1L, "스테이크_파스타_빅세트", BigDecimal.valueOf(90_000), 1L, Arrays.asList(스테이크_이인분, 파스타_삼인분));
        Menu 스테이크_세트 = new Menu(2L, "스테이크_세트", BigDecimal.valueOf(90_000), 2L, Arrays.asList(스테이크_이인분));

        when(menuDao.findAll()).thenReturn(Arrays.asList(스테이크_파스타_빅세트, 스테이크_세트));

        List<Menu> result = menuService.list();

        assertThat(result).hasSize(2);
        assertThat(result).contains(스테이크_세트, 스테이크_파스타_빅세트);
    }
}
