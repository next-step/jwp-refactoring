package kitchenpos.menu.domian;

import kitchenpos.JpaEntityTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹 관련 도메인 테스트")
public class MenuGroupTest extends JpaEntityTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴그룹 생성 테스트")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup("한마리치킨");

        // when
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        // then
        assertThat(savedMenuGroup).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("한마리치킨");
    }
}
