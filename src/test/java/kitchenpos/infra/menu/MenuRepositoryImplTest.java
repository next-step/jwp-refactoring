package kitchenpos.infra.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuRepositoryImplTest {
    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("일관되게 Menu를 저장할 수 있다.")
    @Test
    void saveTest() {
        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(1L, 1L), MenuProduct.of(1L, 1L));
        Menu menu = Menu.of("menu", BigDecimal.ONE, 1L, menuProducts);

        // when
        Menu saved = menuRepository.save(menu);

        assertThat(saved.getId()).isNotNull();
        saved.getMenuProducts().forEach(it -> {
            assertThat(it.getSeq()).isNotNull();
            assertThat(it.getMenuId()).isNotNull();
        });
    }

    @DisplayName("일관되게 Menu 목록을 불러 올 수 있다.")
    @Test
    void listTest() {
        // when
        List<Menu> menus = menuRepository.findAll();

        // then
        assertThat(menus.size()).isNotEqualTo(0);
        assertThat(menus.get(0).getId()).isNotNull();
        menus.get(0).getMenuProducts().forEach(it -> assertThat(it.getSeq()).isNotNull());
    }
}