/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimore.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Chayanne
 */
@Entity
@Table(name = "LESSONDAYS")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Lessondays.findAll", query = "SELECT l FROM Lessondays l"),
    @NamedQuery(name = "Lessondays.findById", query = "SELECT l FROM Lessondays l WHERE l.id = :id"),
    @NamedQuery(name = "Lessondays.findByDay", query = "SELECT l FROM Lessondays l WHERE l.day = :day")
})
public class Lessondays implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "DAY")
    private String day;
    @JoinColumn(name = "LESSONID", referencedColumnName = "ID")
    @ManyToOne
    private Lessons lessonid;

    public Lessondays()
    {
    }

    public Lessondays(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public Lessons getLessonid()
    {
        return lessonid;
    }

    public void setLessonid(Lessons lessonid)
    {
        this.lessonid = lessonid;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lessondays))
        {
            return false;
        }
        Lessondays other = (Lessondays) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString()
    {
        return "com.crimore.model.Lessondays[ id=" + id + " ]";
    }
    
}
