package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@DisplayName("메뉴 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuService menuService;
    private Menu menu;
    private MenuRequest menuRequest;
    private MenuProduct menuProduct;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository);
        menuProduct = new MenuProduct(1L, new Menu(), new Product(), 1);
        menuProduct2 = new MenuProduct(1L, new Menu(), new Product(), 2);
        menu = new Menu(1L, "메뉴이름1", 1000, new MenuGroup(), Lists.newArrayList(menuProduct, menuProduct2));
        menuRequest = new MenuRequest(1L, "메뉴이름1", 1000, 1L, Lists.newArrayList(menuProduct, menuProduct2));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenuTest() {
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(new MenuGroup()));
        when(menuRepository.save(any())).thenReturn(menu);

        // when
        MenuResponse createdMenu = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(createdMenu.getId()).isEqualTo(1L),
                () -> assertThat(createdMenu.getName()).isEqualTo("메뉴이름1")
        );
    }

    @DisplayName("메뉴의 가격은 -가 될 수 없다.")
    @Test
    void createMenuPriceNegativeExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final MenuRequest menuPriceNegative = new MenuRequest(1L, "메뉴 가격 -", -100, null, null);

            // when
            menuService.create(menuPriceNegative);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재해야 한다.")
    @Test
    void createMenuHasMenuGroupExceptionTest() {
        assertThatThrownBy(() -> {
            //given
            final MenuRequest MenuGroupNull = new MenuRequest(1L, "테스트 메뉴", 0, null, null);

            // when
            menuService.create(MenuGroupNull);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 상품들의 총 가격의 합보다 커야한다.")
    @Test
    void createMenuSumBiggerThanPriceExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final MenuRequest MenuSumZero = new MenuRequest(1L, "메뉴이름1", 1000, 1L, new ArrayList<>());

            // when
            menuService.create(MenuSumZero);

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void getMenusTest() {
        when(menuRepository.findAll()).thenReturn(Lists.newArrayList(menu));

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus.get(0).getName()).isEqualTo("메뉴이름1");
    }
}