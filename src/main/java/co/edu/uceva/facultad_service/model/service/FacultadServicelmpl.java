package co.edu.uceva.facultad_service.model.service;


import co.edu.uceva.facultad_service.model.dao.IFacultadDao;
import co.edu.uceva.facultad_service.model.entities.Facultad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultadServicelmpl implements IFacultadService{


    @Autowired
    IFacultadDao facultadDao;

    @Override
    public Facultad save(Facultad facultad) {
        return facultadDao.save(facultad);
    }

    @Override
    public void delete(Facultad facultad) {
        facultadDao.delete(facultad);
    }

    @Override
    public Facultad update(Facultad facultad) {
        return facultadDao.save(facultad);
    }

    @Override
    public List<Facultad> findAll() {
        return (List<Facultad>) facultadDao.findAll();
    }
}