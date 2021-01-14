package kitchenpos.menu.service;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.MenuProduct;
import kitchenpos.infra.Money;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceJpaTest extends IntegrationTest {

    @Autowired
    MenuServiceJpa menuServiceJpa;

    @DisplayName("메뉴에 상품을 여러개 등록할 수 있다.")
    @Test
    void createMenu() {
        MenuRequest menuRequest = new MenuRequest("후라이드+양념", 31000, 1L, getMenuProducts(
                new MenuProductRequest(1L, 1),
                new MenuProductRequest(2L, 1)));
        MenuResponse menuResponse = menuServiceJpa.create(menuRequest);

        assertThat(menuResponse.getMenuProducts()).hasSize(2);
        assertThat(menuResponse.getPrice()).isEqualTo(31000);
    }

    private List<MenuProductRequest> getMenuProducts(MenuProductRequest... menuProductRequests) {
        return Arrays.asList(
                menuProductRequests
        );
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void isNotCollectMenuPrice() {
        assertAll(
                () -> assertThatThrownBy(() -> {
                    MenuRequest menuRequest = new MenuRequest("후라이드+양념", -1, 1L, getMenuProducts(
                            new MenuProductRequest(1L, 1)));
                    menuServiceJpa.create(menuRequest);
                }).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> {
                    MenuRequest menuRequest = new MenuRequest("후라이드+양념", 30000, 1L, new ArrayList<>());
                    menuServiceJpa.create(menuRequest);
                }).isInstanceOf(IllegalArgumentException.class));
    }

    @DisplayName("메뉴의 가격이 메뉴의 상품 가격 * 수량보다 작아야 한다.")
    @Test
    void existsMenuGroup() {
        assertThatThrownBy(() -> {
            MenuRequest menuRequest = new MenuRequest("후라이드+양념", 35000, 1L, getMenuProducts(
                    new MenuProductRequest(1L, 1),
                    new MenuProductRequest(2L, 1)));
            menuServiceJpa.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}