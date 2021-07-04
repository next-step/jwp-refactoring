package kitchenpos.application;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;


    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();

        // when
        menuGroupService.create(menuGroup);

        // then
        verify(menuGroupDao).save(menuGroup);
    }

    @Test
    void list() {
        // when
        menuGroupService.list();

        // then
        verify(menuGroupDao).findAll();
    }
}
