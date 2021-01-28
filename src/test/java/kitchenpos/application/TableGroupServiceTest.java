package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("table group 생성")
    void table_create_test() {
        //given
        TableGroup tableGroupRequest = TABLE_GROUP_REQUEST_생성();
        //when
        TableGroup createdTableGroup = TABLE_GROUP_생성_테스트(tableGroupRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdTableGroup.getId()).isNotNull();
        });
    }


    private TableGroup TABLE_GROUP_생성_테스트(TableGroup tableGroupRequest) {
        return tableGroupService.create(tableGroupRequest);
    }

    private TableGroup TABLE_GROUP_REQUEST_생성() {
        TableGroup tableGroup = new TableGroup();
        return tableGroup;
    }
}
