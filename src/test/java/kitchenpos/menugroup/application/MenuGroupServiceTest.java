package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.utils.fixture.MenuGroupFixtureFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.utils.fixture.MenuGroupFixtureFactory.*;
import static kitchenpos.utils.fixture.MenuGroupFixtureFactory.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup 양식_메뉴;
    private MenuGroup 한식_메뉴;

    @BeforeEach
    void setUp() {
        양식_메뉴 = createMenuGroup( 1L, "양식메뉴");
        한식_메뉴 = createMenuGroup(2L, "한식메뉴");
    }

    @DisplayName("메뉴그룹을 등록할 수 있다")
    @Test
    void 메뉴그룹_등록(){
        //given
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(양식_메뉴);

        //when
        MenuGroupRequest menuGroupRequest = MenuGroupRequest.from(양식_메뉴.getName());
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(menuGroupResponse).isEqualTo(MenuGroupResponse.from(양식_메뉴));
    }

    @DisplayName("메뉴그룹의 목록을 조회할 수 있다")
    @Test
    void 메뉴그룹_목록_조회(){
        //given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(양식_메뉴, 한식_메뉴));

        //when
        List<MenuGroupResponse> list = menuGroupService.list();

        //then
        assertThat(list).containsExactly(MenuGroupResponse.from(양식_메뉴), MenuGroupResponse.from(한식_메뉴));
    }
}