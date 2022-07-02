package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@DisplayName("메뉴 관련")
@SpringBootTest
class MenuServiceTest {
    @Autowired
    MenuService menuService;
    @MockBean
    MenuRepository menuRepository;
    @MockBean
    MenuValidator menuValidator;

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        // given
        Menu menu = new Menu("메뉴", 19000, 1L);
        menu.addMenuProduct(new MenuProduct(1L, 2));
        menu.addMenuProduct(new MenuProduct(2L, 1));

        // when
        menuService.create(menu);

        // then
        verify(menuValidator).checkMenuGroup(menu);
        verify(menuValidator).checkPrice(menu);
        verify(menuRepository).save(menu);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        menuService.list();

        // then
        verify(menuRepository).findAllWithMenuProduct();
    }
}
