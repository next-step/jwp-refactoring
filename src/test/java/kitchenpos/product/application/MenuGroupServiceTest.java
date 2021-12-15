package kitchenpos.product.application;

import static kitchenpos.product.sample.MenuGroupSample.두마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kitchenpos.common.domain.Name;
import kitchenpos.product.domain.MenuGroup;
import kitchenpos.product.domain.MenuGroupRepository;
import kitchenpos.product.ui.request.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void create() {
        //given
        MenuGroupRequest request = new MenuGroupRequest("두마리메뉴");

        MenuGroup 두마리메뉴 = 두마리메뉴();
        when(menuGroupRepository.save(any())).thenReturn(두마리메뉴);

        //when
        menuGroupService.create(request);

        //then
        ArgumentCaptor<MenuGroup> captor = ArgumentCaptor.forClass(MenuGroup.class);
        verify(menuGroupRepository, only()).save(captor.capture());
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
        verify(menuGroupRepository, only()).findAll();
    }
}
