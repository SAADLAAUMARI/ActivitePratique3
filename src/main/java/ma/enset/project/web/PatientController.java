package ma.enset.project.web;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;

import lombok.AllArgsConstructor;
import ma.enset.project.entities.Patient;
import ma.enset.project.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;
    @GetMapping("/user/index")
    public String index(Model model, @RequestParam(name ="page",defaultValue = "0") int p,
    @RequestParam(name="size" ,defaultValue = "4") int s,
                        @RequestParam(name="keyword" ,defaultValue = "") String kw){
        Page<Patient> pagePatient=patientRepository.findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(kw,kw,PageRequest.of(p,s));
        model.addAttribute("listPatients",pagePatient.getContent());
        model.addAttribute("pages",new int[pagePatient.getTotalPages()]);
        model.addAttribute("currentPage",p);
        model.addAttribute("keyword",kw);
        return "patients";
    }
@GetMapping("/admin/delete")
@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(Long id,String keyword,int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
}
    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }
    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formPatients(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }
@PostMapping(path = "/admin/save")
@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/index?page="+page+"&keyword"+keyword;
}
    @GetMapping("/admin/editPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editPatient(Model model,long id, String keyword,int page){
        Patient patient=patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editPatient";
    }
}
