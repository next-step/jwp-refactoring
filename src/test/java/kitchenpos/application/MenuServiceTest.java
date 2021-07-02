package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 테스트")
class MenuServiceTest {

    @Mock
    private JdbcTemplateMenuDao menuDao;

    private Menu menu;

    @BeforeEach
    void setup() {
        menu = new Menu();
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setName("순대국");
        menu.setPrice(BigDecimal.valueOf(8000.0));
        menu.setMenuProducts(new ArrayList<>(Arrays.asList(new MenuProduct())));
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given

        // when
        when(menuDao.save(menu)).thenReturn(menu);
        Menu savedMenu = menuDao.save(menu);
        // then
        assertThat(savedMenu).isNotNull();
        assertThat(savedMenu.getId()).isEqualTo(1L);
    }

    @DisplayName("조회")
    @Test
    void findAll() {
        // given

        // when
        when(menuDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(menu)));
        List<Menu> menus = menuDao.findAll();
        // then
        assertThat(menus.size()).isEqualTo(1);
        assertThat(menus.get(0).getId()).isEqualTo(1L);
    }
}