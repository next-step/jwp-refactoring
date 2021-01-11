package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹의 비즈니스 로직을 처리하는 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    public void 메뉴_그룹_생성() {
        final Long menuGroupId = 1L;
        final String menuGroupName = "순살파닭두마리메뉴";

        final MenuGroup expectedMenuGroup = new MenuGroup();
        expectedMenuGroup.setId(menuGroupId);
        expectedMenuGroup.setName(menuGroupName);

        given(menuGroupDao.save(any())).willReturn(expectedMenuGroup);

        final MenuGroup newMenuGroup = new MenuGroup();
        expectedMenuGroup.setName(menuGroupName);

        final MenuGroup createdMenuGroup = menuGroupService.create(newMenuGroup);

        assertThat(createdMenuGroup.getId()).isEqualTo(expectedMenuGroup.getId());
        assertThat(createdMenuGroup.getName()).isEqualTo(menuGroupName);
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    public void 메뉴_그룹_조회() {
        final List<MenuGroup> expectedMenuGroups = new ArrayList<>();
        final MenuGroup firstMenuGroup = new MenuGroup();
        firstMenuGroup.setId(11L);
        firstMenuGroup.setName("순살파닭두마리메뉴");

        final MenuGroup secondMenuGroup = new MenuGroup();
        secondMenuGroup.setId(22L);
        secondMenuGroup.setName("한마리메뉴");

        expectedMenuGroups.add(firstMenuGroup);
        expectedMenuGroups.add(secondMenuGroup);

        given(menuGroupDao.findAll()).willReturn(expectedMenuGroups);

        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.get(0).getId()).isEqualTo(expectedMenuGroups.get(0).getId());
        assertThat(menuGroups.get(0).getName()).isEqualTo(expectedMenuGroups.get(0).getName());
        assertThat(menuGroups.get(1).getId()).isEqualTo(expectedMenuGroups.get(1).getId());
        assertThat(menuGroups.get(1).getName()).isEqualTo(expectedMenuGroups.get(1).getName());
    }
}
