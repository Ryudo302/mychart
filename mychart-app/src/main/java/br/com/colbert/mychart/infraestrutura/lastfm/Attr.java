
package br.com.colbert.mychart.infraestrutura.lastfm;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Attr {

    @SerializedName("for")
    @Expose
    private String _for;

    /**
     * 
     * @return
     *     The _for
     */
    public String getFor() {
        return _for;
    }

    /**
     * 
     * @param _for
     *     The for
     */
    public void setFor(String _for) {
        this._for = _for;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
