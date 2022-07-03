package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.dto.MenuCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static kitchenpos.application.MenuServiceTest.createMenuProductRequest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
@SpringBootTest
class MenuCreatorTest {
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴의_가격이_올바르지_않으면_등록할_수_없다() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("치킨탕수육", BigDecimal.valueOf(-1), 1L, createMenuProductRequest());

        // when & then
        assertThatThrownBy(() ->
                menuService.create(request)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 메뉴_그룹이_저장되어_있지_않으면_등록할_수_없다() {
        // given
        MenuCreateRequest menu = new MenuCreateRequest("치킨탕수육", BigDecimal.valueOf(100), 10L, createMenuProductRequest());

        // when & then
        assertThatThrownBy(() ->
                menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @Test
    void 메뉴의_가격이_메뉴_상품_가격의_총합보다_높으면_등록할_수_없다() {
        // given
        MenuCreateRequest menu = new MenuCreateRequest("치킨탕수육", BigDecimal.valueOf(17000), 1L, createMenuProductRequest());

        // when & then
        assertThatThrownBy(() ->
                menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품 가격의 총합보다 높을 수 없습니다.");
    }
}
