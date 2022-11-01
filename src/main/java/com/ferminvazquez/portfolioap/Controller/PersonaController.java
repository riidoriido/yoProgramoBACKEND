
package com.ferminvazquez.portfolioap.Controller;

import com.ferminvazquez.portfolioap.Dto.dtoPersona;
import com.ferminvazquez.portfolioap.Entity.Persona;
import com.ferminvazquez.portfolioap.Security.Controller.Mensaje;
import com.ferminvazquez.portfolioap.Service.ImpPersonaService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
/*import org.springframework.web.bind.annotation.DeleteMapping;*/
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personas")
@CrossOrigin(origins = "https://frontendfv.web.app")
public class PersonaController {
    
    @Autowired
    ImpPersonaService personaService;
    
    @GetMapping("/lista")
    public ResponseEntity<List<Persona>> list(){
        List<Persona> list = personaService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody dtoPersona dtopersona) {
        if(StringUtils.isBlank(dtopersona.getNombre()))
            return new ResponseEntity(new Mensaje("El campo nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        
        if(personaService.existsByNombre(dtopersona.getNombre()))
            return new ResponseEntity(new Mensaje("Ya existente"), HttpStatus.BAD_REQUEST);
        
        Persona persona = new Persona(
                dtopersona.getNombre(),
                dtopersona.getApellido(),
                dtopersona.getBio(),
                dtopersona.getImg());
                
        personaService.save(persona);
        return new ResponseEntity(new Mensaje("Se agregó correctamente"),HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody dtoPersona dtopersona){
        if(!personaService.existsById(id))
            return new ResponseEntity(new Mensaje("Id inexistente"),HttpStatus.BAD_REQUEST);
        
        if(personaService.existsByNombre(dtopersona.getNombre()) && personaService.getByNombre(dtopersona.getNombre()).get().getId() != id)
            return new ResponseEntity(new Mensaje("Nombre en uso"),HttpStatus.BAD_REQUEST);
        
        if(StringUtils.isBlank(dtopersona.getNombre()))
            return new ResponseEntity(new Mensaje("El campo nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        
        Persona persona = personaService.getOne(id).get();
        persona.setNombre(dtopersona.getNombre());
        persona.setApellido(dtopersona.getApellido());
        persona.setBio(dtopersona.getBio());
        persona.setImg(dtopersona.getImg());
        
        personaService.save(persona);
        return new ResponseEntity(new Mensaje("Persona actualizada"), HttpStatus.OK);
    }
    
    /*@DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        if(!personaService.existsById(id))
            return new ResponseEntity(new Mensaje("Id inexistente"),HttpStatus.BAD_REQUEST);
        
        personaService.delete(id);
        
        return new ResponseEntity(new Mensaje("Se eliminó el elemento"), HttpStatus.OK);
    }*/
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Persona> getById(@PathVariable("id") int id){
        if(!personaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Persona persona = personaService.getOne(id).get();
        return new ResponseEntity(persona, HttpStatus.OK);
    }
    
}

