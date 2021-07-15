package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("기본 저장 확인")
    void save() {
        // given
        Menu menu = new Menu("A", BigDecimal.valueOf(19000.00), 1L);
        menu.addMenuProduct(new MenuProduct(menu, 1L, 1));
        menu.addMenuProduct(new MenuProduct(menu, 2L, 1));


        // when
        Menu save = menuRepository.save(menu);

        // then
        assertThat(save.getId()).isNotNull();
    }

    @Test
    @DisplayName("기본 삭제 확인")
    void delete() {
        // given
        Menu menu = new Menu("A", BigDecimal.valueOf(19000.00), 1L);
        menu.addMenuProduct(new MenuProduct(menu, 1L, 1));
        menu.addMenuProduct(new MenuProduct(menu, 2L, 1));
        Menu save = menuRepository.save(menu);

        // when
        menuRepository.delete(save);

        // then
        assertThat(menuRepository.findById(save.getId())).isEmpty();
    }
}
