package com.chema.productos;

import com.chema.productos.modelo.ProductoElectronico;
import com.chema.productos.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:pruebas_proyecto;DB_CLOSE_DELAY=-1", "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc
class ProductoApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired ProductoRepository repo;
    private final String generico="{\"nombre\":\"Cuaderno\",\"precio\":3.50,\"cantidad\":25}";
    private final String electronico="{\"nombre\":\"Portátil\",\"precio\":799.99,\"cantidad\":5,\"tipo\":\"ELECTRONICO\",\"marca\":\"Lenovo\",\"modelo\":\"IdeaPad\"}";
    @BeforeEach void limpiar() { repo.deleteAll(); }
    private long crear(String json) throws Exception {
        String salida=mvc.perform(post("/productos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated()).andExpect(header().exists("Location"))
            .andReturn().getResponse().getContentAsString();
        return mapper.readTree(salida).get("id").asLong();
    }
    @Test void crudGenericoCompleto() throws Exception {
        mvc.perform(get("/productos")).andExpect(status().isOk()).andExpect(content().json("[]"));
        long id=crear(generico);
        mvc.perform(get("/productos/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.tipo").value("GENERICO"));
        mvc.perform(put("/productos/"+id).contentType(MediaType.APPLICATION_JSON)
            .content("{\"nombre\":\"Cuaderno azul\",\"precio\":4.20,\"cantidad\":10}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Cuaderno azul"));
        mvc.perform(get("/productos/"+id)).andExpect(jsonPath("$.cantidad").value(10));
        mvc.perform(delete("/productos/"+id)).andExpect(status().isNoContent()).andExpect(content().string(""));
        mvc.perform(get("/productos/"+id)).andExpect(status().isNotFound());
        assertThat(repo.count()).isZero();
    }
    @Test void persisteSubclaseElectronicaYActualizaMarca() throws Exception {
        long id=crear(electronico);
        // Otra consulta del repositorio debe reconstruir la subclase desde H2.
        assertThat(repo.findById(id).orElseThrow()).isInstanceOf(ProductoElectronico.class);
        mvc.perform(get("/productos/"+id)).andExpect(jsonPath("$.marca").value("Lenovo"))
            .andExpect(jsonPath("$.descripcion").value(org.hamcrest.Matchers.containsString("IdeaPad")));
        mvc.perform(put("/productos/"+id).contentType(MediaType.APPLICATION_JSON)
            .content(electronico.replace("Lenovo","Acer").replace("IdeaPad","Aspire")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.marca").value("Acer"));
        mvc.perform(get("/productos/"+id)).andExpect(jsonPath("$.modelo").value("Aspire"));
    }
    @Test void filtraYBuscaMayorCantidadDesdeH2() throws Exception {
        long id=crear(generico); crear(electronico);
        mvc.perform(get("/productos").param("nombre","PORT")).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].nombre").value("Portátil"));
        mvc.perform(get("/productos/mayor-stock")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
        mvc.perform(get("/productos").param("nombre","ausente")).andExpect(content().json("[]"));
        mvc.perform(get("/productos").param("nombre"," ")).andExpect(status().isBadRequest());
    }
    @Test void mayorStockVacioEs204() throws Exception {
        mvc.perform(get("/productos/mayor-stock")).andExpect(status().isNoContent());
    }
    @Test void inexistentes404YFormatoId400() throws Exception {
        mvc.perform(get("/productos/999999")).andExpect(status().isNotFound()).andExpect(jsonPath("$.estado").value(404));
        mvc.perform(put("/productos/999999").contentType(MediaType.APPLICATION_JSON).content(generico)).andExpect(status().isNotFound());
        mvc.perform(delete("/productos/999999")).andExpect(status().isNotFound());
        mvc.perform(get("/productos/abc")).andExpect(status().isBadRequest());
        mvc.perform(get("/productos/0")).andExpect(status().isBadRequest());
    }
    @Test void rechazaDatosInvalidosSinInsertar() throws Exception {
        for(String json:new String[]{"{}", "{", generico.replace("3.50","-1"), generico.replace("25","-1"),
            generico.replace("Cuaderno"," "),generico.replace("3.50","1.999"),generico.replace("25","2.5"),
            generico.replace("3.50","10000000000"),generico.replace("{","{\"id\":15,"),
            generico.replace("\"Cuaderno\"","123"),generico.replace("\"Cuaderno\"","1.5"),
            generico.replace("\"Cuaderno\"","true"),generico.replace("3.50","\"3.50\""),
            generico.replace("25","\"25\""),generico.replace("25","true")}) {
            mvc.perform(post("/productos").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
        }
        assertThat(repo.count()).isZero();
    }
    @Test void validaCamposSegunTipo() throws Exception {
        for(String json:new String[]{electronico.replace("Lenovo"," "), electronico.replace("\"IdeaPad\"","null"),
            electronico.replace("ELECTRONICO","OTRO"),electronico.replace("ELECTRONICO","GENERICO"),
            electronico.replace("\"ELECTRONICO\"","1"),electronico.replace("ELECTRONICO","1"),
            generico.replace("{","{\"tipo\":0,"),generico.replace("{","{\"tipo\":\"0\","),
            electronico.replace("\"Lenovo\"","123"),electronico.replace("\"IdeaPad\"","true")}) {
            mvc.perform(post("/productos").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
        }
        assertThat(repo.count()).isZero();
    }
    @Test void putInvalidoYConflictoNoAlteranDatos() throws Exception {
        long id=crear(electronico);
        mvc.perform(put("/productos/"+id).contentType(MediaType.APPLICATION_JSON).content(electronico.replace("Lenovo"," ")))
            .andExpect(status().isBadRequest());
        mvc.perform(put("/productos/"+id).contentType(MediaType.APPLICATION_JSON).content(electronico.replace("\"Lenovo\"","123")))
            .andExpect(status().isBadRequest());
        mvc.perform(put("/productos/"+id).contentType(MediaType.APPLICATION_JSON)
            .content(generico.replace("{","{\"tipo\":\"GENERICO\","))).andExpect(status().isConflict());
        mvc.perform(get("/productos/"+id)).andExpect(jsonPath("$.nombre").value("Portátil")).andExpect(jsonPath("$.marca").value("Lenovo"));
    }
    @Test void admitePrecioYCantidadCero() throws Exception {
        long id=crear("{\"nombre\":\"Muestra\",\"precio\":0,\"cantidad\":0}");
        mvc.perform(get("/productos/"+id)).andExpect(jsonPath("$.precio").value(0)).andExpect(jsonPath("$.cantidad").value(0));
    }
}
