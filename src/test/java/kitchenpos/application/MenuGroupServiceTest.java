package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
    private MenuGroupService service;

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        //given
        MenuGroup 면류 = new MenuGroup("면류");
        when(menuGroupDao.save(면류)).thenReturn(면류);

        //when
        MenuGroup result = service.create(면류);

        //then
        assertThat(result.getName()).isEqualTo(면류.getName());
    }

    @DisplayName("전체 메뉴 그룹 조회")
    @Test
    void list() {
        //given
        MenuGroup 면류 = new MenuGroup("면류");
        MenuGroup 밥류 = new MenuGroup("밥류");
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(면류, 밥류));

        //when
        List<MenuGroup> menuGroups = service.list();

        //then
        assertThat(menuGroups).contains(면류, 밥류);
    }
}
