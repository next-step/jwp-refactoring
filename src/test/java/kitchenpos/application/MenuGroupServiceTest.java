package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class 용 {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 한식;
    private MenuGroup 중식;
    private MenuGroup 양식;

    @BeforeEach
    void before() {
        한식 = MenuGroupFixtureFactory.create("한식");
        중식 = MenuGroupFixtureFactory.create("중식");
        양식 = MenuGroupFixtureFactory.create("양식");
    }

    @DisplayName("메뉴 그룹을 생성 할 수 있다.")
    @Test
    void createTest() {

        //given
        MenuGroupRequest 저장할_메뉴그룹 = MenuGroupRequest.from( "메뉴그룹1");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(한식);

        //when
        MenuGroupResponse menuGroup = menuGroupService.create(저장할_메뉴그룹);

        //then
        assertThat(menuGroup).isEqualTo(MenuGroupResponse.from(한식));
    }

    @DisplayName("메뉴 그릅의 목록을 조회 할 수 있다.")
    @Test
    void listTest() {

        //given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(한식, 중식, 양식));

        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).containsExactly(
                MenuGroupResponse.from(한식),
                MenuGroupResponse.from(중식),
                MenuGroupResponse.from(양식)
        );
    }
}
