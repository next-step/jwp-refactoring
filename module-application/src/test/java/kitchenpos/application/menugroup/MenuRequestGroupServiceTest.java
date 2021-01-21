package kitchenpos.application.menugroup;

import kitchenpos.common.BaseTest;
import kitchenpos.domain.menugroup.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹의 비즈니스 로직을 처리하는 서비스 테스트")
class MenuRequestGroupServiceTest extends BaseTest {
    private static final String 순살파닭두마리메뉴 = "순살파닭두마리메뉴";
    private static final String 두마리메뉴 = "두마리메뉴";
    private static final String 한마리메뉴 = "한마리메뉴";
    private static final String 신메뉴 = "신메뉴";

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    public void 메뉴_그룹_생성() {
        final MenuGroup newMenuGroup = new MenuGroup();
        newMenuGroup.setName(순살파닭두마리메뉴);

        final MenuGroup createdMenuGroup = menuGroupCreate(newMenuGroup);

        assertThat(createdMenuGroup.getId()).isNotNull();
        assertThat(createdMenuGroup.getName()).isEqualTo(순살파닭두마리메뉴);
    }

    private MenuGroup menuGroupCreate(final MenuGroup newMenuGroup) {
        return menuGroupService.create(newMenuGroup);
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    public void 메뉴_그룹_조회() {
        final MenuGroup firstMenuGroup = new MenuGroup();
        firstMenuGroup.setName(두마리메뉴);
        final MenuGroup secondMenuGroup = new MenuGroup();
        secondMenuGroup.setName(한마리메뉴);
        final MenuGroup thirdMenuGroup = new MenuGroup();
        thirdMenuGroup.setName(순살파닭두마리메뉴);
        final MenuGroup fourthMenuGroup = new MenuGroup();
        fourthMenuGroup.setName(신메뉴);

        menuGroupCreate(firstMenuGroup);
        menuGroupCreate(secondMenuGroup);
        menuGroupCreate(thirdMenuGroup);
        menuGroupCreate(fourthMenuGroup);

        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isGreaterThan(3);
        assertThat(menuGroups.get(0).getId()).isNotNull();
        assertThat(menuGroups.get(0).getName()).isEqualTo("두마리메뉴");
        assertThat(menuGroups.get(1).getId()).isNotNull();
        assertThat(menuGroups.get(1).getName()).isEqualTo("한마리메뉴");
        assertThat(menuGroups.get(2).getId()).isNotNull();
        assertThat(menuGroups.get(2).getName()).isEqualTo("순살파닭두마리메뉴");
        assertThat(menuGroups.get(3).getId()).isNotNull();
        assertThat(menuGroups.get(3).getName()).isEqualTo("신메뉴");
    }
}
