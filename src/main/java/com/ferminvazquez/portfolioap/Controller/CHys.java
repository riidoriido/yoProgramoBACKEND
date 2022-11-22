package com.ferminvazquez.portfolioap.Controller;

import com.ferminvazquez.portfolioap.Dto.dtoHys;
import com.ferminvazquez.portfolioap.Entity.Hys;
import com.ferminvazquez.portfolioap.Security.Controller.Mensaje;
import com.ferminvazquez.portfolioap.Service.Shys;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hys")
@CrossOrigin(origins = "https://frontendfv.web.app")
public class CHys {
    @Autowired
    Shys shys;
    
    @GetMapping("/lista")
    public ResponseEntity<List<Hys>> list(){
        List<Hys> list = shys.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody dtoHys dtohys) {
        if(StringUtils.isBlank(dtohys.getNombre()))
            return new ResponseEntity(new Mensaje("El campo nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        
        if(shys.existsByNombre(dtohys.getNombre()))
            return new ResponseEntity(new Mensaje("Ya existente"), HttpStatus.BAD_REQUEST);
        
        Hys hys = new Hys(
                dtohys.getNombre(),
                dtohys.getPorcentaje());
                
        shys.save(hys);
        return new ResponseEntity(new Mensaje("Se agregó correctamente"),HttpStatus.OK);    
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody dtoHys dtohys){
        if(!shys.existsById(id))
            return new ResponseEntity(new Mensaje("Id inexistente"),HttpStatus.BAD_REQUEST);
        
        if(shys.existsByNombre(dtohys.getNombre()) && shys.getByNombre(dtohys.getNombre()).get().getId() != id)
            return new ResponseEntity(new Mensaje("Nombre en uso"),HttpStatus.BAD_REQUEST);
        
        if(StringUtils.isBlank(dtohys.getNombre()))
            return new ResponseEntity(new Mensaje("El campo nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        
        Hys hys = shys.getOne(id).get();
        hys.setNombre(dtohys.getNombre());
        hys.setPorcentaje(dtohys.getPorcentaje());
        
        shys.save(hys);
        return new ResponseEntity(new Mensaje("Experiencia actualizada"), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        if(!shys.existsById(id))
            return new ResponseEntity(new Mensaje("Id inexistente"),HttpStatus.BAD_REQUEST);
        
        shys.delete(id);
        
        return new ResponseEntity(new Mensaje("Se eliminó el elemento"), HttpStatus.OK);
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Hys> getById(@PathVariable("id") int id){
        if(!shys.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Hys hys = shys.getOne(id).get();
        return new ResponseEntity(hys, HttpStatus.OK);
    }
}
