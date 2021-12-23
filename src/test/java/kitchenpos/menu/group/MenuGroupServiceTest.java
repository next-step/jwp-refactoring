package kitchenpos.menu.group;


import kitchenpos.application.MenuGroupService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {

        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("중화요리");

        when(menuGroupDao.save(any())).thenReturn(menuGroup);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(savedMenuGroup).isNotNull();
        assertThat(savedMenuGroup.getId()).isEqualTo(menuGroup.getId());
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());

    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void getMenuGroups() {

        //given
        List<MenuGroup> menuGroups = new ArrayList<>();

        MenuGroup menuGroupA = new MenuGroup();
        menuGroupA.setId(1L);
        menuGroupA.setName("중화요리");

        MenuGroup menuGroupB = new MenuGroup();
        menuGroupB.setId(2L);
        menuGroupB.setName("파스타");

        menuGroups.add(menuGroupA);
        menuGroups.add(menuGroupB);

        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        //when
        List<MenuGroup> findMenuGroups = menuGroupService.list();

        //then
        assertThat(findMenuGroups).isNotEmpty();
        assertThat(findMenuGroups.size()).isEqualTo(2);
        assertThat(findMenuGroups).extracting(MenuGroup::getName).containsExactly("중화요리", "파스타");
    }


}
