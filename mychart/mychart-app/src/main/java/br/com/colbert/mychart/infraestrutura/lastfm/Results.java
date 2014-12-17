package br.com.colbert.mychart.infraestrutura.lastfm;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.*;

@Generated("org.jsonschema2pojo")
public class Results {

	@SerializedName("opensearch:Query")
	@Expose
	private OpensearchQuery opensearchQuery;
	@SerializedName("opensearch:totalResults")
	@Expose
	private String opensearchTotalResults;
	@SerializedName("opensearch:startIndex")
	@Expose
	private String opensearchStartIndex;
	@SerializedName("opensearch:itemsPerPage")
	@Expose
	private String opensearchItemsPerPage;
	@Expose
	private ArtistMatches artistMatches;
	@SerializedName("@attr")
	@Expose
	private br.com.colbert.mychart.infraestrutura.lastfm.Attr Attr;

	/**
	 * 
	 * @return The opensearchQuery
	 */
	public OpensearchQuery getOpensearchQuery() {
		return opensearchQuery;
	}

	/**
	 * 
	 * @param opensearchQuery
	 *            The opensearch:Query
	 */
	public void setOpensearchQuery(OpensearchQuery opensearchQuery) {
		this.opensearchQuery = opensearchQuery;
	}

	/**
	 * 
	 * @return The opensearchTotalResults
	 */
	public String getOpensearchTotalResults() {
		return opensearchTotalResults;
	}

	/**
	 * 
	 * @param opensearchTotalResults
	 *            The opensearch:totalResults
	 */
	public void setOpensearchTotalResults(String opensearchTotalResults) {
		this.opensearchTotalResults = opensearchTotalResults;
	}

	/**
	 * 
	 * @return The opensearchStartIndex
	 */
	public String getOpensearchStartIndex() {
		return opensearchStartIndex;
	}

	/**
	 * 
	 * @param opensearchStartIndex
	 *            The opensearch:startIndex
	 */
	public void setOpensearchStartIndex(String opensearchStartIndex) {
		this.opensearchStartIndex = opensearchStartIndex;
	}

	/**
	 * 
	 * @return The opensearchItemsPerPage
	 */
	public String getOpensearchItemsPerPage() {
		return opensearchItemsPerPage;
	}

	/**
	 * 
	 * @param opensearchItemsPerPage
	 *            The opensearch:itemsPerPage
	 */
	public void setOpensearchItemsPerPage(String opensearchItemsPerPage) {
		this.opensearchItemsPerPage = opensearchItemsPerPage;
	}

	/**
	 * 
	 * @return The artistMatches
	 */
	public ArtistMatches getArtistmatches() {
		return artistMatches;
	}

	/**
	 * 
	 * @param artistMatches
	 *            The artistMatches
	 */
	public void setArtistmatches(ArtistMatches artistMatches) {
		this.artistMatches = artistMatches;
	}

	/**
	 * 
	 * @return The Attr
	 */
	public br.com.colbert.mychart.infraestrutura.lastfm.Attr getAttr() {
		return Attr;
	}

	/**
	 * 
	 * @param Attr
	 *            The @attr
	 */
	public void setAttr(br.com.colbert.mychart.infraestrutura.lastfm.Attr Attr) {
		this.Attr = Attr;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
