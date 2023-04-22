package co.edu.uceva.facultad_service;

import co.edu.uceva.facultad_service.model.entities.Facultad;
import co.edu.uceva.facultad_service.model.service.IFacultadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Pruebas unitarias (unit tests) para la  API RESTful que se encarga de realizar operaciones CRUD sobre una entidad
 * llamada "Facultad".
 * Se importan las clases necesarias para realizar las pruebas (MockMvc, ObjectMapper, etc.), se inyecta el servicio
 * que se encarga de realizar las operaciones sobre la entidad "Facultad" (IPFacultadService), y se definen varios métodos de
 * prueba para la API RESTful, que comprueban el correcto funcionamiento de los métodos:
 * GET, POST, PUT y DELETE de la API.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FacultadRestControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private IFacultadService facultadService;

    /**
     * Inicializa los objetos necesarios para la prueba. En el ejemplo de código dado, este método se utiliza para \
     * inicializar el objeto MockMvc, que se utiliza para simular el envío de solicitudes HTTP en la prueba de la  \
     * clase PaisRestController.
     */

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * Prueba del método GET "/facultad-service/facultad/{nombre}", que comprueba que se recibe el nombre correcto
     * en la respuesta.
     * @throws Exception
     */
    @Test
    public void testNombre() throws Exception{
        String nombre = "Ingenieria";
        this.mockMvc.perform(get("/facultad-service/facultad/{nombre}", nombre))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola " + nombre));
    }

    /**
     * Prueba del método GET "/facultad-service/listar", que comprueba que se recibe una lista de países en la respuesta.
     * @throws Exception
     */

    @Test
    public void testListar() throws Exception{
        Facultad facultad1 = new Facultad();
        facultad1.setNombre("Ingeniería");
        Facultad facultad2 = new Facultad();
        facultad2.setNombre("Ciencias de la salud");
        facultadService.save(facultad1);
        facultadService.save(facultad2);
        List<Facultad> listarFacultades = new ArrayList<>();
        listarFacultades.add(facultad1);
        listarFacultades.add(facultad2);

        this.mockMvc.perform(get("/facultad-service/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is(facultad1.getNombre())))
                .andExpect(jsonPath("$[1].nombre", is(facultad2.getNombre())));

        facultadService.delete(facultad1);
        facultadService.delete(facultad2);
    }

    /**
     * Prueba del método GET "/facultad-service/listar/{id}", que comprueba que se recibe el país correcto en la respuesta.
     * @throws Exception
     */
    @Test
    public void testBuscarFacultad() throws Exception{
        Facultad facultad1 = new Facultad();
        facultad1.setNombre("Ingeniería");
        facultadService.save(facultad1);

        this.mockMvc.perform(get("/facultad-service/listar/{id}", facultad1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(facultad1.getNombre())));

        facultadService.delete(facultad1);
    }

    /**
     * Prueba del método POST "/facultad-service/listar", que comprueba que se crea un nuevo país correctamente.
     * @throws Exception
     */
    @Test
    public void testCrearFacultad() throws Exception{
        Facultad facultad1 = new Facultad();
        facultad1.setNombre("Ingeniería");

        this.mockMvc.perform(post("/facultad-service/listar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(facultad1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(facultad1.getNombre())));

        facultadService.delete(facultad1);
    }

    /**
     * Prueba del método PUT "/facultad-service/listar", que comprueba que se actualiza un país correctamente.
     * @throws Exception
     */
    @Test
    private void testActualizarFacultad() throws Exception{
        Facultad facultad1 = new Facultad();
        facultad1.setNombre("Ingeniería");
        facultadService.save(facultad1);

        facultad1.setNombre("Ciencias de la salud");

        this.mockMvc.perform(post("/facultad-service/listar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(facultad1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(facultad1.getNombre())));

        facultadService.delete(facultad1);
    }

    /**
     * Prueba del método DELETE "/facultad-service/listar/{id}", que comprueba que se elimina un país correctamente.
     * @throws Exception
     */
    @Test
    public void testBorrarFacultad() throws Exception {
        Facultad facultad1 = new Facultad();
        facultad1.setNombre("Ingeniería");
        facultadService.save(facultad1);

        this.mockMvc.perform(delete("/facultad-service/facultad/{id}", facultad1.getId()))
                .andExpect(status().isOk());

        assertNull(facultadService.findById(facultad1.getId()));
    }

    /**
     * Método para convertir un objeto a una cadena JSON
     *
     * @param obj Objeto a convertir
     * @return Cadena JSON
     */
    private String asJsonString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
