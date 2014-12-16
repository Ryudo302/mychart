package br.com.colbert.mychart.infraestrutura.lastfm;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class ResultadoLastFm {

	@Expose
	private Results results;

	/**
	 * 
	 * @return The results
	 */
	public Results getResults() {
		return results;
	}

	/**
	 * 
	 * @param results
	 *            The results
	 */
	public void setResults(Results results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
