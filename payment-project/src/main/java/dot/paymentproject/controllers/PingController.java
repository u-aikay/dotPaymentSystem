package dot.paymentproject.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PingController {

        @RequestMapping("/v1/ping")
        public String ping() {
            return "Welcome to dot payment project.";
        }
}
