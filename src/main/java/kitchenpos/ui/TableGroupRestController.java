package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupCreate;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupViewResponse> create(@RequestBody final TableGroupCreateRequest createRequest) {
        final TableGroup created = tableGroupService.create(new TableGroupCreate(createRequest.getOrderTableIds()));
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(TableGroupViewResponse.of(created));
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
