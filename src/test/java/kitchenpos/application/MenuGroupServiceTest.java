package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;
    MenuGroup 추천메뉴그룹;
    MenuGroup 베스트메뉴그룹;

    @BeforeEach
    void setUp() {
        추천메뉴그룹 = new MenuGroup();
        추천메뉴그룹.setName("추천메뉴그룹");
        베스트메뉴그룹 = new MenuGroup();
        베스트메뉴그룹.setName("베스트메뉴그룹");
    }

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void create() {
        //given
        when(menuGroupDao.save(any())).thenReturn(추천메뉴그룹);

        //when
        MenuGroup createdMenuGroup = menuGroupService.create(추천메뉴그룹);

        //then
        assertThat(createdMenuGroup.getName()).isEqualTo(추천메뉴그룹.getName());
    }

    @Test
    @DisplayName("전체 메뉴그룹을 조회한다.")
    void list() {
        //given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(추천메뉴그룹, 베스트메뉴그룹));

        //when
        List<MenuGroup> foundMenuGroups = menuGroupService.list();

        //then
        assertThat(foundMenuGroups).containsExactly(추천메뉴그룹, 베스트메뉴그룹);
    }
}