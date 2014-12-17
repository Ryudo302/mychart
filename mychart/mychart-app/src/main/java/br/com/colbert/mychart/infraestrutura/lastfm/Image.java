
package br.com.colbert.mychart.infraestrutura.lastfm;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Image {

    @SerializedName("#text")
    @Expose
    private String Text;
    @Expose
    private String size;

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

    public Image withText(String Text) {
        this.Text = Text;
        return this;
    }

    /**
     * 
     * @return
     *     The size
     */
    public String getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(String size) {
        this.size = size;
    }

    public Image withSize(String size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
