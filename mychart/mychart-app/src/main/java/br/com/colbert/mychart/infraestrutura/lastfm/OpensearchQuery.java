
package br.com.colbert.mychart.infraestrutura.lastfm;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class OpensearchQuery {

    @SerializedName("#text")
    @Expose
    private String Text;
    @Expose
    private String role;
    @Expose
    private String searchTerms;
    @Expose
    private String startPage;

    /**
     * 
     * @return
     *     The Text
     */
    public String getText() {
        return Text;
    }

    /**
     * 
     * @param Text
     *     The #text
     */
    public void setText(String Text) {
        this.Text = Text;
    }

    public OpensearchQuery withText(String Text) {
        this.Text = Text;
        return this;
    }

    /**
     * 
     * @return
     *     The role
     */
    public String getRole() {
        return role;
    }

    /**
     * 
     * @param role
     *     The role
     */
    public void setRole(String role) {
        this.role = role;
    }

    public OpensearchQuery withRole(String role) {
        this.role = role;
        return this;
    }

    /**
     * 
     * @return
     *     The searchTerms
     */
    public String getSearchTerms() {
        return searchTerms;
    }

    /**
     * 
     * @param searchTerms
     *     The searchTerms
     */
    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    public OpensearchQuery withSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
        return this;
    }

    /**
     * 
     * @return
     *     The startPage
     */
    public String getStartPage() {
        return startPage;
    }

    /**
     * 
     * @param startPage
     *     The startPage
     */
    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public OpensearchQuery withStartPage(String startPage) {
        this.startPage = startPage;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
