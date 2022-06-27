package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴의_가격이_올바르지_않으면_등록할_수_없다() {
        // given
        Menu menu = new Menu("치킨탕수육", BigDecimal.valueOf(-1), 1L, createMenuProducts());

        // when & then
        assertThatThrownBy(() ->
            menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 메뉴_그룹이_저장되어_있지_않으면_등록할_수_없다() {
        // given
        Menu menu = new Menu("치킨탕수육", BigDecimal.valueOf(100), 10L, createMenuProducts());

        // when & then
        assertThatThrownBy(() ->
            menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @Test
    void 메뉴의_가격이_메뉴_상품_가격의_총합보다_높으면_등록할_수_없다() {
        // given
        Menu menu = new Menu("치킨탕수육", BigDecimal.valueOf(17000), 1L, createMenuProducts());

        // when & then
        assertThatThrownBy(() ->
            menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품 가격의 총합보다 높을 수 없습니다.");
    }

    @Test
    void 메뉴를_등록한다() {
        // when
        Menu result = menuService.create(createMenu());

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        Menu saved = menuService.create(createMenu());

        // when
        List<Menu> result = menuService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSizeGreaterThan(0),
                () -> assertThat(result).contains(saved)
        );
    }

    private Menu createMenu() {
        return new Menu("치킨탕수육", BigDecimal.valueOf(15000), 1L, createMenuProducts());
    }

    private List<MenuProduct> createMenuProducts() {
        return Collections.singletonList(new MenuProduct(1L, 1));
    }
}
