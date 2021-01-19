package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
public class MenuGroupRepositoryTest {
    @Autowired
    MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴그룹 등록")
    @Test
    void create() {
        MenuGroup expected = new MenuGroup("후라이드 메뉴");
        MenuGroup actual = menuGroupRepository.save(expected);
        assertThat(expected == actual).isTrue();
    }
}
