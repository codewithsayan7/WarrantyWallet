package com.codewithsayan.warrantywallet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final WarrantyService service;
    public DashboardController(WarrantyService service){this.service=service;}
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("items",service.all());
        model.addAttribute("reminders",service.reminders());
        model.addAttribute("coveredCount",service.countCovered());
        model.addAttribute("soonCount",service.countSoon());
        model.addAttribute("expiredCount",service.countExpired());
        model.addAttribute("totalCount",service.all().size());
        return "dashboard";
    }
}
