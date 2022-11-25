package hu.bme.aut.controllerservice.service.dto;

public record LiftDataResult(String liftId,
                             LiftConnectionResult liftConnectionResult,
                             LiftStateResult liftStateResult) {
}
