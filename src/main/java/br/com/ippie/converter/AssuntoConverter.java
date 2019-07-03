package br.com.ippie.converter;

import br.com.ippie.negocio.Assunto;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Ayran
 */
@FacesConverter("assuntoConverter")
public class AssuntoConverter implements Converter 
{
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, 
            String value) 
    {
    Object o=FacesContext.getCurrentInstance().getExternalContext()
                .getApplicationMap().get(value);
    FacesContext.getCurrentInstance().getExternalContext()
                .getApplicationMap().remove(value);
    return o;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, 
            Object value) 
    {
    String id="";
      if(value instanceof Assunto)
      {
      id=((Assunto)value).getCodigo()+"";
      FacesContext.getCurrentInstance().getExternalContext().getApplicationMap()
            .put(id,value);
      }
    return id;
    }
}