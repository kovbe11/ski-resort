package hu.bme.aut.service.dto;

public record LiftDataResult(String liftId,
                             LiftConnectionResult liftConnectionResult,
                             LiftStateResult liftStateResult) {
}
