package DashBoard;

import Dto.OpcEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private Mapping mapping;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        OpcEntity data = mapping.mapnodes();

        model.addAttribute("actual", data.getActual());
        model.addAttribute("target", data.getTarget());
        model.addAttribute("uptime", data.getUptime());
        model.addAttribute("downtime", data.getDowntime());
        model.addAttribute("rooltarget", data.getRooltarget());
        model.addAttribute("quality", 100);

        double productionPercent = 0;
        if (data.getTarget() > 0) {
            productionPercent = (data.getActual() * 100.0) / data.getTarget();
        }
        model.addAttribute("productionPercent", productionPercent);

        return "index"; // your JSP page without .jsp extension
    }
}
