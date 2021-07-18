package kitchenpos.menugroup.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        final MenuGroupRequest menuGroup = new MenuGroupRequest();
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(new MenuGroup("name"));

        // when
        menuGroupService.create(menuGroup);

        // then
        verify(menuGroupRepository).save(any(MenuGroup.class));
    }

    @Test
    void list() {
        // when
        menuGroupService.list();

        // then
        verify(menuGroupRepository).findAll();
    }
}
