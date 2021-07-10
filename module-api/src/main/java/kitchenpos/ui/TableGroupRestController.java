package kitchenpos.ui;

import kitchenpos.application.TableGroupQueryService;
import kitchenpos.application.command.TableGroupService;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;
    private final TableGroupQueryService tableGroupQueryService;

    public TableGroupRestController(TableGroupService tableGroupService, TableGroupQueryService tableGroupQueryService) {
        this.tableGroupService = tableGroupService;
        this.tableGroupQueryService = tableGroupQueryService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupViewResponse> create(@RequestBody final TableGroupCreateRequest createRequest) {
        final Long id = tableGroupService.create(createRequest.toCreate());

        return ResponseEntity.created(URI.create("/api/table-groups/" + id))
                .body(tableGroupQueryService.findById(id));
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build();
    }
}
