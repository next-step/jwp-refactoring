package kitchenpos.domain.menugroup;

import kitchenpos.domain.menugroup.domain.MenuGroup;
import kitchenpos.domain.menugroup.infra.JpaMenuGroupRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MenuGroupTest {

    @Autowired
    private JpaMenuGroupRepository menuGroupRepository;


    @DisplayName("메뉴 그룹은 아이디와 이름으로 구성되어 있다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = MenuGroup.of("추천메뉴");
        // when
        final MenuGroup actual = menuGroupRepository.save(menuGroup);
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertTrue(actual.matchName("추천메뉴"))
        );
    }

    @ParameterizedTest(name = "이름은 빈값이 될 수 없다. \"{0}\"")
    @NullAndEmptySource
    void nameEmpty(String name) {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> MenuGroup.of(name);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
