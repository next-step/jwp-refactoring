package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("메뉴 그룹 관리 기능")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴그룹이 정상적으로 생성된다.")
    void createMenuGroup() {
        MenuGroup created = this.menuGroupService.create(new MenuGroup("menu_group"));

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("menu_group");
    }

    @Test
    @DisplayName("메뉴그룹을 모두 조회한다.")
    void findAll() {
        MenuGroup 메뉴_그룹_A = this.menuGroupRepository.save(new MenuGroup("메뉴_그룹_A"));
        MenuGroup 메뉴_그룹_B = this.menuGroupRepository.save(new MenuGroup("메뉴_그룹_B"));

        List<MenuGroup> list = this.menuGroupService.list();

        assertThat(list).containsExactly(메뉴_그룹_A, 메뉴_그룹_B);
    }

}
