package br.com.escolalura.escolalura.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author zroz
 */
@Controller
public class AdminController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    
}
