package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    public void create() {
        //given
        MenuGroup 추천메뉴 = new MenuGroup("추천메뉴");
        given(menuGroupDao.save(any())).willReturn(추천메뉴);

        //when
        MenuGroup actual = menuGroupService.create(추천메뉴);

        //then
        assertThat(actual.getName()).isEqualTo(추천메뉴.getName());
    }

    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    @Test
    void list() {
        //given
        MenuGroup 추천메뉴 = new MenuGroup("추천메뉴");
        MenuGroup 오늘의메뉴 = new MenuGroup("오늘의메뉴");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천메뉴, 오늘의메뉴));

        //when
        List<MenuGroup> list = menuGroupService.list();

        //then
        assertThat(list).contains(추천메뉴, 오늘의메뉴);
    }
}
