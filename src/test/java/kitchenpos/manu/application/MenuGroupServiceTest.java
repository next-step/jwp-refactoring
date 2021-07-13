package kitchenpos.manu.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
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
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    private MenuGroupRequest 중식_요청;
    private MenuGroupRequest 양식_요청;
    private MenuGroup 중식;
    private MenuGroup 양식;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    public void setUp() {
        // given
        // 메뉴 그룹 생성됨
        중식_요청 = new MenuGroupRequest("중식");
        양식_요청 = new MenuGroupRequest("양식");

        중식 = new MenuGroup("중식");
        양식 = new MenuGroup("양식");
    }

    @Test
    @DisplayName("메뉴 그룹 등록 테스트")
    void 메뉴_그룹_등록_테스트() {
        // given
        when(menuGroupRepository.save(중식)).thenReturn(중식);

        // when
        // 메뉴 그룹을 등록 요청
        MenuGroupResponse expected = menuGroupService.create(중식_요청);

        // then
        // 메뉴 그릅 등록 됨
        assertThat(expected.getName()).isEqualTo(중식.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 리스트 조회 테스트")
    void 메뉴_그룹_리스트_조회_테스트() {
        // when
        // 메뉴 그룹 리스트 조회 요청 함
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(중식, 양식));
        List<MenuGroupResponse> expected = menuGroupService.list();

        // then
        // 메뉴 그릅 등록 됨
        assertThat(expected.size()).isEqualTo(2);
        assertThat(expected).containsAll(Arrays.asList(MenuGroupResponse.of(중식), MenuGroupResponse.of(양식)));
    }
}
