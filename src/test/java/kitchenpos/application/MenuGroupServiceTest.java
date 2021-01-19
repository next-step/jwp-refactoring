package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static kitchenpos.utils.TestHelper.등록되어_있지_않은_menuGroup_id;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    public void createMenuGroup() {
        MenuGroup menuGroup = MenuGroup.of(등록되어_있지_않은_menuGroup_id, "핫메뉴");

        MenuGroup result = menuGroupService.create(menuGroup);

        assertThat(result).isEqualTo(menuGroup);
    }
}
