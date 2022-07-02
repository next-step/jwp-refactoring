package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;
    private MenuGroup 메뉴그룹;

    @BeforeEach
    void setUp() {
        메뉴그룹 = new MenuGroup();
        메뉴그룹.setId(1L);
        메뉴그룹.setName("오늘의메뉴");
    }

    @Test
    void 메뉴그룹을_등록할_수_있다() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(메뉴그룹.getName());
        given(menuGroupRepository.save(any())).willReturn(메뉴그룹);

        MenuGroupResponse createdMenuGroup = menuGroupService.create(menuGroupRequest);

        assertAll(
                () -> assertThat(createdMenuGroup.getName()).isEqualTo(메뉴그룹.getName()),
                () -> assertThat(createdMenuGroup).isNotNull()
        );
    }

    @Test
    void 메뉴그룹을_조회할_수_있다() {
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(메뉴그룹));

        List<MenuGroupResponse> 메뉴그룹리스트 = menuGroupService.list();

        assertThat(메뉴그룹리스트).isNotNull();
    }
}
