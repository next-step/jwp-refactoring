package kitchenpos.menu.application;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupRequest;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;


    @Test
    void create() {
        // given
        MenuGroupRequest menuGroup = new MenuGroupRequest();

        // when
        menuGroupService.create(menuGroup);

        // then
        verify(menuGroupDao).save(any(MenuGroup.class));
    }

    @Test
    void list() {
        // when
        menuGroupService.list();

        // then
        verify(menuGroupDao).findAll();
    }
}
