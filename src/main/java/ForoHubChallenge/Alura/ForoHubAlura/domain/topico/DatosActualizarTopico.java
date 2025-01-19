package ForoHubChallenge.Alura.ForoHubAlura.domain.topico;

import java.time.LocalDateTime;

public record DatosActualizarTopico(
        Long id,
         String titulo,
         String mensaje,
         LocalDateTime fechaCreacion,
         String status,
         Integer autor,
         String curso


) {
}
