/**
 * As classes de domínio da aplicação, como entidades, repositórios e serviços.
 */
@TypeDefs({ @TypeDef(name = "localDateType", defaultForType = LocalDate.class, typeClass = LocalDateUserType.class),
		@TypeDef(name = "optionalType", defaultForType = Optional.class, typeClass = OptionalUserType.class) })
package br.com.colbert.mychart.dominio;

import java.time.LocalDate;
import java.util.Optional;

import org.hibernate.annotations.*;

import br.com.colbert.mychart.infraestrutura.hibernate.*;
import br.com.colbert.mychart.infraestrutura.hibernate.type.*;

