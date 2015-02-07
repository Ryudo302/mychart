package br.com.colbert.base.ui.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.*;

import br.com.colbert.mychart.infraestrutura.swing.model.*;

/**
 * Testes de integração da {@link ObjectTableModelIT}.
 * 
 * @author Thiago Colbert
 * @since 21/12/2014
 */
public class ObjectTableModelIT {

	public static class Exemplo implements Comparable<Exemplo> {

		private String nome;
		private int numero;

		public Exemplo(String nome, int numero) {
			this.nome = nome;
			this.numero = numero;
		}

		public String getNome() {
			return nome;
		}

		public int getNumero() {
			return numero;
		}

		@Override
		public int compareTo(Exemplo other) {
			return CompareToBuilder.reflectionCompare(this, other);
		}
	}

	private static class ExemploTableModel extends ObjectTableModel<Exemplo> {

		private static final long serialVersionUID = 93896799728086251L;

		@Override
		protected List<ObjectTableModelColumn<?>> makeColumns() {
			ObjectTableModelColumn<String> coluna1 = ObjectTableModelColumn.<String> newColumn().withName("Nome")
					.withTheValueOf("nome").type(String.class);
			ObjectTableModelColumn<Integer> coluna2 = ObjectTableModelColumn.<Integer> newColumn().withName("Número")
					.withTheValueOf("numero").type(Integer.class);

			return Arrays.asList(coluna1, coluna2);
		}
	}

	private static final Exemplo ELEMENTO_TABELA = new Exemplo("A", 1);

	private ObjectTableModel<Exemplo> modelo;
	private JTable tabela;

	@Before
	public void setUp() {
		modelo = new ExemploTableModel();
		modelo.addElement(ELEMENTO_TABELA);

		tabela = spy(new JTable());
		tabela.setModel(modelo);
	}

	@Test
	public void testInclusaoElemento() {
		modelo.addElement(new Exemplo("B", 2));

		verify(tabela, atLeast(1)).tableChanged(any(TableModelEvent.class));
	}

	@Test
	public void testExclusaoElemento() {
		modelo.removeElement(ELEMENTO_TABELA);

		verify(tabela, atLeast(1)).tableChanged(any(TableModelEvent.class));
	}

	@Test
	public void testExibicaoElementos() {
		assertThat("A tabela deveria possuir 2 colunas", tabela.getColumnModel().getColumnCount(), is(equalTo(2)));
		assertThat("A tabela deveria possuir 1 linha", tabela.getRowCount(), is(equalTo(1)));
	}
}
