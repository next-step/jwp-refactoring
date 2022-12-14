package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그륩을 등록한다")
    void createMenuGroup() {
        MenuGroup 피자 = new MenuGroup(1L, "피자");
        when(menuGroupDao.save(any())).thenReturn(피자);

        MenuGroup result = menuGroupService.create(피자);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("피자");
    }

    @Test
    @DisplayName("메뉴 그륩 리스트를 받아온다")
    void getMenuGroupList() {
        MenuGroup 피자 = new MenuGroup(1L, "피자");
        MenuGroup 치킨 = new MenuGroup(2L, "치킨");


        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(피자, 치킨));

        List<MenuGroup> result = menuGroupService.list();

        assertThat(result).hasSize(2);
        assertThat(result).contains(치킨, 피자);
    }
}
