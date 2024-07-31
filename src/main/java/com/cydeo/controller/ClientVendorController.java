package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.service.ClientVendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;

    public ClientVendorController(ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }
    @GetMapping("/list")
    public String getClientVendors(Model model) {
        model.addAttribute("clientVendors", clientVendorService.listAllClientVendorsByCompany());

        return "/clientVendor/clientVendor-list";
    }
    @GetMapping("/create")
    public String createClientVendorForm(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDto());
        model.addAttribute("clientVendorTypes", clientVendorService.findAllTypes());
        return "/clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String saveClientVendor(@ModelAttribute("newClientVendor") @Valid ClientVendorDto clientVendorDto,BindingResult bindingResult,Model model) {
        if (clientVendorService.existsByName(clientVendorDto.getClientVendorName())){
            bindingResult.rejectValue("clientVendorName", "", "Client vendor already exists");
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("clientVendorTypes", clientVendorService.findAllTypes());
            return "/clientVendor/clientVendor-create";
        }

        clientVendorService.save(clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String updateClientVendorForm(@PathVariable("id") Long id, Model model) {
        ClientVendorDto clientVendorDto = clientVendorService.findById(id);
        model.addAttribute("clientVendor", clientVendorDto);
        model.addAttribute("clientVendorTypes", clientVendorService.findAllTypes());
        return "/clientVendor/clientVendor-update";
    }
    @PostMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") Long id, @ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto,BindingResult bindingResult,Model model) {
        if (clientVendorService.existsByName(clientVendorDto.getClientVendorName())){
            bindingResult.rejectValue("clientVendorName", "", "Client vendor already exists");
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("clientVendorTypes", clientVendorService.findAllTypes());
            return "/clientVendor/clientVendor-create";
        }
        clientVendorDto.setId(id);
        clientVendorService.update(clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendors(@PathVariable("id") Long id) {
        clientVendorService.delete(id);

        return "redirect:/clientVendors/list";
    }

}
