package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.utils.fixture.MenuGroupFixtureFactory.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
@DisplayName("메뉴그룹 Service 테스트")
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    MenuGroupService menuGroupService;

    private MenuGroup 양식_메뉴;
    private MenuGroup 한식_메뉴;

    @BeforeEach
    void setUp() {
        양식_메뉴 = createMenuGroup( "양식메뉴");
        한식_메뉴 = createMenuGroup("한식메뉴");
    }

    @DisplayName("메뉴그룹을 등록할 수 있다")
    @Test
    void 메뉴그룹_등록(){
        //when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(MenuGroupRequest.from(양식_메뉴.getName()));

        //then
        Assertions.assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(양식_메뉴.getName())
        );
    }

    @DisplayName("메뉴그룹의 목록을 조회할 수 있다")
    @Test
    void 메뉴그룹_목록_조회(){
        //given
        MenuGroupResponse savedMenuGroup = menuGroupService.create(MenuGroupRequest.from(양식_메뉴.getName()));

        //when
        List<MenuGroupResponse> list = menuGroupService.list();

        //then
        assertThat(list).contains(savedMenuGroup);
    }
}