package kitchenpos.application;

import kitchenpos.repository.MenuRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ProductService productService;

    @Mock
    private MenuGroupService menuGroupService;

    @InjectMocks
    private MenuService menuService;

    private final static long ANY_MENU_ID = 1L;
    private final static long ANY_MENU_GROUP_ID = 1L;
    private final static long ANY_PRODUCT_ID = 1L;

    private MenuRequest menuRequest;
    private MenuGroup menuGroup;
    private Menu menu;
    private Product dummyProduct;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.of("menuGroupName");
        ReflectionTestUtils.setField(menuGroup, "id", ANY_MENU_GROUP_ID);

        menuRequest = new MenuRequest("tomato pasta", BigDecimal.ZERO, ANY_MENU_GROUP_ID, new ArrayList<>());
        menu = Menu.of("tomato pasta", BigDecimal.ZERO, menuGroup, new ArrayList<>());

        dummyProduct = Product.of("rice", BigDecimal.valueOf(10L));
        ReflectionTestUtils.setField(dummyProduct, "id", ANY_PRODUCT_ID);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 잇다.")
    void create_test() {
        given(menuGroupService.isExists(menuGroup)).willReturn(false);
        given(menuGroupService.findById(ANY_MENU_GROUP_ID)).willReturn(menuGroup);
        given(menuRepository.save(menu)).willReturn(menu);

        menuService.create(menuRequest);

        verify(menuRepository).save(menu);
    }

    @Test
    @DisplayName("메뉴를 등록하는 시점에 메뉴 그룹이 미리 등록되어 있어야 한다.")
    void menuGroup() {
        given(menuGroupService.isExists(menuGroup)).willReturn(true);
        given(menuGroupService.findById(ANY_MENU_GROUP_ID)).willReturn(menuGroup);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("menuGroup");
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴그룹의 가격보다 높을 경우 등록될 수 없다.")
    void price() {
        given(menuGroupService.isExists(menuGroup)).willReturn(false);
        given(menuGroupService.findById(ANY_MENU_GROUP_ID)).willReturn(menuGroup);
        given(productService.getProduct(1L)).willReturn(dummyProduct);

        menuRequest = new MenuRequest("tomato pasta", BigDecimal.valueOf(100L), ANY_MENU_GROUP_ID,
                Lists.list(new MenuProductRequest(ANY_PRODUCT_ID, 1)));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Total Price");
    }
}