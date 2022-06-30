package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴의_가격이_올바르지_않으면_등록할_수_없다() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("치킨탕수육", BigDecimal.valueOf(-1), 1L, createMenuProducts());

        // when & then
        assertThatThrownBy(() ->
            menuService.create(request)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 메뉴_그룹이_저장되어_있지_않으면_등록할_수_없다() {
        // given
        MenuCreateRequest menu = new MenuCreateRequest("치킨탕수육", BigDecimal.valueOf(100), 10L, createMenuProducts());

        // when & then
        assertThatThrownBy(() ->
            menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @Test
    void 메뉴의_가격이_메뉴_상품_가격의_총합보다_높으면_등록할_수_없다() {
        // given
        MenuCreateRequest menu = new MenuCreateRequest("치킨탕수육", BigDecimal.valueOf(17000), 1L, createMenuProducts());

        // when & then
        assertThatThrownBy(() ->
            menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품 가격의 총합보다 높을 수 없습니다.");
    }

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

    private MenuCreateRequest createMenuRequest() {
        return new MenuCreateRequest("치킨탕수육", BigDecimal.valueOf(15000), 1L, createMenuProducts());
    }

    private List<MenuProductCreateRequest> createMenuProducts() {
        return Collections.singletonList(new MenuProductCreateRequest(1L, 1));
    }
}
