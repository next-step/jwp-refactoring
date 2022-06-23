package kitchenpos.menu.application;

import static kitchenpos.ServiceTestFactory.HONEY_MENU_PRODUCT;
import static kitchenpos.ServiceTestFactory.NOT_EXISTS_MENU_PRODUCT;
import static kitchenpos.ServiceTestFactory.RED_MENU_PRODUCT;
import static kitchenpos.ServiceTestFactory.createMenuBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dao.FakeMenuDao;
import kitchenpos.menu.dao.FakeMenuGroupDao;
import kitchenpos.menu.dao.FakeMenuProductDao;
import kitchenpos.product.dao.FakeProductDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class MenuServiceTest {
    private final MenuService menuService = new MenuService(new FakeMenuDao(),
            new FakeMenuGroupDao(),
            new FakeMenuProductDao(),
            new FakeProductDao());
    private Menu honeyRedCombo;

    @BeforeEach
    void setUp() {
        honeyRedCombo = createMenuBy(1L, "허니레드콤보", 39_000L);
    }

    @Test
    @DisplayName("가격이 0원 미만인 경우, 예외를 반환한다.")
    void createWithInvalidPrice() {
        //given
        honeyRedCombo.setPrice(BigDecimal.valueOf(-1L));

        assertThatThrownBy(() -> {
            menuService.create(honeyRedCombo);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면, 예외를 반환한다.")
    void createWithNotExistingMenuGroup() {
        assertThatThrownBy(() -> {
            menuService.create(honeyRedCombo);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품이 존재하지 않으면, 예외를 반환한다.")
    void createWithNotExistingMenuProducts() {
        //given
        honeyRedCombo.setMenuGroupId(1L);
        assertThatThrownBy(() -> {
            menuService.create(honeyRedCombo);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("메뉴 상품 중에 상품 등록이 되어있지 않으면 예외를 반환한다.")
    void createWithNotExistingProduct() {
        //given
        honeyRedCombo.setMenuGroupId(1L);
        honeyRedCombo.setMenuProducts(Arrays.asList(HONEY_MENU_PRODUCT, NOT_EXISTS_MENU_PRODUCT));
        //when, then
        assertThatThrownBy(() -> {
            menuService.create(honeyRedCombo);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품 가격 총합보다 크면 예외를 반환한다.")
    void createOverPrice() {
        //given
        honeyRedCombo.setMenuGroupId(1L);
        honeyRedCombo.setMenuProducts(Arrays.asList(HONEY_MENU_PRODUCT, RED_MENU_PRODUCT));
        honeyRedCombo.setPrice(BigDecimal.valueOf(40_000L));
        //when, then
        assertThatThrownBy(() -> {
            menuService.create(honeyRedCombo);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록한다.")
    void createMenu() {
        //given
        honeyRedCombo.setMenuGroupId(1L);
        honeyRedCombo.setMenuProducts(Arrays.asList(HONEY_MENU_PRODUCT, RED_MENU_PRODUCT));
        //when
        Menu actual = menuService.create(honeyRedCombo);
        //then
        assertThat(actual.getMenuProducts()).containsExactlyElementsOf(Arrays.asList(HONEY_MENU_PRODUCT, RED_MENU_PRODUCT));
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void findAll() {
        //given
        honeyRedCombo.setMenuGroupId(1L);
        honeyRedCombo.setMenuProducts(Arrays.asList(HONEY_MENU_PRODUCT, RED_MENU_PRODUCT));
        menuService.create(honeyRedCombo);
        //when
        List<Menu> menus = menuService.list();
        //then
        assertThat(menus).hasSize(1);
    }
}
