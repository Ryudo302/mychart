package br.com.colbert.mychart.infraestrutura.lastfm;

import java.util.*;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class ArtistMatches {

	@Expose
	private List<Artist> artist = new ArrayList<Artist>();

	/**
	 * 
	 * @return The artist
	 */
	public List<Artist> getArtist() {
		return artist;
	}

	/**
	 * 
	 * @param artist
	 *            The artist
	 */
	public void setArtist(List<Artist> artist) {
		this.artist = artist;
	}

	public ArtistMatches withArtist(List<Artist> artist) {
		this.artist = artist;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
