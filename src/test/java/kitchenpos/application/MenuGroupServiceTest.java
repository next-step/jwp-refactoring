package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void createMenuGroup() {
        //given
        MenuGroup menuGroup = new MenuGroup("양식");
        given(menuGroupDao.save(menuGroup)).willReturn(menuGroup);

        //when
        MenuGroup saveMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(saveMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회 한다.")
    void retriedMenuGroups() {
        //given
        MenuGroup 양식 = new MenuGroup("양식");
        MenuGroup 일식 = new MenuGroup("일식");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(양식, 일식));

        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertAll("메뉴 그룹 목록을 조회 한다.",
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups).extracting("name").contains("양식", "일식")
        );

    }

}