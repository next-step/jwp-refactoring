package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {

    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    //주문 그룹을 저장한다.
    // - id,생성일자,List<OrderTable>
        //private Long id;
        //private Long tableGroupId;
        //private int numberOfGuests;
        //private boolean empty;
        //-- TableGroup의 orderTables 갯수는 2개보다 작으면 안됨.

    //주문그룹을 삭제한다.
        //-- tableGroupId로 OrderTable목록을 조회한다.
        //-- orderTableIds로 COOKING, MEAL상태인 깞이 있는지 조회하는데 있으면 에러 (그니까 요리중이거나 식사중인 테이블그룹은 없어야함)
    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroup> create(@RequestBody final TableGroup tableGroup) {
        final TableGroup created = tableGroupService.create(tableGroup);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
