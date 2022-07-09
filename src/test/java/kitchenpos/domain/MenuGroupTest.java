package kitchenpos.domain;

import kitchenpos.dao.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class MenuGroupTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void save() {
        // given
        MenuGroup 두마리메뉴 = menuGroupRepository.save(MenuGroup.builder()
                                                            .name("두마리메뉴")
                                                            .build());

        // when
        assertThat(두마리메뉴.getId()).isNotNull();
    }

    @DisplayName("이름을 입력하지 않으면 메뉴 그룹을 생성할 수 없다.")
    @Test
    void save_throwException_givenEmptyName() {
        // when
        // then
        assertThatThrownBy(() -> menuGroupRepository.save(MenuGroup.builder()
                                                                   .build()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("동일한 이름으로 메뉴 그룹을 생성할 수 없다.")
    @Test
    void save_throwException_givenRepeatedNames() {
        // given
        menuGroupRepository.save(MenuGroup.builder()
                                          .name("두마리메뉴")
                                          .build());

        // when
        // then
        assertThatThrownBy(() -> menuGroupRepository.save(MenuGroup.builder()
                                                                   .name("두마리메뉴")
                                                                   .build()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
