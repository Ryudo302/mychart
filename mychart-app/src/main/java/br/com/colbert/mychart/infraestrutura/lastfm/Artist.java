package br.com.colbert.mychart.infraestrutura.lastfm;

import java.util.*;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Artist {

	@Expose
	private String name;
	@Expose
	private String listeners;
	@Expose
	private String mbid;
	@Expose
	private String url;
	@Expose
	private String streamable;
	@Expose
	private List<Image> image = new ArrayList<Image>();

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Artist withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @return The listeners
	 */
	public String getListeners() {
		return listeners;
	}

	/**
	 * 
	 * @param listeners
	 *            The listeners
	 */
	public void setListeners(String listeners) {
		this.listeners = listeners;
	}

	public Artist withListeners(String listeners) {
		this.listeners = listeners;
		return this;
	}

	/**
	 * 
	 * @return The mbid
	 */
	public String getMbid() {
		return mbid;
	}

	/**
	 * 
	 * @param mbid
	 *            The mbid
	 */
	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public Artist withMbid(String mbid) {
		this.mbid = mbid;
		return this;
	}

	/**
	 * 
	 * @return The url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 *            The url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public Artist withUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 
	 * @return The streamable
	 */
	public String getStreamable() {
		return streamable;
	}

	/**
	 * 
	 * @param streamable
	 *            The streamable
	 */
	public void setStreamable(String streamable) {
		this.streamable = streamable;
	}

	public Artist withStreamable(String streamable) {
		this.streamable = streamable;
		return this;
	}

	/**
	 * 
	 * @return The image
	 */
	public List<Image> getImage() {
		return image;
	}

	/**
	 * 
	 * @param image
	 *            The image
	 */
	public void setImage(List<Image> image) {
		this.image = image;
	}

	public Artist withImage(List<Image> image) {
		this.image = image;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
