package com.codewithsayan.warrantywallet;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
public class WarrantyController {
    private final WarrantyService service;
    private final ReceiptStorageService storage;
    public WarrantyController(WarrantyService service,ReceiptStorageService storage){this.service=service;this.storage=storage;}

    @GetMapping("/warranties/new")
    public String newForm(Model model){
        if(!model.containsAttribute("form")) { WarrantyForm form=new WarrantyForm(); form.setPurchaseDate(LocalDate.now()); model.addAttribute("form",form); }
        model.addAttribute("categories",categories());
        return "warranty-form";
    }

    @PostMapping("/warranties")
    public String create(@Valid @ModelAttribute("form") WarrantyForm form, BindingResult result,
                         @RequestParam(name="receipt",required=false) MultipartFile receipt,
                         Model model,RedirectAttributes redirect){
        model.addAttribute("categories",categories());
        if(result.hasErrors()) return "warranty-form";
        String stored=null;
        try{
            stored=storage.store(receipt);
            service.create(form,stored);
            redirect.addFlashAttribute("successMessage","Product saved. Warranty expiry was calculated.");
            return "redirect:/";
        }catch(IllegalArgumentException | IllegalStateException ex){
            if(stored!=null) storage.delete(stored);
            result.reject("save.error",ex.getMessage());
            return "warranty-form";
        }
    }

    @GetMapping("/warranties/{id}")
    public String details(@PathVariable Long id,Model model){
        model.addAttribute("item",service.get(id));
        model.addAttribute("repairs",service.repairs(id));
        if(!model.containsAttribute("repairForm")) model.addAttribute("repairForm",new RepairForm());
        return "warranty-detail";
    }

    @PostMapping("/warranties/{id}/repairs")
    public String addRepair(@PathVariable Long id,@Valid @ModelAttribute("repairForm") RepairForm form,
                            BindingResult result,Model model,RedirectAttributes redirect){
        if(result.hasErrors()){
            model.addAttribute("item",service.get(id));
            model.addAttribute("repairs",service.repairs(id));
            return "warranty-detail";
        }
        service.addRepair(id,form);
        redirect.addFlashAttribute("successMessage","Service history entry added.");
        return "redirect:/warranties/"+id;
    }

    @PostMapping("/warranties/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirect){
        service.delete(id);
        redirect.addFlashAttribute("successMessage","Product and related history removed.");
        return "redirect:/";
    }

    @GetMapping("/receipts/{fileName:.+}")
    public ResponseEntity<Resource> receipt(@PathVariable String fileName){
        Resource resource=storage.load(fileName);
        MediaType type=MediaType.APPLICATION_OCTET_STREAM;
        String lower=fileName.toLowerCase();
        if(lower.endsWith(".pdf")) type=MediaType.APPLICATION_PDF;
        else if(lower.endsWith(".png")) type=MediaType.IMAGE_PNG;
        else if(lower.endsWith(".jpg")||lower.endsWith(".jpeg")) type=MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(type)
                .header(HttpHeaders.CONTENT_DISPOSITION,ContentDisposition.inline().filename("purchase-receipt").build().toString())
                .body(resource);
    }

    private String[] categories(){return new String[]{"Electronics","Home appliance","Computer","Mobile","Furniture","Other"};}
}
