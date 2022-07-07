package kitchenpos.memuGroup.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupTest {

    @Test
    @DisplayName("MenuGroupRequest 으로 MenuGroup 인스턴스를 생성한다")
    void of() {
        // given
        Name name = new Name("메뉴그룹");
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name.value());

        // when
        MenuGroup menuGroup = MenuGroup.of(menuGroupRequest);

        // then
        assertAll(
                () -> assertThat(menuGroup).isNotNull(),
                () -> assertThat(menuGroup.getName()).isEqualTo(name)
        );

    }
}
