/**
 * <p>A camada de domínio do modelo proposto pelo <em>Domain Driven Design</em> (DDD).</p>
 * <p>É o coração da aplicação, contendo todas as regras de negócio, bem como as entidades e outros elementos que fazem parte do domínio, como repositórios, serviços, 
 * VOs e fábricas.</p>
 * @see <a href="http://dddsample.sourceforge.net/architecture.html">DDD - Architecture</a>
 */
@TypeDefs({ @TypeDef(name = "localDateType", defaultForType = LocalDate.class, typeClass = LocalDateUserType.class),
		@TypeDef(name = "optionalType", defaultForType = Optional.class, typeClass = OptionalUserType.class) })
package br.com.colbert.mychart.dominio;

import java.time.LocalDate;
import java.util.Optional;

import org.hibernate.annotations.*;

import br.com.colbert.mychart.infraestrutura.hibernate.type.*;

