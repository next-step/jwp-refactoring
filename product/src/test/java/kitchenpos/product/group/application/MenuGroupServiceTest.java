package kitchenpos.product.group.application;

import static kitchenpos.product.group.sample.MenuGroupSample.두마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kichenpos.common.domain.Name;
import kitchenpos.product.group.domain.MenuGroup;
import kitchenpos.product.group.domain.MenuGroupCommandService;
import kitchenpos.product.group.domain.MenuGroupQueryService;
import kitchenpos.product.group.ui.request.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    @Mock
    private MenuGroupCommandService commandService;
    @Mock
    private MenuGroupQueryService queryService;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void create() {
        //given
        MenuGroupRequest request = new MenuGroupRequest("두마리메뉴");

        MenuGroup 두마리메뉴 = 두마리메뉴();
        when(commandService.save(any())).thenReturn(두마리메뉴);

        //when
        menuGroupService.create(request);

        //then
        ArgumentCaptor<MenuGroup> captor = ArgumentCaptor.forClass(MenuGroup.class);
        verify(commandService, only()).save(captor.capture());
        assertThat(captor.getValue())
            .extracting(MenuGroup::name)
            .isEqualTo(Name.from(request.getName()));
    }

    @Test
    @DisplayName("메뉴 그룹들을 조회할 수 있다.")
    void list() {
        //when
        menuGroupService.list();

        //then
        verify(queryService, only()).findAll();
    }
}
