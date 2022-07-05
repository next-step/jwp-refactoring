package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        MenuRequest menu = new MenuRequest("메뉴", 19000, 1L);
        menu.setMenuProducts(Arrays.asList(new MenuProductRequest(1L, 2), new MenuProductRequest(2L, 1)));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu.toEntity());

        // when
        MenuResponse createMenu = menuService.create(menu);

        // then
        verify(menuValidator).checkMenuGroup(menu);
        verify(menuValidator).checkPrice(menu);
        assertThat(createMenu.getName()).isEqualTo(menu.getName());
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
