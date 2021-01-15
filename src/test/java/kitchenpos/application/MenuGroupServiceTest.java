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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroup 치킨;
    private MenuGroup 음료;
    private MenuGroup 식사;

    private List<MenuGroup> 메뉴그룹리스트 = new ArrayList<>();
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    public void setUp() {
        치킨 = new MenuGroup("치킨");
        음료 = new MenuGroup("음료");
        식사 = new MenuGroup("식사");

        메뉴그룹리스트.add(치킨);
        메뉴그룹리스트.add(음료);
        메뉴그룹리스트.add(식사);
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴그룹 등록 테스트")
    @Test
    void createMenuGroup() {
        mockSaveMenuGroup(치킨);

        MenuGroup resultMenuGroup = menuGroupService.create(치킨);
        assertThat(resultMenuGroup.getName()).isEqualTo(치킨.getName());
    }

    @DisplayName("메뉴그룹 조회 테스트")
    @Test
    void findMenuGroupList() {
        when(menuGroupDao.findAll()).thenReturn(메뉴그룹리스트);

        List<MenuGroup> resultMenuGroups = menuGroupService.list();
        List<String> menuGroupNames = resultMenuGroups.stream()
                .map(menuGroup -> menuGroup.getName())
                .collect(Collectors.toList());
        List<String> expectedMenuGroupNames = 메뉴그룹리스트.stream()
                .map(menuGroup -> menuGroup.getName())
                .collect(Collectors.toList());

        assertThat(resultMenuGroups.size()).isEqualTo(메뉴그룹리스트.size());
        assertThat(menuGroupNames).containsExactlyElementsOf(expectedMenuGroupNames);
    }

    private void mockSaveMenuGroup(MenuGroup menuGroup) {
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);
    }

}
