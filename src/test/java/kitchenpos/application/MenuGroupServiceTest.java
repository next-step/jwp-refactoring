package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    MenuGroup 한마리메뉴;
    MenuGroup 두마리메뉴;

    MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        두마리메뉴 = new MenuGroup(2L, "두마리메뉴");

        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void create() {
        //given
        when(menuGroupDao.save(한마리메뉴)).thenReturn(한마리메뉴);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(한마리메뉴);

        //then
        assertThat(savedMenuGroup.getId()).isEqualTo(한마리메뉴.getId());
    }

    @Test
    void list() {
        //given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(한마리메뉴, 두마리메뉴));

        //when, then
        assertThat(menuGroupService.list()).hasSize(2);
    }
}
