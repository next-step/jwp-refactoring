package kitchenpos.table;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TableGroupTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;


    @Test
    @DisplayName("테이블 그룹을 등록합니다.")
    void save() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.changeCreatedDate(LocalDateTime.now());

        // when
        TableGroup persistTableGroup = this.tableGroupRepository.save(tableGroup);

        // then
        assertThat(persistTableGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹ID로 특정 테이블 그룹을 조회합니다")
    void findById() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.changeCreatedDate(LocalDateTime.now());
        TableGroup persistTableGroup = this.tableGroupRepository.save(tableGroup);

        // when
        TableGroup foundTableGroup = this.tableGroupRepository.findById(persistTableGroup.getId()).get();

        // then
        assertThat(foundTableGroup.getId()).isEqualTo(persistTableGroup.getId());
    }

    @Test
    @DisplayName("전체 테이블 그룹을 조회합니다.")
    void findAll() {
        // given
        TableGroup tableGroup1 = new TableGroup();
        TableGroup tableGroup2 = new TableGroup();
        TableGroup tableGroup3 = new TableGroup();
        tableGroup1.changeCreatedDate(LocalDateTime.now());
        tableGroup2.changeCreatedDate(LocalDateTime.now());
        tableGroup3.changeCreatedDate(LocalDateTime.now());
        this.tableGroupRepository.save(tableGroup1);
        this.tableGroupRepository.save(tableGroup2);
        this.tableGroupRepository.save(tableGroup3);

        // when
        List<TableGroup> tableGroups = this.tableGroupRepository.findAll();

        // then
        assertThat(tableGroups).hasSize(3);
    }
}
