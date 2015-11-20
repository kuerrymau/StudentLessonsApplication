/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimore.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Chayanne
 */
@Entity
@Table(name = "LESSONS")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Lessons.findAll", query = "SELECT l FROM Lessons l"),
    @NamedQuery(name = "Lessons.findById", query = "SELECT l FROM Lessons l WHERE l.id = :id"),
    @NamedQuery(name = "Lessons.findByName", query = "SELECT l FROM Lessons l WHERE l.name = :name"),
    @NamedQuery(name = "Lessons.findByFaculty", query = "SELECT l FROM Lessons l WHERE l.faculty = :faculty"),
    @NamedQuery(name = "Lessons.findByTotalnoofstudentss", query = "SELECT l FROM Lessons l WHERE l.totalnoofstudentss = :totalnoofstudentss")
})
public class Lessons implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @Size(max = 255)
    @Column(name = "FACULTY")
    private String faculty;
    @Column(name = "TOTALNOOFSTUDENTSS")
    private Integer totalnoofstudentss;
    @OneToMany(mappedBy = "lessonid")
    private Collection<Lessondays> lessondaysCollection;

    public Lessons()
    {
    }

    public Lessons(Integer id)
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFaculty()
    {
        return faculty;
    }

    public void setFaculty(String faculty)
    {
        this.faculty = faculty;
    }

    public Integer getTotalnoofstudentss()
    {
        return totalnoofstudentss;
    }

    public void setTotalnoofstudentss(Integer totalnoofstudentss)
    {
        this.totalnoofstudentss = totalnoofstudentss;
    }

    @XmlTransient
    public Collection<Lessondays> getLessondaysCollection()
    {
        return lessondaysCollection;
    }

    public void setLessondaysCollection(Collection<Lessondays> lessondaysCollection)
    {
        this.lessondaysCollection = lessondaysCollection;
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
        if (!(object instanceof Lessons))
        {
            return false;
        }
        Lessons other = (Lessons) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.crimore.model.Lessons[ id=" + id + " ]";
    }
    
}
