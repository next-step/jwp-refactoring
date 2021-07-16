package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    public void 메뉴그룹생성_성공() {
        //given
        MenuGroup menuGroup = mock(MenuGroup.class);
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        verify(menuGroupDao).save(any());

    }

    @Test
    public void 메뉴그룹조회_성공() {
        //given
        given(menuGroupDao.findAll()).willReturn(asList(mock(MenuGroup.class)));

        //when
        List<MenuGroup> menuGroupList = menuGroupService.list();

        //then
        verify(menuGroupDao).findAll();

    }

}