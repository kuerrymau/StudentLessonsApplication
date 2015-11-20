package com.crimore.model;

import com.crimore.model.util.JsfUtil;
import com.crimore.model.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean(name = "lessonsController")
@SessionScoped
public class LessonsController implements Serializable
{

    @EJB
    private com.crimore.model.LessonsFacade ejbFacade;
    private List<Lessons> items = null;
    private Lessons selected;

    public LessonsController()
    {
    }

    public Lessons getSelected()
    {
        return selected;
    }

    public void setSelected(Lessons selected)
    {
        this.selected = selected;
    }

    protected void setEmbeddableKeys()
    {
    }

    protected void initializeEmbeddableKey()
    {
    }

    private LessonsFacade getFacade()
    {
        return ejbFacade;
    }

    public Lessons prepareCreate()
    {
        selected = new Lessons();
        initializeEmbeddableKey();
        return selected;
    }

    public void create()
    {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LessonsCreated"));
        if (!JsfUtil.isValidationFailed())
        {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update()
    {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LessonsUpdated"));
    }

    public void destroy()
    {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LessonsDeleted"));
        if (!JsfUtil.isValidationFailed())
        {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Lessons> getItems()
    {
        if (items == null)
        {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage)
    {
        if (selected != null)
        {
            setEmbeddableKeys();
            try
            {
                if (persistAction != PersistAction.DELETE)
                {
                    getFacade().edit(selected);
                } else
                {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex)
            {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null)
                {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0)
                {
                    JsfUtil.addErrorMessage(msg);
                } else
                {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Lessons> getItemsAvailableSelectMany()
    {
        return getFacade().findAll();
    }

    public List<Lessons> getItemsAvailableSelectOne()
    {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Lessons.class)
    public static class LessonsControllerConverter implements Converter
    {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value)
        {
            if (value == null || value.length() == 0)
            {
                return null;
            }
            LessonsController controller = (LessonsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "lessonsController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value)
        {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object)
        {
            if (object == null)
            {
                return null;
            }
            if (object instanceof Lessons)
            {
                Lessons o = (Lessons) object;
                return getStringKey(o.getId());
            } else
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]
                {
                    object, object.getClass().getName(), Lessons.class.getName()
                });
                return null;
            }
        }

    }

}
