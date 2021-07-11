package kitchenpos.table.dto;

public class UpdateEmptyDto {
    private Boolean empty;

    public UpdateEmptyDto() { }

    public UpdateEmptyDto(Boolean empty) {
        this.empty = empty;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
