package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void create() {
        //given
        MenuGroup request = menuGroupCreateRequest("두마리메뉴");

        //when
        menuGroupService.create(request);

        //then
        ArgumentCaptor<MenuGroup> captor = ArgumentCaptor.forClass(MenuGroup.class);
        verify(menuGroupDao, only()).save(captor.capture());
        assertThat(captor.getValue())
            .extracting(MenuGroup::getName)
            .isEqualTo(request.getName());
    }

    @Test
    @DisplayName("메뉴 그룹들을 조회할 수 있다.")
    void list() {
        //when
        menuGroupService.list();

        //then
        verify(menuGroupDao, only()).findAll();
    }

    private MenuGroup menuGroupCreateRequest(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
