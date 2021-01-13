package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;
    private MenuGroup menuGroup3;

    private List<MenuGroup> menuGroups = new ArrayList<>();
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    public void setUp() {
        menuGroup1 = new MenuGroup("치킨");
        menuGroup2 = new MenuGroup("음료");
        menuGroup3 = new MenuGroup("식사");

        menuGroups.add(menuGroup1);
        menuGroups.add(menuGroup2);
        menuGroups.add(menuGroup3);
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴그룹 등록 테스트")
    @Test
    void createMenuGroup() {
        mockSaveMenuGroup(menuGroup1);
        mockSaveMenuGroup(menuGroup2);
        mockSaveMenuGroup(menuGroup3);
        checkMenuGroup(menuGroupService.create(menuGroup1), menuGroup1);
        checkMenuGroup(menuGroupService.create(menuGroup2), menuGroup2);
        checkMenuGroup(menuGroupService.create(menuGroup3), menuGroup3);
    }

    @DisplayName("메뉴그룹 조회 테스트")
    @Test
    void findMenuGroupList() {
        when(menuGroupDao.findAll()).thenReturn(menuGroups);
        assertThat(menuGroupService.list().size()).isEqualTo(menuGroups.size());
        List<String> menuGroupNames = menuGroupService.list().stream()
                .map(menuGroup -> menuGroup.getName())
                .collect(Collectors.toList());
        List<String> expectedMenuGroupNames = menuGroups.stream()
                .map(menuGroup -> menuGroup.getName())
                .collect(Collectors.toList());
        assertThat(menuGroupNames).containsExactlyElementsOf(expectedMenuGroupNames);
    }

    private void checkMenuGroup(MenuGroup ResultMenuGroup, MenuGroup menuGroup) {
        assertThat(ResultMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    private void mockSaveMenuGroup(MenuGroup menuGroup) {
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);
    }

}