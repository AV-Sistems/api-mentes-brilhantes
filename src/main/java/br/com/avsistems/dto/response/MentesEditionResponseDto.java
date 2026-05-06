    package br.com.avsistems.dto.response;

    import br.com.avsistems.entity.MentesEditionEntity;

    import java.time.LocalDate;
    import java.util.UUID;

    public record MentesEditionResponseDto (
            UUID id,
            String title,
            LocalDate dateEdition,
            String zipCode,
            String city,
            String state
    ){
        public MentesEditionResponseDto(MentesEditionEntity entity){
            this(
                 entity.id,
                 entity.title,
                 entity.dateEdition,
                    entity.zipCode,
                    entity.city,
                    entity.state
            );
        }
    }
