package kitchenpos.menu.application;

import kitchenpos.application.MenuService;
import kitchenpos.menu.dao.FakeMenuDao;
import kitchenpos.menu.dao.FakeMenuGroupDao;
import kitchenpos.menu.dao.FakeMenuProductDao;
import kitchenpos.product.dao.FakeProductDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.ServiceTestFactory.HONEY_RED_COMBO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuServiceTest {
    private final MenuService menuService = new MenuService(new FakeMenuDao(),
            new FakeMenuGroupDao(),
            new FakeMenuProductDao(),
            new FakeProductDao());

    @Test
    @DisplayName("가격이 0원 미만인 경우, 예외를 반환한다.")
    void createWithInvalidPrice() {
        //given
        HONEY_RED_COMBO.setPrice(BigDecimal.valueOf(-1L));

        assertThatThrownBy(() -> {
            menuService.create(HONEY_RED_COMBO);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면, 예외를 반환한다.")
    void createWithNotExistingMenuGroup() {
        assertThatThrownBy(() -> {
            menuService.create(HONEY_RED_COMBO);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상품이 존재하지 않으면, 예외를 반환한다.")
    void createWithNotExistingMenuProducts() {
        HONEY_RED_COMBO.setMenuGroupId(1L);
        assertThatThrownBy(() -> {
            menuService.create(HONEY_RED_COMBO);
        }).isInstanceOf(NullPointerException.class);
    }
}
