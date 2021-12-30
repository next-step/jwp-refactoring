package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.fixture.TestMenuGroupFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void createTest() {
        final MenuGroup 메뉴그룹 = TestMenuGroupFactory.메뉴그룹_조회됨(1L, "메뉴그룹");

        given(menuGroupRepository.save(any())).willReturn(메뉴그룹);

        final MenuGroupResponse actual = menuGroupService.create(TestMenuGroupFactory.메뉴그룹생성_요청("메뉴그룹"));

        TestMenuGroupFactory.메뉴그룹_생성_확인됨(actual, 메뉴그룹);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void listTest() {
        final List<MenuGroup> 메뉴_그룹_목록 = TestMenuGroupFactory.메뉴그룹목록_조회됨(10);

        given(menuGroupRepository.findAll()).willReturn(TestMenuGroupFactory.메뉴그룹목록_조회됨(10));

        final List<MenuGroupResponse> actual = menuGroupService.list();

        TestMenuGroupFactory.메뉴그룹_목록_확인됨(actual, 메뉴_그룹_목록);
    }
}
