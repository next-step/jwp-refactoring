package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    public static final MenuGroup 햄버거_메뉴 =  new MenuGroup();

    static {
        햄버거_메뉴.setName("햄버거 메뉴");
    }

    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        given(menuGroupDao.save(any()))
                .willReturn(new MenuGroup());
        // when
        final MenuGroup menuGroup = menuGroupService.create(햄버거_메뉴);
        // then
        assertThat(menuGroup).isInstanceOf(MenuGroup.class);
    }

    @Test
    void list() {
        // given
        given(menuGroupDao.findAll())
                .willReturn(Arrays.asList(햄버거_메뉴));
        // when
        final List<MenuGroup> list = menuGroupService.list();
        // then
        assertThat(list).hasSize(1);
    }
}
