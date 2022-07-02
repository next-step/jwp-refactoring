package kitchenpos.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.dto.MenuCreateRequest;
import kitchenpos.menu.ui.dto.MenuProductCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_등록한다() {
        // when
        Menu result = menuService.create(createMenuRequest());

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        Menu saved = menuService.create(createMenuRequest());

        // when
        List<Menu> result = menuService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSizeGreaterThan(0),
                () -> assertThat(result).contains(saved)
        );
    }

    public static MenuCreateRequest createMenuRequest() {
        return new MenuCreateRequest("치킨탕수육", BigDecimal.valueOf(15000), 1L, createMenuProductRequest());
    }

    public static List<MenuProductCreateRequest> createMenuProductRequest() {
        return Collections.singletonList(new MenuProductCreateRequest(1L, 1));
    }
}
