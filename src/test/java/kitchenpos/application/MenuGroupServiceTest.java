package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void create() {
        //given
        String name = "menuGroup";
        MenuGroup request = new MenuGroup(name);

        long id = 1L;
        given(menuGroupDao.save(request)).willReturn(new MenuGroup(id, name));

        //when
        MenuGroup menuGroup = menuGroupService.create(request);

        //then
        assertAll(
                () -> assertEquals(id, menuGroup.getId()),
                () -> assertEquals(name, menuGroup.getName())
        );
    }

    @Test
    void list() {
        //given
        long id = 1L;
        String name = "menuGroup";
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(new MenuGroup(id, name)));

        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertEquals(1, menuGroups.size());
    }
}