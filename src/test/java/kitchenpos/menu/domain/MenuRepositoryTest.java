package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void save() {
        // given
        final Price price = new Price(BigDecimal.ZERO);
        final Menu menu = new Menu("name", price, 1L);

        // when
        final Menu actual = menuRepository.save(menu);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(menu.getName());
        assertThat(actual.getPrice()).isEqualTo(menu.getPrice());
        assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
    }
}
