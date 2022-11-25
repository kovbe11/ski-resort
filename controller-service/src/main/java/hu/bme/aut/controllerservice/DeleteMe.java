package hu.bme.aut.controllerservice;

import hu.bme.aut.controllerservice.service.LiftControllingService;
import hu.bme.aut.controllerservice.service.dto.LiftStateMessage;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DeleteMe {
    private final LiftControllingService liftControllingService;

    @GetMapping("/{liftId}/{liftState}")
    public String control(@PathVariable String liftId, @PathVariable String liftState) {
        final var instant = liftControllingService.sendCommand(liftId, LiftStateMessage.LiftState.valueOf(liftState.toUpperCase()));
        return instant.toString();
    }
}
