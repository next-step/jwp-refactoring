package kitchenpos.menu.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceTest extends BaseServiceTest {
    @Autowired
    private MenuService menuService;

    /**
     * Menu: 후라이드_치킨_메뉴
     *  ㄴ MenuGroup: 한마리_메뉴_그룹
     *  ㄴ Price: 16,000
     *  ㄴ MenuProducts:
     *      1. MenuProduct 후라이드_1개
     *          ㄴ Price: 16000
     **/
    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        // given
        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록된_product_id, 1));
        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(16000),
                등록된_menuGroup_id, menuProducts);

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(menuResponse.getId()).isEqualTo(menuRequest.getId()),
                () -> assertThat(menuResponse.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(menuRequest.getPrice()),
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId()),
                () -> assertThat(menuResponse.getMenuProductResponses().get(0).getMenuId()).isEqualTo(menuResponse.getId())
        );
    }

    @DisplayName("메뉴 가격을 설정 안했을 경우 등록하지 못한다.")
    @Test
    void createMenuException1() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록된_product_id, 1));
        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", null, 등록된_menuGroup_id, menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격이 없거나 음수입니다.");
    }

    @DisplayName("메뉴 가격이 0보다 작을 경우 등록하지 못한다.")
    @Test
    void createMenuException2() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록된_product_id, 1));
        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(-1),
                등록된_menuGroup_id, menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격이 없거나 음수입니다.");
    }

    @DisplayName("메뉴 그룹이 등록되어 있지 않을 경우 등록하지 못한다.")
    @Test
    void createMenuException3() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록된_product_id, 1));
        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(16000),
                등록되어_있지_않은_menuGroup_id, menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("해당 메뉴 그룹을 찾을 수가 없습니다.");
    }

    @DisplayName("등록되어 있지 않은 상품으로 만들어진 메뉴 상품이 있으면 등록할 수 없다.")
    @Test
    void createMenuException4() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록되어_있지_않은_product_id, 1));
        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(16000),
                등록된_menuGroup_id, menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("등록되지 않은 상품이 있습니다.");
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 총 금액보다 크면 등록하지 못한다.")
    @Test
    void createMenuException5() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록된_product_id, 1));
        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(17000),
                등록된_menuGroup_id, menuProducts);

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품들의 총 합보다 작아야 합니다.");
    }
}
