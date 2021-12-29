package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.common.price.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuResponses;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup menuGroup;
    private Menu menu;
    private MenuProduct menuProduct;
    private MenuProducts menuProducts;
    private MenuProductRequest menuProductRequest;

    @BeforeEach
    void setUp() {
        menuProductRequest = new MenuProductRequest(1L, 2);
        menuProduct = new MenuProduct(1L, 2);
        menuGroup = new MenuGroup(1L, "menuGroup");
        menuProducts = new MenuProducts(Arrays.asList(menuProductRequest));
        menu = new Menu(1L, "menu", new Price(BigDecimal.ONE), menuGroup, menuProducts);
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        // given
        ID로_메뉴_그룹_조회(Optional.of(menuGroup));
        Mockito.doNothing().when(menuValidator).validate(Mockito.any());
        메뉴_저장();

        MenuRequest request = new MenuRequest("menu", BigDecimal.ONE, 1L, Arrays.asList(menuProductRequest));

        // when
        MenuResponse actual = menuService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getMenuProducts()).hasSize(1),
            () -> assertThat(actual.getMenuProducts().get(0).getProductId()).isEqualTo(1L)
        );
    }

    @DisplayName("메뉴 상품이 없을 시 에러")
    @Test
    void createErrorWhenProductNotExists() {
        // given
        ID로_메뉴_그룹_조회(Optional.of(menuGroup));
        Mockito.doThrow(new KitchenposNotFoundException()).when(menuValidator).validate(Mockito.any());

        MenuRequest request =
            new MenuRequest("name", BigDecimal.ONE, 1L, Arrays.asList(menuProductRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴 상품 가격이 null이면 에러")
    @Test
    void createErrorWhenPriceIsNull() {
        MenuRequest menu = new MenuRequest("name", null, 1L, Arrays.asList(menuProductRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격이 0 미만이면 에러")
    @Test
    void createErrorWhenPriceIsLessThanZero() {
        MenuRequest menu = new MenuRequest("name", BigDecimal.valueOf(-1), 1L, Arrays.asList(menuProductRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 에러 발생")
    @Test
    void createErrorWhenMenuGroupNotExists() {
        // given
        ID로_메뉴_그룹_조회(Optional.empty());

        MenuRequest menu = new MenuRequest("name", BigDecimal.ONE, 1L, Arrays.asList(menuProductRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 조회")
    @Test
    void list() {
        // given
        Menu menu1 = new Menu(1L, "name1", new Price(BigDecimal.ONE), menuGroup, menuProducts);
        Menu menu2 = new Menu(2L, "name2", new Price(BigDecimal.ONE), menuGroup, menuProducts);
        List<Menu> menus = Arrays.asList(menu1, menu2);
        Mockito.when(menuRepository.findAll())
            .thenReturn(menus);

        // when
        MenuResponses actual = menuService.list();

        // given
        assertAll(
            () -> assertThat(actual.getMenuResponses().get(0).getMenuProducts()).hasSize(1),
            () -> assertThat(actual.getMenuResponses().get(0).getMenuProducts().get(0))
                .isEqualTo(MenuProductResponse.from(menuProduct))
        );
    }

    private void 메뉴_저장() {
        Mockito.when(menuRepository.save(Mockito.any()))
            .thenReturn(menu);
    }

    private void ID로_메뉴_그룹_조회(Optional<MenuGroup> menuGroup) {
        Mockito.when(menuGroupRepository.findById(Mockito.anyLong()))
            .thenReturn(menuGroup);
    }
}