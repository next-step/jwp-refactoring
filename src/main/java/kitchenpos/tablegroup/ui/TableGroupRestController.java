package kitchenpos.tablegroup.ui;

import io.swagger.annotations.ApiOperation;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @ApiOperation("테이블 그룹(단체) 생성")
    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroup> create(@RequestBody final TableGroup tableGroup) {
        final TableGroup created = tableGroupService.create(tableGroup);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @ApiOperation("테이블 그룹(단체) 삭제")
    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
