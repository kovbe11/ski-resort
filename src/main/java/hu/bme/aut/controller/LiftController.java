package hu.bme.aut.controller;

import hu.bme.aut.service.LiftControllingService;
import hu.bme.aut.service.RegisteredLiftService;
import hu.bme.aut.service.dto.LiftStateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Transactional
@Controller
public class LiftController {

    @Autowired
    private RegisteredLiftService liftService;

    @Autowired
    private LiftControllingService liftControllingService;


    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("liftDatas", liftService.currentlyRegisteredLifts());
        return "user";
    }

    @GetMapping("/user/edit/start/{id}")
    public String openLift(@PathVariable("id") String id) {
        liftControllingService.sendCommand(id, LiftStateMessage.LiftState.OPEN);
        return "redirect:/user";
    }

    @GetMapping("/user/edit/stop/{id}")
    public String stopLift(@PathVariable("id") String id) {
        liftControllingService.sendCommand(id, LiftStateMessage.LiftState.CLOSED);
        return "redirect:/user";
    }

    @GetMapping("/user/edit/pause/{id}")
    public String pauseLift(@PathVariable("id") String id) {
        liftControllingService.sendCommand(id, LiftStateMessage.LiftState.TEMPORARILY_CLOSED);
        return "redirect:/user";
    }
}
