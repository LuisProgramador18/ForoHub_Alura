package ForoHubChallenge.Alura.ForoHubAlura.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopicos(Long id,
                                  String titulo,
                                  String mensaje,
                                  LocalDateTime fechaCreacion) {

    public DatosListadoTopicos(Topico topico){
        this(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion());
    }
}
