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

import static kitchenpos.fixture.MenuGroupFixture.*;
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
        MenuGroup request = 메뉴묶음_요청데이터_생성(name);

        Long id = 1L;
        given(menuGroupDao.save(request)).willReturn(new MenuGroup(id, name));

        //when
        MenuGroup menuGroup = menuGroupService.create(request);

        //then
        메뉴묶음_확인(menuGroup, id, name);
    }

    @Test
    void list() {
        //given
        Long id = 1L;
        String name = "menuGroup";
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(메뉴묶음_데이터_생성(id, name)));

        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertEquals(1, menuGroups.size());
        MenuGroup menuGroup = menuGroups.get(0);
        메뉴묶음_확인(menuGroup, id, name);
    }
}