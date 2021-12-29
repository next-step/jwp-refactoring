package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MenuRepositoryTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("메뉴를 등록한다.")
    @Test
    void saveMenu() {
        final MenuGroup menuGroup = MenuGroup.from("메뉴그룹");
        final MenuGroup persistMenuGroup = menuGroupRepository.save(menuGroup);
        final Menu menu = Menu.of("메뉴", new BigDecimal("1000"), persistMenuGroup);

        final Menu actual = menuRepository.save(menu);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(actual.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(actual.getPrice())
        );
    }

    @DisplayName("등록한 메뉴를 조회한다.")
    @Test
    void findMenu() {
        final MenuGroup menuGroup = MenuGroup.from("메뉴그룹");
        final MenuGroup persistMenuGroup = menuGroupRepository.save(menuGroup);
        final Menu menu = Menu.of("메뉴", new BigDecimal("1000"), persistMenuGroup);
        final Menu expected = menuRepository.save(menu);

        final Menu actual = menuRepository.findById(expected.getId()).get();

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(actual.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(actual.getPrice())
        );
    }

    @DisplayName("등록되어 있는 메뉴 목록을 조회한다.")
    @Test
    void findAllMenu() {
        final List<Menu> actual = menuRepository.findAll();
        assertAll(
                 () -> assertThat(actual).hasSizeGreaterThan(0)
        );
    }
}
