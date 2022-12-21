package kitchenpos.menu.domian;

import kitchenpos.MenuApplication;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(classes = MenuApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("메뉴그룹 관련 도메인 테스트")
public class MenuGroupTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
    }

    @DisplayName("메뉴그룹 생성 테스트")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup("한마리치킨");

        // when
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        // then
        assertThat(savedMenuGroup).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("한마리치킨");
    }
}
