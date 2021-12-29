package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupRequest 메뉴그룹_요청;
    private MenuGroup 메뉴그룹;
    private List<MenuGroup> 메뉴그룹_목록;

    @BeforeEach
    void setUp() {
        메뉴그룹_요청 = MenuGroupRequest.from("추천메뉴");
        메뉴그룹 = MenuGroup.of(1L, "추천메뉴");
        메뉴그룹_목록 = Collections.singletonList(메뉴그룹);
    }

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void createTest() {
        given(menuGroupRepository.save(any())).willReturn(메뉴그룹);

        final MenuGroupResponse actual = menuGroupService.create(메뉴그룹_요청);

        assertThat(actual.getId()).isEqualTo(메뉴그룹.getId());
        assertThat(actual.getName()).isEqualTo(메뉴그룹.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void listTest() {
        given(menuGroupRepository.findAll()).willReturn(메뉴그룹_목록);

        final List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual.size()).isEqualTo(메뉴그룹_목록.size());
    }
}
