package ForoHubChallenge.Alura.ForoHubAlura.Controllers;

import ForoHubChallenge.Alura.ForoHubAlura.domain.topico.*;
import ForoHubChallenge.Alura.ForoHubAlura.infra.errores.ErrorMessage;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topico")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity<?>  registrarMedico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder) {
        if(topicoRepository.existsByTituloAndMensaje(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje())){
            String mensaje = String.format(
                    "No se pudo crear el recurso. Ya existe un tópico con el título '%s' y mensaje '%s'.",
                    datosRegistroTopico.titulo(),
                    datosRegistroTopico.mensaje()
            );
            return ResponseEntity.badRequest().body(new ErrorMessage(mensaje, 409));
        }
        Topico topico = topicoRepository.save(new Topico(datosRegistroTopico));
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion());
        URI url = uriComponentsBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopicos>> listadoTopicos(Pageable pageable) {
        return ResponseEntity.ok(topicoRepository.findAll(pageable).map(DatosListadoTopicos::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity retornarDatosDeTopico(@PathVariable @Valid Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("El parámetro 'id' es obligatorio.");
        }
        Topico topico = topicoRepository.getReferenceById(id);
        var dataTopics =  new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion());
        return ResponseEntity.ok(dataTopics);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizarTopico(@RequestBody @Valid DatosActualizarTopico datosActualizarTopico, @PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        topico.actualizarDatos(datosActualizarTopico);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        if(topicoRepository.existsById(id)){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();

        }

    }


}
