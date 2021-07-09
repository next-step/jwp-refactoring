package kitchenpos.menugroup;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuGroupIntegrationTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupRepository.save(new MenuGroup("테스트메뉴그룹1"));
        menuGroupRepository.save(new MenuGroup("테스트메뉴그룹2"));
    }


    @DisplayName("메뉴 그룹 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("메뉴그룹");

        // when
        MenuGroupResponse actual = menuGroupService.create(request);

        // then
        assertThat(actual).isNotNull()
                          .extracting(MenuGroupResponse::getName)
                          .isEqualTo("메뉴그룹");
    }

    @DisplayName("전체 메뉴 그룹 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertThat(actual).isNotEmpty()
                          .hasSizeGreaterThanOrEqualTo(2);
    }

}
